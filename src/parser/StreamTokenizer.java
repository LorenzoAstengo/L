package parser;

import static parser.TokenType.*;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class StreamTokenizer implements Tokenizer {
	private static final String regEx;
	private static final Map<String, TokenType> keywords = new HashMap<>();
	private static final Map<String, TokenType> symbols = new HashMap<>();

	private boolean hasNext = true; // any stream contains at least the EOF 
	
	// token
	private TokenType tokenType;
	private String tokenString;
	private int intValue;
	private boolean boolValue;
	private String stringValue;
	private final Scanner ss;

	static {
		// remark: groups must correspond to the ordinal of the corresponding
		// token type
		
		//skip
		final String skipRegEx = "(\\s+|//.*)"; // group 1
		
		//ident
		final String identRegEx = "([a-zA-Z][a-zA-Z0-9]*)"; // group 2
		
		//L++ 
		final String numRegEx="(0[xX][0-9a-fA-F]+|0|[1-9][0-9]*)"; //group3
		final String stringRegEx = "(\"([\\w\\d\\s!#-@\\[\\]^_'{-~]*|([\\\\]{2})*|([\\\\][\"]{1})*)*\")";//gruppo 4
		final String symbolRegEx="(\\+|\\*|==|=|\\(|\\)|\\[|\\]|;|,|\\{|\\}|-|!|&&|\\\"|\\^|([\\\\][\\\\\\/])|([\\\\\\/][\\\\])|#|\\|)";
		
		//final regex
		regEx = skipRegEx + "|" + identRegEx + "|" + numRegEx  + "|" + stringRegEx + "|" + symbolRegEx;
	}

	static {
		keywords.put("print", PRINT);
		keywords.put("let", LET);
		keywords.put("false", BOOL);
		keywords.put("true", BOOL);
		keywords.put("if", IF);
		keywords.put("else", ELSE);
		keywords.put("fst", FST);
		keywords.put("snd", SND);
		
		//L++ keywords extensions
		keywords.put("in", IN);
		keywords.put("while", WHILE);
	}

	static {
		symbols.put("+", PLUS);
		symbols.put("*", TIMES);
		symbols.put("=", ASSIGN);
		symbols.put("(", OPEN_PAR);
		symbols.put(")", CLOSE_PAR);
		symbols.put("[", OPEN_PAIR);
		symbols.put("]", CLOSE_PAIR);
		symbols.put(";", STMT_SEP);
		symbols.put(",", EXP_SEP);
		symbols.put("{", OPEN_BRACE);
		symbols.put("}", CLOSE_BRACE);
		symbols.put("-", MINUS);
		symbols.put("!", NOT);
		symbols.put("&&", AND);
		symbols.put("==", EQ);
	
		//L++ symbols extensions
		symbols.put("^", CONCAT); 
		symbols.put("\\/", UNION);
		symbols.put("/\\", INTERSECT);
		symbols.put("#", DIM);
	}

	public StreamTokenizer(Reader reader) {
		ss = new StreamScanner(regEx, reader);
	}

	private void checkType() throws TokenizerException {
		tokenString = ss.group(); 
		
		
		/** IDENT, BOOL **/
		if(ss.group(IDENT.ordinal()) != null) {
			tokenType = keywords.get(tokenString); 
			if(tokenType == null)  
				tokenType=IDENT;
			if(tokenType==BOOL) 
				boolValue = Boolean.parseBoolean(tokenString);
			return;
		}
		
		
		/** NUM **/
		if(ss.group(NUM.ordinal())!=null) { 
			tokenType = NUM;
			try { 				
				intValue = Integer.parseInt(tokenString);
			}
			catch(NumberFormatException e){ // esasdecimale Hex
				/***
				* parseInt non ha funzionato: NUM e` in formato esadecimale!
				* uso il metodo decode per decodificare l'esadecimale sia
				* nel caso "0x..." che nel caso "0X..."
				* info qua:
				* https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html#decode(java.lang.String)
				***/
				intValue = Integer.decode(tokenString);
			}
			return;
		}
		
		/** SKIP **/
		if (ss.group(SKIP.ordinal()) != null) { // SKIP
			tokenType = SKIP;
			return;
		}
		
		/** L++ STRINGLIT **/
		if (ss.group(STRINGLIT.ordinal()) != null) { 
			tokenType = STRINGLIT;
			stringValue = tokenString.substring(1, tokenString.length() - 1).replace("\\\"", "\"").replace("\\\\","\\");
			return;
		}
	
		/** OTHERS **/
		tokenType = symbols.get(tokenString);
		if (tokenType==null) {			
		//se tokenType non e` un simbolo exception errore di sintassi
			throw new TokenizerException("Unrecognized string:"+tokenString.replace("\"", "\\\""));
		}
	}
	
	/*** OVERRIDE ***/
	
	@Override
	public TokenType next() throws TokenizerException {
		do {
			tokenType = null;
			tokenString = "";
			try {
				if (hasNext && !ss.hasNext()) {
					hasNext = false;
					return tokenType = EOF;
				}
				ss.next();
			} catch (ScannerException e) {
			
				throw new TokenizerException(e);
			}
			checkType();
		} while (tokenType == SKIP);
		return tokenType;
	}

	private void checkValidToken() {
		if (tokenType == null)
			throw new IllegalStateException();
	}

	private void checkValidToken(TokenType ttype) {
		if (tokenType != ttype)
			throw new IllegalStateException();
	}

	@Override
	public String tokenString() {
		checkValidToken();
		return tokenString;
	}

	@Override
	public boolean boolValue() {
		checkValidToken(BOOL);
		return boolValue;
	}
	
	//L++
	@Override
	public String stringValue() {
		checkValidToken(STRINGLIT);
		return stringValue;
	}
	//L++ end
	
	@Override
	public int intValue() {
		checkValidToken(NUM);
		return intValue;
	}

	@Override
	public TokenType tokenType() {
		checkValidToken();
		return tokenType;
	}

	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@Override
	public void close() throws TokenizerException {
		try {
			ss.close();
		} catch (ScannerException e) {
			throw new TokenizerException(e);
		}
	} 

}
