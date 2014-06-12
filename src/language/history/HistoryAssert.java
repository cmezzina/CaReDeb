package language.history;

import java.io.Serializable;

import language.value.IValue;

public class HistoryAssert implements IHistory, Serializable {
	
	static HistoryType type = HistoryType.ASSERT;
	private IValue guard;

	public HistoryAssert(IValue guard)
	{
		this.guard = guard;
	}
	@Override
	public HistoryType getType() {
		// TODO Auto-generated method stub
		return type;
	}

	public IValue getGuard() {
		return guard;
	}

	@Override
	public int getInstruction() {
		// TODO Auto-generated method stub
		return 0;
	}
	public IHistory clone()
	{
		return new HistoryAssert(guard);
	}
	public String toString()
	{
		return "assert( "+guard+" )";
	}

}
