package language.history;

public class HistoryVar implements IHistory {

	private String id;
	public HistoryVar(String id) {
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
		return HistoryType.VAR;
	}

	public String toString()
	{
		return "created variable "+id;
	}
}
