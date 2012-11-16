package language.statement;


public class Nil implements IStatement {

	@Override
	public StatementType getType() {
		return StatementType.NIL;
	}
	
	public String toString()
	{
		return "nil";
	}

	@Override
	public void rename(String old_id, String new_id) {
		// TODO Auto-generated method stub
		
	}

	public Nil clone()
	{
		return new Nil();
	}
}
