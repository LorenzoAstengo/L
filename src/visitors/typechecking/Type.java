package visitors.typechecking;

public interface Type {
	
	default Type checkEqual(Type found) throws TypecheckerException {
		if (!equals(found))
			throw new TypecheckerException(found.toString(), toString());
		return this;
	}

	default void checkIsPairType() throws TypecheckerException {
		if (!(this instanceof PairType))
			throw new TypecheckerException(toString(), PairType.TYPE_NAME);
	}

	default Type getFstPairType() throws TypecheckerException {
		checkIsPairType();
		return ((PairType) this).getFstType();
	}

	default Type getSndPairType() throws TypecheckerException {
		checkIsPairType();
		return ((PairType) this).getSndType();
	}
	
	//L++ SetType
	default void checkIsSetType() throws TypecheckerException {
		if (!(this instanceof SetType))
			throw new TypecheckerException(toString(), SetType.TYPE_NAME);
	}
	
	default Type getElemType() throws TypecheckerException {
		checkIsSetType();
		return ((SetType) this).getElemType();
	}
	
	//L++ Countable Types SET, STRING
	default void checkIsCountable() throws TypecheckerException {
		if (!(this instanceof SetType)&&!(this instanceof StringType)) {
			throw new TypecheckerException(toString(), StringType.TYPE_NAME+" OR "+SetType.TYPE_NAME);
		}
	}
	
}
