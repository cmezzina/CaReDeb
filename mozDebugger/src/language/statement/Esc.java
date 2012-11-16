package language.statement;

public class Esc implements IStatement {

	

	@Override
	public void rename(String old_id, String new_id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StatementType getType() {
		return StatementType.ESC;
	}
	public Esc clone()
	{
		return new Esc();
	}
	
	public String toString()
	{
		//return "esc";
		return "";
	}
}
