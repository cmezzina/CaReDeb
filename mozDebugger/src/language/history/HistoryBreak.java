package language.history;

public class HistoryBreak implements IHistory {

	static HistoryType type = HistoryType.BREAK;
	@Override
	public HistoryType getType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public HistoryBreak clone()
	{
		return new HistoryBreak();
	}

}
