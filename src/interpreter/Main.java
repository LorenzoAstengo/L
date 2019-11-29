package interpreter;
import parser.MyParser;
import parser.ParserException;
import parser.StreamTokenizer;
import parser.ast.Prog;
import visitors.evaluation.Eval;
import visitors.evaluation.EvaluatorException;
import visitors.typechecking.TypeCheck;
import visitors.typechecking.TypecheckerException;

import static java.lang.System.err;

import java.io.*;


//Simple Shell for L++
public class Main {
	
	static Reader tryOpenReader(String nameFile) {
		if (nameFile != null) {
			try {
				return new FileReader(nameFile);
			}
			catch (FileNotFoundException e){
				System.err.println("Cannot open " + nameFile);
			}
		}
		System.out.println("Inserisci il testo da analizzare (non inserire \";\" nell'ultima riga, termina con CTRL+D):");
		return new InputStreamReader(System.in);
	}

	public static void main(String[] args) {		
		String filein = null, fileout = null;
		Boolean tc=true;
		if (args.length > 1) {
			for(int i=0; i<args.length; i++) {
				switch (args[i]) {
				case "-i":
					i++;
					filein=args[i];			
					break;
					
				case "-o":
					i++;
					fileout=args[i];
					break;
				
				case "-ntc":
					tc=false;
					break;

				default:
					throw new IllegalArgumentException();					
				}
			}	
		}
		
		//System.out.println("**DEBUG** Input:"+filein+" Output:"+fileout+" TypeCheckOption:"+tc);
		
		Reader read=tryOpenReader(filein);
		StreamTokenizer st=new StreamTokenizer(read);
		MyParser parser=new MyParser(st);
		try {
			
				//Parsing : Syntax 
				Prog prog=parser.parseProg(filein==null);				
			
				//Type-Checking	: Static Semantics
				if(tc) {
					prog.accept(new TypeCheck());
				}
			
			//Evaluation : Dynamic Semantics
			if (fileout==null)
				prog.accept(new Eval());
			else {
				PrintWriter writer=new PrintWriter(fileout);
				prog.accept(new Eval(writer));
				writer.close();
			}
		}	
		
		//Exceptions
		catch (ParserException pE) {
				System.err.println("Syntax error: " + pE.getMessage());
		}
		catch (IOException ioE) {
			System.err.println("Input error: " + ioE.getMessage());
		}
		catch (TypecheckerException tcE) {
			System.err.println("Static error: " + tcE.getMessage());
		}
		catch (EvaluatorException eE) {
			System.err.println("Dynamic error: " + eE.getMessage());
		}
		catch (Throwable e) {
			err.println("Unexpected error.");
			e.printStackTrace();
		}
	}
}
