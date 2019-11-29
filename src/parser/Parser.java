package parser;

import java.io.IOException;

import parser.ast.Prog;

public interface Parser {

	Prog parseProg(boolean interactiveMode) throws ParserException, IOException;

}