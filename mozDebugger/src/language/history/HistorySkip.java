package language.history;

public class HistorySkip implements IHistory {

	@Override
	public HistoryType getType() {
		// TODO Auto-generated method stub
		return HistoryType.SKIP;
	}
	public String toString()
	{
		return "skip";
	}

}
