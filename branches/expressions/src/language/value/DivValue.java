package language.value;

public class DivValue implements BinaryIntExp {

    final static ValueType type = ValueType.DIV;

    private IntExp sx, dx;

    @Override
    public ValueType getType() {
        // TODO Auto-generated method stub
        return type;
    }

    @Override
    public IntExp getSx() {
        return sx;
    }

    @Override
    public void setSx(IntExp sx) {
        this.sx = sx;
    }

    @Override
    public IntExp getDx() {
        return dx;
    }

    @Override
    public void setDx(IntExp dx) {
        this.dx = dx;
    }

    public DivValue(IntExp sx, IntExp dx) {
        super();
        this.sx = sx;
        this.dx = dx;
    }

    @Override
    public String toString() {
        return sx + " / " + dx;

    }

    @Override
    public void rename(String old_id, String new_id) {
        this.sx.rename(old_id, new_id);
        this.dx.rename(old_id, new_id);
    }

    @Override
    public IValue clone() {

        return new DivValue((IntExp) sx.clone(), (IntExp) dx.clone());
    }
}
