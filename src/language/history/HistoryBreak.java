package language.history;

public class HistoryBreak implements IHistory {

    static HistoryType type = HistoryType.BREAK;

    @Override
    public HistoryType getType() {
        // TODO Auto-generated method stub
        return type;
    }

    @Override
    public HistoryBreak clone() {
        return new HistoryBreak();
    }

    @Override
    public String toString() {
        return "breakpoint";
    }

    @Override
    public int getInstruction() {
        // TODO Auto-generated method stub
        return 0;
    }

}
