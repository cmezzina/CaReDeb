package language.value;
/** this class is used for equality tests on integer expressions**/
public class BoolExpr implements IValue{

	
	static ValueType type = ValueType.BOOL_EXP;
	private BoolOp op;
	private IntExp sx, dx;
	@Override
	public ValueType getType() {
		// TODO Auto-generated method stub
		return type;
	}
	
	public BoolOp getOp()
	{
		return this.op;
	}

	public BoolExpr(IntExp sx, IntExp dx, BoolOp op)
	{
		this.sx = sx;
		this.dx = dx;
		this.op = op;
	}
	
	public IntExp getSx()
	{
		return this.sx;
	}
	public IntExp getDx()
	{
		return this.dx;
	}
	
	@Override
	public void rename(String old_id, String new_id) {

		sx.rename(old_id, new_id);
		dx.rename(old_id, new_id);
	}
	public BoolExpr clone()
	{
		return new BoolExpr((IntExp)sx.clone(), (IntExp)dx.clone(), op);
	}
	
	public String toString()
	{
		String op=null;
		if(this.op == BoolOp.EQ)
			op=" == ";
		else
			if(this.op == BoolOp.GT)
				op=" > ";
		else
			if(this.op == BoolOp.LT)
				op=" < ";
		return sx.toString() + op + dx.toString();
	
	}
	
	//perhaps we need an evaluate method 
}
