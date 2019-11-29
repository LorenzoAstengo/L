package parser.ast;

import static java.util.Objects.requireNonNull;

import visitors.Visitor;

public class SetLiteral implements Exp {
	private final ExpSeq expSeq;
	
	public SetLiteral(ExpSeq expseq) {
		this.expSeq=requireNonNull(expseq);
	}
	
	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitSetLiteral(expSeq);
	}
	
}
