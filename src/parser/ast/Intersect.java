package parser.ast;

import visitors.Visitor;

public class Intersect extends BinaryOp {
	
	public Intersect(Exp left, Exp right) {
		super(left,right);
	}
	
	@Override
	public <T> T accept(Visitor<T> visitor) {
		// TODO Auto-generated method stub
		return visitor.visitIntersect(left, right);
	}

}