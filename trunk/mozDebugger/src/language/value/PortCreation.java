package language.value;

public class PortCreation implements IValue {

	@Override
	public ValueType getType() {
		return ValueType.PORT;
	}
	
	public String toString()
	{
		return "port";
	}

	@Override
	public void rename(String old_id, String new_id) {
		// TODO Auto-generated method stub
		
	}
	
	public PortCreation clone()
	{
		return new PortCreation();
	}
}
