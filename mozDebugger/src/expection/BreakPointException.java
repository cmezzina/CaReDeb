package expection;

import language.statement.IStatement;

public class BreakPointException extends Exception {

	private IStatement stm;
	
	public BreakPointException(IStatement stm)
	{
		this.stm= stm;
	}

	public IStatement getStm() {
		return stm;
	}

	public void setStm(IStatement stm) {
		this.stm = stm;
	}
	
}
