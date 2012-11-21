package language.history;

public class HistorySend implements IHistory {

	private String chan;
	private int instruction;
	@Override
	public HistoryType getType() {
		// TODO Auto-generated method stub
		return HistoryType.SEND;
	}
	public String getChan() {
		return chan;
	}
	public void setChan(String chan) {
		this.chan = chan;
	}
	
	public HistorySend(String chan, int instruction) {
		super();
		this.instruction=instruction;
		this.chan = chan;
	}
	public int getInstruction() {
		return instruction;
	}
	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}
	@Override
	public String toString() {
		return "wrote on " + chan;
	}

}
