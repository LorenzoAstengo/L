package visitors.evaluation;
import java.util.HashSet;
import java.util.Iterator;


/***
 * 
 * @author ac
 *Implementa i Set! Finalmenteeeeeeeeee!!!!!!! 
 *
 */

public class SetValue implements Value, Iterable<Value> {
	private final HashSet<Value> elements ;
	
	public SetValue() {
		elements = new HashSet<>();
	}
	
	public SetValue(SetValue set) {
		this();
		for (Value element : set.elements)
			this.add(element);
	}
	
	public SetValue(Value head, SetValue tail) {
		this(tail);
		elements.add(head);
	}
	
	public SetValue(Value value) {
		this();
		this.add(value);
	}

	@Override
	public Iterator<Value> iterator() {
		return elements.iterator();
	}

	@Override 
	public SetValue asSet() {
		return this;
	}
	
	@Override
	public String toString() {
		return "{" +elements.toString().substring(1).substring(0, (elements.toString().length()-2))+"}";
	}

	public Value add(Value e){
		elements.add(e);
		return this	;
	}
	
	public Value add(SetValue set){
		for (Value element : set.elements)
			this.add(element);	
		return this;
	}
	
	@Override
	public int hashCode() {
		return elements.hashCode();
	}
	
	public int dim() {
		return elements.size();
	}
	
	public Value intersect(SetValue set) {
		elements.retainAll(set.elements);
		return this;
	}
	
	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SetValue))
			return false;
		return elements.equals(((SetValue) obj).elements);
	}


}
