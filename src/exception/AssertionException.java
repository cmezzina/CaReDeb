package exception;

import language.statement.IStatement;

public class AssertionException extends Exception {

    // similar to breakpoint exception
    private IStatement stm;
    private String msg;

    public AssertionException(IStatement stm, String msg) {
        this.stm = stm;
        this.msg = msg;
    }

    public IStatement getStm() {
        return stm;
    }

    public String getMsg() {
        return msg;
    }

    public void setStm(IStatement stm) {
        this.stm = stm;
    }

}