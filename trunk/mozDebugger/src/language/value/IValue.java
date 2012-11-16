package language.value;

public interface IValue extends Cloneable{
	ValueType getType();
	public void rename(String old_id, String new_id);
	
	public IValue clone();
}
