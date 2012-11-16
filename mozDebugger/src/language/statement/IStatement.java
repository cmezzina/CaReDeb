package language.statement;

public interface IStatement extends Cloneable{

	StatementType getType();
	
	public void rename(String old_id, String new_id);

	//clone should be of an abstract class
	public IStatement clone();
}

