package language.statement;

import language.value.IValue;

public class Assert implements IStatement {
	
	private IValue guard;

	public Assert(IValue guard)
	{
		this.guard = guard;
	}
	
	public IValue getGuard()
	{
		return guard;
	}
	@Override
	public StatementType getType() {
		// TODO Auto-generated method stub
		return StatementType.ASSERT;
	}

	@Override
	public void rename(String old_id, String new_id) {
		// TODO Auto-generated method stub
		guard.rename(old_id, new_id);
	}

	public Assert clone()
	{
		return new Assert(guard);
	}
	
	public String toString()
	{
		return "assert( "+ guard.toString() + " )";
	}
}
