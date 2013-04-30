package language.util;

import java.util.ArrayList;
import java.util.HashMap;

import language.history.IHistory;
import language.statement.IStatement;
import language.value.IValue;

public class DumpedConfiguration {

	private HashMap<String, IValue> store;
	private HashMap<String, Channel> chans ;
	//procedure store
	private HashMap<String,IValue> procs ;
	//thread pool
	private HashMap<String,IStatement> threadlist ;
	private HashMap<String , ArrayList<IHistory>> history ;

	
	public DumpedConfiguration(HashMap<String, IValue> store,
			HashMap<String, Channel> chans, HashMap<String, IValue> procs,
			HashMap<String, IStatement> threadlist,
			HashMap<String , ArrayList<IHistory>> history) {
		super();
		this.store = store;
		this.chans = chans;
		this.procs = procs;
		this.threadlist = threadlist;
		this.history=history;
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

	public HashMap<String, IValue> getProcs() {
		return procs;
	}

	public void setProcs(HashMap<String, IValue> procs) {
		this.procs = procs;
	}

	public HashMap<String, IStatement> getThreadlist() {
		return threadlist;
	}

	public void setThreadlist(HashMap<String, IStatement> threadlist) {
		this.threadlist = threadlist;
	}
	
	
	
}
