package parser.ast;

import visitors.Visitor;

public class Concat extends BinaryOp {
	public Concat(Exp left, Exp right) {
		super(left, right);
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitConcat(left, right);
	}

}
