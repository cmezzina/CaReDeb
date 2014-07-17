package exception;

import language.statement.IStatement;

public class BreakPointException extends Exception {

	private IStatement stm;
	private String msg;
	
	public BreakPointException(IStatement stm, String msg)
	{
		this.stm= stm;
		this.msg = msg;
	}

	public IStatement getStm() {
		return stm;
	}

	public void setStm(IStatement stm) {
		this.stm = stm;
	}
	public String getMsg()
	{
		return msg;
	}
}
