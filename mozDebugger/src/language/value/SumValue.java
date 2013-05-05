package language.value;

public class SumValue implements BinaryIntExp{

	final static ValueType type = ValueType.SUM;
	
	private IntExp sx,dx;
	@Override
	public ValueType getType() {
		// TODO Auto-generated method stub
		return type;
	}
	public IntExp getSx() {
		return sx;
	}
	public void setSx(IntExp sx) {
		this.sx = sx;
	}
	public IntExp getDx() {
		return dx;
	}
	public void setDx(IntExp dx) {
		this.dx = dx;
	}
	public SumValue(IntExp sx, IntExp dx) {
		super();
		this.sx = sx;
		this.dx = dx;
	}
	
	public String toString()
	{
		return sx + " + "+ dx;
	}
	@Override
	public void rename(String old_id, String new_id) {
		// TODO Auto-generated method stub
		
	}

	public IValue clone()
	{

		return new SumValue((IntExp)sx.clone(), (IntExp)dx.clone());
	}
}
