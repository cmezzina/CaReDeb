package language.statement;

public class Skip implements IStatement {

	@Override
	public StatementType getType() {
		return StatementType.SKIP;
	}
	
	public String toString()
	{
		return "skip";
	}

	@Override
	public void rename(String old_id, String new_id) {
		// TODO Auto-generated method stub
		
	}
	public Skip clone()
	{
		return new Skip();
		
	}
}
