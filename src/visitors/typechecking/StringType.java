package visitors.typechecking;

import com.sun.jdi.Value;

import visitors.evaluation.IntValue;

public class StringType implements Type {
private final String string;
	
	public static final String TYPE_NAME="STRING";
	
	public StringType() {
		this.string="";
	}
	
	public StringType(String string) {
		this.string=string;
	}
	
	public Type getType() {
		return this;
	}
	
	@Override
	public int hashCode() {
		return 31 * string.hashCode();
	}

	@Override
	public String toString() {
		return TYPE_NAME;
	}
	
	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof StringType))
			return false;
		else return true;
		
	}
}
