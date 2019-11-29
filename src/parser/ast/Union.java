package parser.ast;

import visitors.Visitor;

public class Union extends BinaryOp {
	
	public Union(Exp left, Exp right) {
		super(left,right);
	}
	
	@Override
	public <T> T accept(Visitor<T> visitor) {
		// TODO Auto-generated method stub
		return visitor.visitUnion(left, right);
	}

}
