package language.history;

public class HistorySend implements IHistory {

	private String chan;
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
	
	public HistorySend(String chan) {
		super();
		this.chan = chan;
	}
	@Override
	public String toString() {
		return "wrote on channel " + chan;
	}

}
