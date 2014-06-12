package language.value;

public class SubValue implements BinaryIntExp{

	final static ValueType type = ValueType.SUB;
	
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
	public SubValue(IntExp sx, IntExp dx) {
		super();
		this.sx = sx;
		this.dx = dx;
	}
	
	public String toString()
	{
		return sx + " - "+ dx;
		
	}
	@Override
	public void rename(String old_id, String new_id) {
		this.sx.rename(old_id, new_id);
		this.dx.rename(old_id, new_id);
	}

	public IValue clone()
	{

		return new SubValue((IntExp)sx.clone(), (IntExp)dx.clone());
	}
}
