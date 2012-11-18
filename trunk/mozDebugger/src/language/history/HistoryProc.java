package language.history;

public class HistoryProc implements IHistory {
	private String id;
	
	public HistoryProc(String id) {
		super();
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public HistoryType getType() {
		// TODO Auto-generated method stub
		return HistoryType.PROCEDURE;
	}

	public String toString()
	{
		return "new function "+id;
	}
}
