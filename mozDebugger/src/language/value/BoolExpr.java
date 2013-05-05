package language.value;
/** this class is used for equality tests on integer expressions**/
public class BoolExpr implements IValue{

	
	static ValueType type = ValueType.BOOL_EXP;
	private IntExp sx, dx;
	@Override
	public ValueType getType() {
		// TODO Auto-generated method stub
		return type;
	}

	public BoolExpr(IntExp sx, IntExp dx)
	{
		this.sx = sx;
		this.dx = dx;
	}
	@Override
	public void rename(String old_id, String new_id) {
		// TODO Auto-generated method stub
		
	}
	public BoolExpr clone()
	{
		return new BoolExpr((IntExp)sx.clone(), (IntExp)dx.clone());
	}
	
	//perhaps we need an evaluate method 
}
