package parser;

//added L++ extensions

public enum TokenType { // important: SKIP, IDENT, NUM, L++ STRINGLIT must have ordinals 1, 2, 3, and 4
	EOF, SKIP, IDENT, NUM, STRINGLIT, PRINT, LET, PLUS, TIMES, EQ, ASSIGN, OPEN_PAR, CLOSE_PAR, OPEN_PAIR, CLOSE_PAIR, STMT_SEP,
	EXP_SEP, MINUS, NOT, AND, BOOL, IF, ELSE, FST, SND, CONCAT, UNION, INTERSECT, DIM, IN, 
	WHILE, OPEN_BRACE, CLOSE_BRACE, COMMAS, STRING, HEXL
}

