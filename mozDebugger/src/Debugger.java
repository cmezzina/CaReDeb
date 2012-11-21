import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import language.history.HistoryEsc;
import language.history.HistoryIf;
import language.history.HistoryInvoke;
import language.history.HistoryPort;
import language.history.HistoryProc;
import language.history.HistoryReceive;
import language.history.HistorySend;
import language.history.HistorySkip;
import language.history.HistoryThread;
import language.history.HistoryType;
import language.history.HistoryVar;
import language.history.IHistory;
import language.statement.Assignment;
import language.statement.Conditional;
import language.statement.Esc;
import language.statement.IStatement;
import language.statement.Invoke;
import language.statement.Nil;
import language.statement.Send;
import language.statement.Sequence;
import language.statement.Skip;
import language.statement.StatementType;
import language.statement.ThreadStm;
import language.util.Channel;
import language.util.Tuple;
import language.value.BoolValue;
import language.value.IValue;
import language.value.PortCreation;
import language.value.Procedure;
import language.value.Receive;
import language.value.SimpleId;
import language.value.ValueType;
import parser.ParseException;
import parser.mozParser;
import expection.ChildMissingException;
import expection.WrongElementChannel;
public class Debugger {

	static String path="C:\\Users\\aa\\workspace\\mozDebugger\\src\\pgm.txt";
	
	/* counters */
	static int chan_count =0;
	static int proc_count =0;
	static int thread_count =0;
	static int var_count=0;
	static int pc=0;
	
	static IStatement program;
	static String last_com="";
	
	//stores
	static HashMap<String, IValue> store= new HashMap<String, IValue>();
	static HashMap<String, Channel> chans = new HashMap<String, Channel>();
	static HashMap<String,IValue> procs = new HashMap<String,IValue>();
	static HashMap<String,IStatement> threadlist = new HashMap<String, IStatement>();
	
	static HashMap<String , ArrayList<IHistory>> history = new HashMap<String, ArrayList<IHistory>>();
	
	/***prompt messages ***/
	static String warning="\n++";
	static String error="\n**";
	static String done = "...done";
	
	public static void main(String arg[])
	{
		BufferedReader cons = new BufferedReader(new InputStreamReader(System.in));

		if(arg.length >0)
			path = arg[0];
		System.out.println("reading  file  ... "+path);
		try {
			program = mozParser.parse(new FileInputStream(path));
			//program represents the first configuration
			
			String initial = generateThreadId();
			threadlist.put(initial, program);
			history.put(initial, new ArrayList<IHistory>());
			System.out.println("generated initial configuration "+ initial +"\n\n");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(warning+" type help to see all the commands \n\n");

				
		String command;
		try {
				
			while( true)
			{
				System.out.print("Insert command : ");
				command = cons.readLine();

				//enter is a shortcut to redo the same command
				
				if(command.equals("") && !last_com.equals(""))
					command = last_com;
				
				last_com = command;
				String[] cmd = command.split(" ");
				if(cmd[0].equals("quit"))
				{
					System.out.println("*** quitting debugging");
					return;
				}

				if(cmd[0].equals("help") || cmd[0].equals("c"))
				{
					showHelp();
				}
				else
					if(cmd[0].equals("store") || cmd[0].equals("s"))
					{
						if(store.size() == 0)
						{
							System.out.println("The store is empty");
						}
						else
							System.out.println("Stored ids :"+store.keySet());
					
					}
				else 
					if(cmd[0].equals("print") || cmd[0].equals("p"))
					{
						
						if(threadlist.containsKey(cmd[1]))
						{
							System.out.println(threadlist.get(cmd[1]));
							continue;
						}
						String toprint = printId(cmd[1]);
						if(toprint!=null)
						{
							System.out.println(cmd[1]+ " = "+toprint);
						}
					}
					else
						if(cmd[0].equals("list") || cmd[0].equals("l"))
						{

							System.out.println("Available threads : "+threadlist.keySet());
						}
					else
						if(cmd.length >1 && ( cmd[0].equals("forth") || cmd[0].equals("f")))
						{
							IStatement body = threadlist.get(cmd[1]);
							if(body != null)
							{
								if(body.getType() != StatementType.NIL )
								{
									body = execute(body, cmd[1]);
									if(body == null)
									{
										break;
									}
									threadlist.put(cmd[1], body);
								}
								else
								{
									System.out.println("thread "+cmd[1] + " has terminated");
								}
							}
							else
							{
								System.out.println(error+"invalid thread name "+ cmd[1]+ "\n");
							}
						}
						else if(cmd.length>1 && (cmd[0].equals("back") || cmd[0].equals("b")))
						{
							if(!threadlist.containsKey(cmd[1]))
							{
								System.out.println(warning +"invalid thread identifier "+cmd[1]);
								continue;
							}
							
							if(!history.containsKey(cmd[1]))
							{
								System.out.println(warning +"invalid memory for thread "+cmd[1]);
								continue;
							}
								try {
									if(cmd.length == 2)
										stepBack(cmd[1]);
									else rollNsteps(cmd[1], Integer.parseInt(cmd[2]));
									System.out.println(done);

								} catch (WrongElementChannel e) {
									System.out.println(warning+ e.getMsg());
								} catch (ChildMissingException e) {
									// TODO Auto-generated catch block
									System.out.println(warning+ e.getMsg());								}
						}
						else if(cmd.length >1 && (cmd[0].equals("roll") || cmd[0].equals("r")))
						{
							rollEnd(cmd[1]);
						}
						else if(cmd.length >1 && (cmd[0].equals("story") || cmd[0].equals("h")))
						{
							printHistory(cmd[1]);
						}
						else
						{
							last_com = "";
							System.out.println(error+"invalid command "+ cmd[0]+ "\n");
						}
						
			}

			//System.out.println("..............");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("exiting from debugging ");


	}

	//forward execution
	private static  IStatement execute(IStatement stm, String thread_name)
	{
		StatementType type = stm.getType();
		ArrayList<IHistory> h = history.get(thread_name);

		switch(type)
		{
			case SEQUENCE: 
			{
				Sequence seq = (Sequence)stm;
				
		
				//avoiding executing NIL 
				IStatement sx= execute(seq.getSx(), thread_name);
				//error --> quit
				if(sx == null)
					return null;
				
				if(sx.getType() == StatementType.NIL)
				{
					//	h.add(new HistoryNil());
				//		history.put(thread_name, h);
						//return execute(seq.getDx(), thread_name);
					if(seq.getDx().getType() == StatementType.ESC)
							return execute(seq.getDx(), thread_name);
					
					return seq.getDx();
				}
				seq.setSx(sx);
				//should log this stuff
				return seq;
			}
			case SPAWN:
			{
				ThreadStm th = (ThreadStm)stm;
	
				String tid = generateThreadId();
				threadlist.put(tid, th.getBody());
				history.put(tid, new ArrayList<IHistory>());
				h.add(new HistoryThread(tid));
				history.put(thread_name, h);
				System.out.println("Generating thread "+ tid);
				return new Nil();
			}
			case LET: 
			{
				//should return a sequence with a trailing ESC
				
				Assignment let = (Assignment)stm;
				IValue val = let.getV();
				String old_id = let.getId();
				
				//renaming variable
				String new_id = generateVarId(old_id);
				
				ValueType valuetype = val.getType();
				switch(valuetype)
				{
					case RECEIVE:
					{
						Receive rec = (Receive) val;
						if(store.containsKey(rec.getFrom()))
						{
							String from = rec.getFrom();
							IValue chan=store.get(from);
							if(chan != null)
							{
								//consuming message
								//this value should be saved 
								String chanid = ((SimpleId)chan).getId();
							//	ArrayList<IValue> lst = chans.get(chanid);
								Channel ch = chans.get(chanid);
								if(ch.isEmpty())
								{
									System.out.println("channel empty ");
									return stm;
								}
								System.out.println("receiving from "+from +" in "+new_id);
								int gamma= pc++;
								IValue received =ch.receive(thread_name, gamma);
	//							chans.put(chanid,  lst);
								store.put(new_id, received);
								h.add(new HistoryReceive(from, new_id,gamma));
	
							}
						}
						break;
					}
					case BOOLEAN:
							{
								System.out.println("putting in store variable "+new_id);
								store.put(new_id, val);
								h.add(new HistoryVar(new_id));
								break;
							}
					case PORT:
							{
								String xi = generateChanId();
								System.out.println("generating channel "+ new_id +" --> "+xi);
								store.put(new_id, new SimpleId(xi));
								chans.put(xi, new Channel());
								h.add(new HistoryPort(new_id));
								break;
							}
					case PROCEDURE:
							{
								//Procedure prc = (Procedure)val;
								String lambda = generateProcId();
								System.out.println("generating procedure "+ new_id +" --> "+lambda);
								val.rename(old_id, new_id);
								store.put(new_id, new SimpleId(lambda));
								procs.put(lambda, val);
								h.add(new HistoryProc(new_id));
								break;
							}
					//			execute(let.getStm());
				}
				//renaming the continuation
				let.getStm().rename(old_id, new_id);
			
				//logging the action
				history.put(thread_name, h);
				
				//putting a trailing ESC to delimit the let scope
				return new Sequence(let.getStm(), new Esc());			
			}
			case IF:
			{
				Conditional cond = (Conditional) stm;
				String guard = cond.getGuard();
				IValue val = store.get(guard);
				IStatement ret =null;
				if(val != null && val.getType() == ValueType.BOOLEAN)
				{
					BoolValue e = (BoolValue)val;
					if(e.getValue())
					{
						System.out.println("reducing to then (left) branch");
						h.add(new HistoryIf(guard, cond.getRight(), true));
						ret= cond.getLeft();
					}
					else
					{
						System.out.println("reducing to else (right) branch");
						h.add(new HistoryIf(guard, cond.getLeft(), false));
						ret =cond.getRight();
					}
				}
				else
				{
					if(val == null)
					{
						System.out.println(error+" undefined variable "+guard);
					}
					else
					{
						System.out.println(error+" non boolean value for "+guard);
					}
					return null;
				}
				history.put(thread_name, h);
				return new Sequence(ret,new Esc());
			}
			case SEND:
			{
				Send snd = (Send)stm;
				String to = snd.getObj();
				IValue chan = store.get(to);
				if(chan == null)
				{
					System.out.println(to + " is not recognized as channel");
					return null;
				}
				if(chan.getType()== ValueType.ID)
				{
					String id = ((SimpleId)chan).getId();
					//chans.put(id, chans.get(id).add(e))
	
					//IValue tosend = store.get(snd.getSub());
					IValue tosend = new SimpleId(snd.getSub());
					int gamma=pc++;
					Channel tmp = chans.get(id);
					tmp.send(tosend, thread_name,gamma);
					//chans.put(id, tmp );
					System.out.println("sending to channel "+to);
					h.add(new HistorySend(to,gamma));
					history.put(thread_name, h);
					return new Nil();
				}	
				else
				{
					System.out.println("error "+to +"is not a channel type");
	
					return null; 
				}
			}
			case INVOKE:
			{
				Invoke call = (Invoke)stm;
				String call_id = call.getCallee();
				SimpleId real_name = (SimpleId) store.get(call_id);
				if(real_name != null && real_name.getType() == ValueType.ID)
				{
					System.out.println("should execute procedure "+ real_name.getId());
					Procedure proc_def = (Procedure) procs.get(real_name.getId());
					
					h.add(new HistoryInvoke(call_id, call.getParams()));
					history.put(thread_name, h);
					if(call.getParams().isEmpty())
					{
						return new Sequence(proc_def.getBody(), new Esc());
					}
					else
					{
						List<String> param = proc_def.getParams();
						List<String> actual_param = call.getParams();
						if(param.size() == actual_param.size())
						{
							IStatement body= proc_def.getBody().clone();							
							for(int i=0; i < param.size(); i++)
							{
								body.rename(param.get(i), actual_param.get(i));
								
								//should clone the body I guess
							
							}
							return new Sequence(body, new Esc());
						}
						else
						{
							System.out.println(warning + " size mismatch on invocation of procedure "+ call_id );
						}
					}
				}
				
				break;
			}
			case SKIP:
			{
				System.out.println("skip");
				h.add(new HistorySkip());
				history.put(thread_name, h);
				return new Nil();
			}
			case ESC:
			{
			//	System.out.println("esc");
				h.add(new HistoryEsc());
				history.put(thread_name, h);
				return new Nil();
			}
			
			case NIL:	return stm;
		
		}
			return null;
	}
	
	private  static int stepBack(String thread_id) throws WrongElementChannel, ChildMissingException
	{
		ArrayList<IHistory> lst; 
		IStatement body = threadlist.get(thread_id);
		int ret =0;
		//thread next action after a step backward
		IStatement new_body = null;
		//the rest of the body
		IStatement next=null ;
		
		//checks about thread id and history are done in the callee
		lst = history.get(thread_id);
		
		if(lst.size() == 0)
		{
			System.out.println(warning + "empty history for thread "+thread_id);
			return ret;
		}
		
		int index = lst.size()-1;
		IHistory action = lst.get(index);
		
		switch(action.getType())
		{
			case SKIP: {
				if(body.getType() == StatementType.NIL)
					new_body = new Skip();
				else{
					new_body = new Sequence(new Skip(), body);
				}
				break;
			}
			case IF :{
				HistoryIf log = (HistoryIf)action;
				if(log.isLeft())
				{
					next = afterEsc(body);
					new_body = beforeEsc(body);
					new_body = new Conditional(log.getGuard(), new_body, log.getBody());
/*					if(next == null)
						new_body = new Conditional(log.getGuard(), new_body, log.getBody());
					else
						new_body = new Sequence(new Conditional(log.getGuard(), new_body, log.getBody()), new Esc());
				*/
				}
				break;
			}
			case VAR:
			{
				//var creation of a boolean value
				HistoryVar log = (HistoryVar)action;
				
				if(store.containsKey(log.getId()))
				{
					IValue val =store.remove(log.getId());
					
					//probably this check is useless
					if(body.getType() == StatementType.SEQUENCE)
					{
							next = afterEsc(body);
							new_body = beforeEsc(body);
							new_body = new Assignment(log.getId(), val, new_body);
						
					}
				/*	if(next != null)
						new_body = new Sequence(new  Assignment(log.getId(), val, new_body),next);
					else
						new_body = new Assignment(log.getId(), val, new_body);
				*/
				}
				break;
			}
			case PROCEDURE:
			{
				HistoryProc log = (HistoryProc)action;
				if(store.containsKey(log.getId()))
				{
					IValue val =store.remove(log.getId());
					if(val.getType() == ValueType.ID)
					{
						String xi = ((SimpleId)val).getId();
						IValue proc = null;
						if( (proc = procs.get(xi)) != null)
						{
							next = afterEsc(body);
							new_body = beforeEsc(body);
							new_body = new  Assignment(log.getId(), proc, new_body);
							//removing from store variale and procedure
							store.remove(log.getId());
							procs.remove(xi);
						}
					}
					
				}
				break;
				
				
			}
			case PORT:
			{
				HistoryPort log = (HistoryPort)action;
				//to reverse a port creation the port should be empty
				if(store.containsKey(log.getPort_name()))
				{
					IValue val =store.remove(log.getPort_name());
					if(val.getType() == ValueType.ID)
					{
						String xi = ((SimpleId)val).getId();
						Channel chan = null;
						if( (chan = chans.get(xi)) != null)
						{
							if(chan.isEmpty())
							{

								next = afterEsc(body);
								new_body = beforeEsc(body);
							
								new_body = new  Assignment(log.getPort_name(), new PortCreation(), new_body);
							//removing from store variable and procedure
								store.remove(log.getPort_name());
								procs.remove(xi);
							
							}
							else
							{
								System.out.println(warning +" cannot revert port creation of "+log.getPort_name() +" since it is not empty");
								return ret;
							}
						}
						
					}
					
				}
				
				
				break;
				
			}
			case THREAD:{

				HistoryThread log = (HistoryThread)action;
				//to reverse a port creation the port should be empty
				String xi = log.getThread_id();
				if(threadlist.containsKey(xi))
				{
					ArrayList<IHistory> child_story = null;

					if( (child_story = history.get(xi)) != null)
					{
						if(child_story.size() == 0)
						{
							IStatement thread_body = threadlist.get(xi);
							if(thread_body == null)
							{
								//this cannot happen
							}
							new_body = new ThreadStm(thread_body);
							next = body;
							//removing from store variable and procedure
							threadlist.remove(xi);
							history.remove(xi);
						}
						else
						{
							throw new ChildMissingException(" cannot revert thread creation of "+log.getThread_id() +" since it has not empty history", log.getThread_id());
//							System.out.println(warning +" cannot revert thread creation of "+log.getThread_id() +" since it has not empty history");
//							return;
						}
					}
				}
				break;
			}
			case SEND:{
				HistorySend log = (HistorySend)action;
				String id = log.getChan();
				ret = log.getInstruction();
				SimpleId tmp = (SimpleId) store.get(id);
				Channel ch = chans.get(tmp.getId());
				if(ch !=null)
				{
					if(ch.isEmpty())
						//different kind of exception ... should reverse who read the msg ..
						throw new WrongElementChannel(" value on channel "+tmp.getId() +" does not belong to thread "+thread_id, ch.getReaders(thread_id));
					IValue val =ch.reverseSend(thread_id);
					if(val == null)
					{
						//System.out.println(ch.beforeThread(thread_id));
						throw new WrongElementChannel(" value on channel "+tmp.getId() +" does not belong to thread "+thread_id, ch.getSenders(thread_id));
						//System.out.println(warning +"value on channel "+tmp.getId() +" does not belong to thread "+thread_id);
						//return;
					}
					//next = afterEsc(body);
					//new_body = beforeEsc(body);
				
					if(val.getType() == ValueType.ID){
						new_body = new  Send(log.getChan(), ((SimpleId)val).getId());
						next=body;
					}
				}
				break;
			}
			case RECEIVE:
			{
				HistoryReceive log = (HistoryReceive)action;
				String id = log.getFrom();
				ret = log.getInstruction();

				//lookup
				String xi = ((SimpleId)store.get(id)).getId();
				Channel ch = chans.get(xi);
				//putting back msg
				ch.reverseReceive(thread_id);
				
				//receive is an assigment
				next = afterEsc(body);
				new_body = beforeEsc(body);
				new_body = new  Assignment(log.getVar(), new Receive(log.getFrom()), new_body);

				//erasing variables from store
				store.remove(log.getClass());
				
				break;
			}
			case INVOKE:{
				HistoryInvoke log = (HistoryInvoke)action;
				
//				String xi = log.getProc_name();
				next= afterEsc(body);
				new_body = new Invoke(log.getProc_name(), log.getParam());
				break;
			}
			case ESC:
			{
				new_body = new Esc();
				next = body;
				
				if(next != null && next.getType() != StatementType.NIL)
					new_body = new Sequence(new_body,next);
				
				lst.remove(index);
				history.put(thread_id, lst);
				threadlist.put(thread_id, new_body);
				 return stepBack(thread_id);
				 //return ret;
			}
		}
	
		if(next != null && next.getType() != StatementType.NIL)
			new_body = new Sequence(new_body,next);
		
		lst.remove(index);
		history.put(thread_id, lst);
		threadlist.put(thread_id, new_body);
		return ret;
	}

	
	
	//rollbacks a thread till a given action
	private static void rollTill(HashMap<String, Integer> map)
	{
		Iterator<String> it =  map.keySet().iterator();
		while(it.hasNext())
		{
			String id = it.next();
			int gamma = map.get(id);
			
			while(true)
			{
				try {
					int nro = stepBack(id);
					System.out.println("... reversing thread "+id +" of one step");
					if(nro == gamma)
						break;
				} catch (WrongElementChannel e) {
					rollTill(e.getDependencies());
				} catch (ChildMissingException e) {
					// TODO Auto-generated catch block
					rollEnd(e.getChild());
				}
				
			}
		
		}
	}
	
	//rollbacks a thread to the beginning causing the failure of its caused actions
	private static void rollEnd(String thread_id)
	{
		if(!threadlist.containsKey(thread_id))
		{
			System.out.println(warning + " invalid thread id "+thread_id);
			return;
		}
		while(history.get(thread_id).size() != 0)
		{
			try {
				stepBack(thread_id);
	//			System.out.println(nro);
			} catch (WrongElementChannel e) {
			
				System.out.println("roll till");
				rollTill(e.getDependencies());
				}
			 catch (ChildMissingException e) {
				// TODO Auto-generated catch block
				System.out.println(warning +" reversing child thread "+e.getChild());
				rollEnd(e.getChild());
			}
		}
	}
	
	private static void rollNsteps(String thread_id, int steps)
	{
		if(!threadlist.containsKey(thread_id))
		{
			System.out.println(warning + " invalid thread id "+thread_id);
			return;
		}
	
		while(history.get(thread_id).size() > 0 && steps >0)
		{
			try {
				stepBack(thread_id);
				steps--;
			//	System.out.println(nro);
			} catch (WrongElementChannel e) {
			
				System.out.println("roll till");
				rollTill(e.getDependencies());
				}
			 catch (ChildMissingException e) {
				// TODO Auto-generated catch block
				System.out.println(warning +" reversing child thread "+e.getChild());
				rollEnd(e.getChild());
			}
		}
	}
	
	private static String generateChanId()
	{
		return "chan_"+(chan_count++);

	}
	private static String generateProcId()
	{
		String ret= null;
		
		while(true)
		{
			ret="proc_"+(proc_count++);
			if(!store.containsKey(ret));
				break;
		}
		return ret;

	}
	
	private static String generateThreadId()
	{
		String ret= null;
		
		while(true)
		{
			ret="t_"+(thread_count++);
			if(!store.containsKey(ret));
				break;
		}
		return ret;

	}
	private static String generateVarId(String id)
	{
		String ret;
		
		while(true)
		{
			ret = id+var_count++;
			if(!store.containsKey(ret))
				break;
		}
		return ret;
	}

	private static void showHelp()
	{
		System.out.println("\nCommands : \n\t forth (f) thread_name (executes forward one step of thread_name)");
		System.out.println("\t back (b) [n] thread_name (executes backward one step of thread_name) [of n steps]");
		System.out.println("\t roll thread_name (rollsback a thread at its starting point)");
		System.out.println("\t list (l) (displays all the available threads)");
		System.out.println("\t print (p) id (shows the state of a thread, channel, or variable)");
		System.out.println("\t story (h) thread_id (shows thread computational history)");
		System.out.println("\t store  (s) (displays all the ids contained in the store)");
		System.out.println("\t help  (c) (displays all commands)");
		System.out.println("\t quit (q)\n");
	}

	/*prints the status of an id, including threads*/
	private static String printId(String id)
	{
		IValue val = store.get(id);
		ValueType type;
		if(val!=null)
		{
			type = val.getType();
			switch(type)
			{
				case BOOLEAN:
					//System.out.println(id + " = "+val);
					return val.toString();
				
				case ID:
					//if ID it can be either a channel or a procedure or a variable
					String xi = ((SimpleId)val).getId();
					Channel chan = chans.get(xi);
					//channel
					if(chan !=null)
						//System.out.println(id +" = " + printChan(chan.getValues()));
						return printChan(chan.getValues());
					else
					{
						//procedure
						IValue proc = procs.get(xi);
						if(proc != null)
							return proc.toString();
						else
						{
							return printId(xi);
						}
					}
						
			}
		}
		else
		{
			System.out.println("\n**invalid identifier: "+ id);
		}
		return null;
		
		
	}
	
	
	private static String printChan(List<Tuple<IValue,String>> queue)
	{
		String ret= " [";
		for (Tuple<IValue,String> ith : queue) {
			ValueType type = ith.getFirst().getType();
			if(type == ValueType.ID)
			{
				String xi = ((SimpleId)ith.getFirst()).getId();
				//variable
				if(store.get(xi)!= null)
				{
					ret+= " (" + store.get(xi).toString()+" , "+ith.getSecond() +")";
					continue;
				}
				List<Tuple<IValue,String>> lst = chans.get(xi).getValues();
				if(lst != null)
					ret += printChan(lst)+" ";
			}
			else
				ret+= " (" +ith.getFirst().toString() + " , "+ith.getSecond()+")";
		}
//		ret= ret.substring(0, ret.length());
		return ret+=" ]";
		
	}
	
	static void printHistory(String id)
	{
		ArrayList<IHistory> h = history.get(id);
		if(h == null)
		{	
			System.out.println(warning + " no history for thread "+ id);
			return;
		}
		
		if(h.size() == 0)
		{
			System.out.println(warning +" empty history for thread "+id);
		}
		for (IHistory log : h) {
			if(log.getType() != HistoryType.ESC)
				System.out.println(log.toString());
			
			
		}
	}
	
	/*static void printChanHistory(String chan_id)
	{
		Channel ch = chans.get(chan_id);
		if(ch == null)
		{	
			System.out.println(warning + " no history for channel "+ chan_id);
			return;
		}
		
		if(ch.chanSenderStory() == null)
		{
			System.out.println(warning +" empty history for thread "+chan_id);
		}

		ArrayList<IValue> val = ch.chanValueStory();
		ArrayList<String> ids = ch.chanSenderStory();
		int size = val.size()-1;
		for(int i=size; i >=0; i--)
		{
			System.out.println("("+ids.get(i)+" , "+ val.get(i)+")");
		}
		
	}*/
	
	//there should be a better recursive implementation of this
	
	//return the rest of a statement sequence after the FIRST ESC
	private static IStatement afterEsc(IStatement stm)
	{
		ArrayList<IStatement> queue = new ArrayList<IStatement>();
		queue.add(stm);
		while(true)
		{
			IStatement el = queue.remove(0);
			//exits the loop as soon as the first esc has been found
			if(el.getType() == StatementType.ESC)
				break;
			if(el.getType()== StatementType.SEQUENCE)
			{
				Sequence seq = (Sequence) el;
				queue.add(0,seq.getDx());
				queue.add(0,seq.getSx());
			}
		}
		return build(queue);
	}

	//return the all the construct before the FIRST ESC

	private static IStatement beforeEsc(IStatement stm)
	{
		ArrayList<IStatement> queue = new ArrayList<IStatement>();
		ArrayList<IStatement> rest = new ArrayList<IStatement>();
		queue.add(stm);
		
		while(true)
		{
			IStatement el = queue.remove(0);
			//exits the loop as soon as the first esc has been found
			if(el.getType() == StatementType.ESC)
				break;
			if(el.getType() != StatementType.SEQUENCE)
				rest.add(el);
			if(el.getType()== StatementType.SEQUENCE)
			{
				Sequence seq = (Sequence) el;
				queue.add(0,seq.getDx());
				queue.add(0,seq.getSx());
			}
		}
/*		System.out.println(queue);
		System.out.println("-->"+ build(queue));
		*/
		return build(rest);
	}
	
	//bulds back a stametent (simple of sequence) from a  list
	private static IStatement build(ArrayList<IStatement> queue)
	{
		if(queue.size() == 0)
			return null;
		if(queue.size() == 1)
			return queue.remove(0);
		else return new Sequence (queue.remove(0), build(queue));
			
	}
}
