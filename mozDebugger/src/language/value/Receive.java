package language.value;


public class Receive implements IValue {

	private String from;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Receive(String from) {
		super();
		this.from = from;
	}

	@Override
	public ValueType getType() {
		return ValueType.RECEIVE;
	}
	
	public String toString()
	{
		return  "{ receive "+ from +" }";
	}

	@Override
	public void rename(String old_id, String new_id) {
	
		if(from.equals(old_id))
			from = new_id;
	}
	
	public Receive clone()
	{
		return new Receive(from);
	}
}
