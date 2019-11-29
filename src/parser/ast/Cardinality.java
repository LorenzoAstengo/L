package parser.ast;

import visitors.Visitor;

public class Cardinality extends UnaryOp{
	public Cardinality(Exp exp) {
		super(exp);
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitCardinality(exp);
	}
}
