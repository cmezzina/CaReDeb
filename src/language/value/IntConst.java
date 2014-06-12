package language.value;


public class IntConst implements IntExp {

	final static ValueType type = ValueType.CONST;
	private int value ;
	@Override
	public ValueType getType() {
		// TODO Auto-generated method stub
		return type;
	}

	public IntConst(int value)
	{
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public String toString()
	{
		return ""+value;
	}

	@Override
	public void rename(String old_id, String new_id) {
		// TODO Auto-generated method stub
		
	}
	public IValue clone()
	{

		return new IntConst(value);
	}

}
