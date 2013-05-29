package language.history;

public class HistoryBreak implements IHistory {

	static HistoryType type = HistoryType.BREAK;
	@Override
	public HistoryType getType() {
		// TODO Auto-generated method stub
		return type;
	}
	
	public HistoryBreak clone()
	{
		return new HistoryBreak();
	}

	public String toString()
	{
		return "breakpoint";
	}
}
