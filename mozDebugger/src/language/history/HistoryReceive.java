package language.history;

public class HistoryReceive implements IHistory {

	private String from;
	private String var;
	
	private int instruction;

	
	public HistoryReceive(String from, String var, int instruction) {
		super();
		this.from = from;
		this.var = var;
		this.instruction= instruction;
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



	public int getInstruction() {
		return instruction;
	}



	public void setInstruction(int instruction) {
		this.instruction = instruction;
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
