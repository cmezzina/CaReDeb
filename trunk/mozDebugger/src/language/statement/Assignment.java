package language.statement;

import language.value.IValue;

public class Assignment implements IStatement {

	
	private String id;
	private IValue v;
	
	private IStatement stm;
	
	public Assignment(String id, IValue v, IStatement stm) {
		super();
		this.id = id;
		this.v = v;
		this.stm =stm;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setStm(IStatement stm) {
		this.stm = stm;
	}
	public IValue getV() {
		return v;
	}
	public void setV(IValue v) {
		this.v = v;
	}
	public IStatement getStm() {
		return stm;
	}
	@Override
	public StatementType getType() {
		// TODO Auto-generated method stub
		return StatementType.LET;
	}
	public String toString()
	{
		return "let "+ id + " = " + v + " in " + stm + " end";
	}
	@Override
	public void rename(String old_id, String new_id) {

		//name clashing
		if(id.equals(old_id))
		{
			return;
		}
		v.rename(old_id, new_id);
		stm.rename(old_id, new_id);
		
	}
	public Assignment clone()
	{
		return new Assignment(id, v.clone(), stm.clone());
	}
}
