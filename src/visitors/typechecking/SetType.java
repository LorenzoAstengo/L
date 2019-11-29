package visitors.typechecking;

import static java.util.Objects.requireNonNull;

public class SetType implements Type {
	private final Type elemType;
	
	public static final String TYPE_NAME="SET";
	
	public SetType(Type elemType) {
		this.elemType=requireNonNull(elemType);
	}
	
	public Type getElemType() {
		return elemType;
	}
	
	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SetType))
			return false;
		SetType st = (SetType) obj;
		return elemType.equals(st.elemType);
	}
	
	@Override
	public int hashCode() {
		return 31 * elemType.hashCode();
	}

	@Override
	public String toString() {
		return elemType+ " " + TYPE_NAME;
	}
	
}
