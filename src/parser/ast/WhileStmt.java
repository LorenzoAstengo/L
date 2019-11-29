package parser.ast;

import static java.util.Objects.requireNonNull;

import visitors.Visitor;

public class WhileStmt implements Stmt {
	private final Exp exp;
	private final Block whileBlock;
	
	
	public WhileStmt(Exp exp, Block whileBlock) {
		this.exp = requireNonNull(exp);
		this.whileBlock = requireNonNull(whileBlock);
	}
	
	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitWhileStmt(exp, whileBlock);
	}

}
