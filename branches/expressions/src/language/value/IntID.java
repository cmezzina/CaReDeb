package language.value;

public class IntID implements IntExp {
    final static ValueType type = ValueType.INT_ID;
    private String value;

    @Override
    public ValueType getType() {
        // TODO Auto-generated method stub
        return type;
    }

    public IntID(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public IValue clone() {
        return new IntID(value);
    }

    @Override
    public void rename(String old_id, String new_id) {

        if (this.value.equals(old_id)) {
            this.value = new_id;
        }
    }

    @Override
    public String toString() {
        return value;
    }

}
