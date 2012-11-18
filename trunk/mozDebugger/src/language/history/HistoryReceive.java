package language.history;

public class HistoryReceive implements IHistory {

	private String from;
	private String var;
	
	
	
	public HistoryReceive(String from, String var) {
		super();
		this.from = from;
		this.var = var;
	}



	public String getFrom() {
		return from;
	}



	public void setFrom(String from) {
		this.from = from;
	}



	public String getVar() {
		return var;
	}



	public void setVar(String var) {
		this.var = var;
	}



	@Override
	public HistoryType getType() {
		return HistoryType.RECEIVE;
	}

	public String toString()
	{
		return "read in "+var + " from "+from;
	}
}
