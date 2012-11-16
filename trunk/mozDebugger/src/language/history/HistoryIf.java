package language.history;

import language.statement.IStatement;

public class HistoryIf implements IHistory {

	private String guard;
	private IStatement body;
	private boolean left;
	
	public HistoryIf(String guard, IStatement body, boolean left) {
		super();
		this.guard = guard;
		this.body = body;
		this.left = left;
	}


	public String getGuard() {
		return guard;
	}


	public void setGuard(String guard) {
		this.guard = guard;
	}


	public IStatement getBody() {
		return body;
	}


	public void setBody(IStatement body) {
		this.body = body;
	}


	public boolean isLeft() {
		return left;
	}


	public void setLeft(boolean left) {
		this.left = left;
	}


	@Override
	public HistoryType getType() {
		// TODO Auto-generated method stub
		return HistoryType.IF;
	}

	
	public String toString()
	{
		String ret = "if ( "+guard +" ) ";
		if(left)
			return ret +" then _ else "+body.toString() + " end ";
		
		return ret +" then  "+body.toString() + " else _ end ";
		
	}
}
