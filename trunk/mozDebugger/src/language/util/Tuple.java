package language.util;

public class Tuple<E,V> {
	
	private E first;
	private V second;
	public Tuple(E firt, V second) {
		super();
		this.first = firt;
		this.second = second;
	}
	public E getFirst() {
		return first;
	}
	public void setFirst(E firt) {
		this.first = firt;
	}
	public V getSecond() {
		return second;
	}
	public void setSecond(V second) {
		this.second = second;
	}
	
	public String toString()
	{
		return "("+first.toString() + " , "+ second.toString()+")";
	}
}
