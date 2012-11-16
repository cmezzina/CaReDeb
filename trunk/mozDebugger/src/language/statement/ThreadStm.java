package language.statement;

public class ThreadStm implements IStatement {

	private IStatement body;

	public IStatement getBody() {
		return body;
	}

	public void setBody(IStatement body) {
		this.body = body;
	}

	public ThreadStm(IStatement body) {
		super();
		this.body = body;
	}

	@Override
	public StatementType getType() {
		return StatementType.SPAWN;
	}
	
	public String toString()
	{
		return "thread "+ body + " end ";
	}

	@Override
	public void rename(String old_id, String new_id) {
		// TODO Auto-generated method stub
		body.rename(old_id, new_id);
	}
	
	public ThreadStm clone()
	{
		return new ThreadStm(body.clone());
	}
}
