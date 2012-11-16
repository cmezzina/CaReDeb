package language.statement;

public class Sequence implements IStatement {

		

		private IStatement sx;
		private IStatement dx;
		
		public Sequence(IStatement sx, IStatement dx)
		{
			this.sx=sx;
			this.dx= dx;
		}
		public IStatement getSx() {
			return sx;
		}
		public void setSx(IStatement sx) {
			this.sx = sx;
		}
		public IStatement getDx() {
			return dx;
		}
		public void setDx(IStatement dx) {
			this.dx = dx;
		}
		@Override
		public StatementType getType() {
			return StatementType.SEQUENCE;
		}
		
		public String toString()
		{
			
			if(dx !=null && dx.getType() != StatementType.ESC)
				return sx.toString() + ";" + dx.toString();
			return sx.toString();
		}
		@Override
		public void rename(String old_id, String new_id) {
			this.sx.rename(old_id, new_id);
			this.dx.rename(old_id, new_id);
		}
		
		public Sequence clone()
		{
			return new Sequence(sx.clone(), dx.clone());
		}
}
