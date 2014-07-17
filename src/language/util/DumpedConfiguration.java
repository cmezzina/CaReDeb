package language.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import language.history.IHistory;
import language.statement.IStatement;
import language.value.IValue;
import language.value.Procedure;

public class DumpedConfiguration implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private HashMap<String, IValue> store;
    private HashMap<String, Channel> chans;
    // procedure store
    private HashMap<String, Procedure> procs;
    // thread pool
    private HashMap<String, IStatement> threadlist;
    private HashMap<String, ArrayList<IHistory>> history;

    private HashMap<String, IValue> expressions;
    // channel/port store
    private HashMap<String, Integer> thread_child;
    private HashMap<String, Integer> thread_chan;
    private HashMap<String, Tuple<String, Integer>> parenthood;

    private HashMap<String, Tuple<String, Integer>> variables;
    private int pc;

    public DumpedConfiguration(HashMap<String, IValue> store,
            HashMap<String, Channel> chans, HashMap<String, Procedure> procs,
            HashMap<String, IStatement> threadlist,
            HashMap<String, ArrayList<IHistory>> history,
            HashMap<String, IValue> expressions,
            HashMap<String, Integer> thread_child,
            HashMap<String, Integer> thread_chan,
            HashMap<String, Tuple<String, Integer>> parenthood,
            HashMap<String, Tuple<String, Integer>> variables, int pc) {
        super();
        this.store = store;
        this.chans = chans;
        this.procs = procs;
        this.threadlist = threadlist;
        this.history = history;

        this.expressions = expressions;
        this.thread_chan = thread_chan;
        this.thread_child = thread_child;
        this.parenthood = parenthood;
        this.variables = variables;
        this.pc = pc;
    }

    public HashMap<String, IValue> getExpressions() {
        return expressions;
    }

    public void setExpressions(HashMap<String, IValue> expressions) {
        this.expressions = expressions;
    }

    public HashMap<String, Integer> getThread_child() {
        return thread_child;
    }

    public void setThread_child(HashMap<String, Integer> thread_child) {
        this.thread_child = thread_child;
    }

    public HashMap<String, Integer> getThread_chan() {
        return thread_chan;
    }

    public void setThread_chan(HashMap<String, Integer> thread_chan) {
        this.thread_chan = thread_chan;
    }

    public HashMap<String, Tuple<String, Integer>> getParenthood() {
        return parenthood;
    }

    public void setParenthood(HashMap<String, Tuple<String, Integer>> parenthood) {
        this.parenthood = parenthood;
    }

    public HashMap<String, Tuple<String, Integer>> getVariables() {
        return variables;
    }

    public void setVariables(HashMap<String, Tuple<String, Integer>> variables) {
        this.variables = variables;
    }

    public long getSerialversionuid() {
        return serialVersionUID;
    }

    public HashMap<String, ArrayList<IHistory>> getHistory() {
        return history;
    }

    public void setHistory(HashMap<String, ArrayList<IHistory>> history) {
        this.history = history;
    }

    public HashMap<String, IValue> getStore() {
        return store;
    }

    public void setStore(HashMap<String, IValue> store) {
        this.store = store;
    }

    public HashMap<String, Channel> getChans() {
        return chans;
    }

    public void setChans(HashMap<String, Channel> chans) {
        this.chans = chans;
    }

    public HashMap<String, Procedure> getProcs() {
        return procs;
    }

    public void setProcs(HashMap<String, Procedure> procs) {
        this.procs = procs;
    }

    public HashMap<String, IStatement> getThreadlist() {
        return threadlist;
    }

    public void setThreadlist(HashMap<String, IStatement> threadlist) {
        this.threadlist = threadlist;
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

}
