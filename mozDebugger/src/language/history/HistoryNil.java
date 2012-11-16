package language.history;

public class HistoryNil implements IHistory {

	@Override
	public HistoryType getType() {
		// TODO Auto-generated method stub
		return HistoryType.NIL;
	}

	public String toString()
	{
		return "nil";
	}
}
