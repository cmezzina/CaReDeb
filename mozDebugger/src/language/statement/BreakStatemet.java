package language.statement;

public class BreakStatemet implements IStatement {

	static StatementType type = StatementType.BREAK;
	@Override
	public StatementType getType() {
		// TODO Auto-generated method stub
		return type;
	}

	@Override
	public void rename(String old_id, String new_id) {
		// TODO Auto-generated method stub

	}
	public BreakStatemet clone()
	{
		return new BreakStatemet();
	}
}
