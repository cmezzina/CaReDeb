
/*******************************************************************************
 * Copyright (c) 2012 Claudio Antares Mezzina.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Claudio Antares Mezzina - initial API and implementation
 *     Ivan Lanese - implementation
 *     Elena Giachino - implementation
 ******************************************************************************/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import language.history.HistoryAssert;
import language.history.HistoryBreak;
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
import language.statement.Assert;
import language.statement.Assignment;
import language.statement.BreakStatemet;
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
import language.util.DumpedConfiguration;
import language.util.Monitor;
import language.util.Tuple;
import language.value.BinaryIntExp;
import language.value.BoolExpr;
import language.value.BoolValue;
import language.value.DivValue;
import language.value.IValue;
import language.value.IntConst;
import language.value.IntExp;
import language.value.IntID;
import language.value.MulValue;
import language.value.PortCreation;
import language.value.Procedure;
import language.value.Receive;
import language.value.SimpleId;
import language.value.SubValue;
import language.value.SumValue;
import language.value.ValueType;
import parser.ParseException;
import parser.mozParser;
import expection.AssertionException;
import expection.BreakPointException;
import expection.ChildMissingException;
import expection.WrongElementChannel;
public class Debugger {

	static String path="";
	
	/* counters */
	static int chan_count =0;
	static int proc_count =0;
	static int thread_count =0;
	static int var_count=0;
	static int pc=1;
	
	static IStatement program;
	static String last_com="";

	//should be cloned I guess
	static DumpedConfiguration dump ;
	static Monitor monitor;
	
	
	/*stores*/
	//variables store
	static HashMap<String, IValue> store= new HashMap<String, IValue>();
	//mapping id -> not evaluated expressions
	static HashMap<String, IValue> expressions = new HashMap<String, IValue>();
	//channel/port store
	static HashMap<String, Channel> chans = new HashMap<String, Channel>();
	//procedure store
	static HashMap<String,IValue> procs = new HashMap<String,IValue>();
	//thread pool
	static HashMap<String,IStatement> threadlist = new HashMap<String, IStatement>();
	static HashMap<String, Integer> thread_child = new HashMap<String, Integer>();
	static HashMap<String, Integer> thread_chan = new HashMap<String, Integer>();
	
	static HashMap<String , ArrayList<IHistory>> history = new HashMap<String, ArrayList<IHistory>>();
	
	//mapping child t_id -- (father t_id -- gamma)
	static HashMap<String,Tuple<String,Integer>> parenthood = new HashMap<String, Tuple<String, Integer>>();
	
	static HashMap<String,Tuple<String,Integer>> variables = new HashMap<String, Tuple<String, Integer>>();
	
	
	/** DEBUGGING PAMATERES**/
	static boolean NO_MEMORY = false;
	
	/*** prompt messages ***/
	static String warning="+++";
	static String error="\n***";
	static String done = "...done";
	
	public static void main(String arg[])
	{
		BufferedReader console
		= new BufferedReader(new InputStreamReader(System.in));
		
		if(arg.length >0)
			path = arg[0];
		else
		{
			System.out.println(error + " missing program_file parameter");
			return;
		}
	/*	if(arg.length >1)
		{
			boolean flag = Boolean.valueOf(arg[1]);
			NO_MEMORY = flag;
			Channel.NO_MEMORY = flag;
		}*/
		try {
			File f = new File(path);
			if(!f.exists())
			{
				System.out.println(error + " undefined file "+ path);
				return;
			}
			System.out.println("reading  file  ... "+path);
				program = mozParser.parse(new FileInputStream(path));
			
				//program represents the first configuration
			
			String initial = "t_0";
			thread_child.put(initial, 0);
			threadlist.put(initial, program);
			history.put(initial, new ArrayList<IHistory>());
			System.out.println("generated initial configuration "+ initial +"\n\n");
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			return;
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}
		System.out.println(warning+" type help to see all the commands \n\n");

				
		String command;
		try {
			while( true)
			{
//				System.out.println( "consumed memory : " + (float)(Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory())/1024);

				System.out.print("Insert command : ");
				command = console.readLine();
				
				if(command.equals("") && !last_com.equals(""))
					command = last_com;
				
				last_com = command;
				String[] cmd = command.split(" ");
				if(cmd[0].equals("quit") || cmd[0].equals("q"))
				{
					System.out.println(error+"quitting debugging");
					return;
				}
				
				if(cmd[0].equals("run"))
				{
					run();
					continue;
				}
				
				if(cmd[0].equals("replay"))
				{
					try {
						replay();
					} catch (BreakPointException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (AssertionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(cmd[0].equals("help") || cmd[0].equals("c"))
				{
					showHelp();
					continue;
				}
			
				if(cmd[0].equals("store") || cmd[0].equals("s"))
				{ 
					if(store.size() == 0)
					{
						System.out.println(warning +"empty store\n");
						System.out.println();
					}
					else
						System.out.println("Stored ids :"+store.keySet());		
					continue;
				}
				else 
					if (cmd[0].equals("monitor") || cmd[0].equals("m"))
				{
					init_monitor();
					System.out.println(monitor);
				}
				else
					if(cmd[0].equals("list") || cmd[0].equals("l"))
					{
					//	System.out.println("Available threads : "+threadlist.keySet());
						printThreads();
						continue;
					}
				if(cmd.length <2)
				{
					System.out.println(warning+"missing parameters for command "+cmd[0]+"\n");
					continue;

				}
				if (cmd[0].equals("back") ||   cmd[0].equals("roll") || cmd[0].equals("forth") ||cmd[0].equals("f") ||
						cmd[0].equals("b") ||  cmd[0].equals("r") )
				{
					if(cmd.length > 2 && !threadlist.containsKey(cmd[1]))
					{
						System.out.println(warning+"invilid thread identifier "+ cmd[1]+"\n");
						System.out.println();
						continue;
					}
					
				}
				
				if(cmd[0].equals("dump") || cmd[0].equals("d"))
				{
					dump(cmd[1]);
					System.out.println(".... configuration dumped in " +cmd[1]+"\n");
					continue;
				}
				if(cmd[0].equals("restore"))
				{
					String filename = cmd[1];
					
					 FileInputStream fileIn = new FileInputStream("./"+filename);
			         ObjectInputStream in = new ObjectInputStream(fileIn);
			         try {
						dump = (DumpedConfiguration) in.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			         in.close();
			         fileIn.close();
					history = dump.getHistory();
					procs= dump.getProcs();
					chans = dump.getChans();
					store = dump.getStore();
					threadlist = dump.getThreadlist();
					expressions = dump.getExpressions();
					thread_child = dump.getThread_child();
					thread_chan = dump.getThread_chan();
					parenthood = dump.getParenthood();
					variables = dump.getVariables();
					pc = dump.getPc(); 
					System.out.println(".... restored configuration " + filename +"\n");
					continue;
				}
				 
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
						if( (cmd[0].equals("forth") || cmd[0].equals("f")) )
						{
							IStatement body = threadlist.get(cmd[1]);
							if(body != null)
							{
								if(body.getType() != StatementType.NIL )
								{
									try {
										body = execute(body, cmd[1]);
										
									} catch (BreakPointException e) {
										// TODO Auto-generated catch block
										System.out.println(e.getMsg());
										body = e.getStm();
									}catch (AssertionException e) {
										// TODO Auto-generated catch block
										System.out.println(e.getMsg());
										body = e.getStm();
									}
									if(body == null)
									{
										//should not be possible to reach this point ...
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
						else if(cmd[0].equals("back") || cmd[0].equals("b"))
						{
							/*if(!threadlist.containsKey(cmd[1]))
							{
								System.out.println(warning +"invalid thread identifier "+cmd[1]);
								continue;
							}*/
							
							if(!threadlist.containsKey(cmd[1]))
							{
								System.out.println(warning +"invalid thread_id "+cmd[1]+"\n");
								continue;
							}
							if(!history.containsKey(cmd[1]))
							{
								System.out.println(warning +"invalid memory for thread "+cmd[1]+"\n");
								continue;
							}
								try {
									if(stepBack(cmd[1])>= 0)
										System.out.println(done);

								} catch (WrongElementChannel e) {
									System.out.println(warning+ e.getMsg());
								} catch (ChildMissingException e) {
									System.out.println(warning+ e.getMsg());								
								}
						}
						
					
		/*				else if(cmd[0].equals("undo") || cmd[0].equals("u"))
						{
							try{
								if(!threadlist.containsKey(cmd[1]))
								{
									System.out.println(warning + " invalid thread id "+cmd[1]+"\n");
									continue;
								}
							
								if(rollNsteps(cmd[1], Integer.parseInt(cmd[2])))
									System.out.println(done);
								else
									System.out.println(warning + "nothing to undo\n");
							}
							catch(NumberFormatException e)
							{
								System.out.println(warning + "invalid number\n");
							}
						}
		*/	
						else if(cmd[0].equals("rollsend") || cmd[0].equals("rs"))
						{
							
							int n=1;
							try{
								if(cmd.length >2)
								n = Integer.parseInt(cmd[2]);
								
								rollSend(cmd[1],n);
							}
							catch(NumberFormatException e)
							{
								System.out.println(warning + "invalid number\n");
							}

						}
						else if(cmd[0].equals("rollreceive") || cmd[0].equals("rr"))
						{
							
							int n=1;
							try{
								if(cmd.length >2)
								n = Integer.parseInt(cmd[2]);
								
								rollReceive(cmd[1],n);
							}
							catch(NumberFormatException e)
							{
								System.out.println(warning + "invalid number\n");
							}

						}
						else if(cmd[0].equals("rollthread") || cmd[0].equals("rt"))
						{
								if(!threadlist.containsKey(cmd[1]))
								{
									System.out.println(warning + " invalid thread id "+cmd[1]+"\n");
									continue;
								}
								rollThread(cmd[1]);
						}
					
						else if(cmd[0].equals("rollvariable") || cmd[0].equals("rv"))
						{
								rollLet(cmd[1]);
						}
					
						else if(cmd[0].equals("roll") || cmd[0].equals("r"))
						{
							if(!threadlist.containsKey(cmd[1]))
							{
								System.out.println(warning + " invalid thread id "+cmd[1]+"\n");
								continue;
							}
							if(cmd.length == 2)
							{
								rollEnd(cmd[1]);
								System.out.println(done);
							}
								else
							{
								try
								{
									if(rollNsteps(cmd[1], Integer.parseInt(cmd[2])))
										System.out.println(done);
									else
										System.out.println(warning + "nothing to undo\n");
								}
								catch(NumberFormatException e)
								{
									System.out.println(warning + "invalid number\n");
								}
									
							}
						}
						else if(cmd[0].equals("hystory") || cmd[0].equals("h"))
						{
							printHistory(cmd[1]);
						}
						else
						{
							last_com = "";
							System.out.println(error+"invalid command "+ cmd[0]+ "\n");
							continue;
						}
					printThreads();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("exiting from debugging ");


	}
	
	/*
	 * should re-execute following the monitor 
	 * **/
	private static void replay() throws BreakPointException, AssertionException
	{
		ArrayList<String> to_iterate = monitor.getThreadList();
		
		while(to_iterate.size() >0)
		{
			ArrayList<String> to_remove = new ArrayList<String>();
			for (String tid : to_iterate) 
			{
				if (monitor.canMove(tid) )
				{
					if (threadlist.containsKey(tid))
					{
						IStatement stm = threadlist.get(tid);
						if (stm.getType() == StatementType.LET)
						{
		
							Assignment let = (Assignment)stm;
							IValue val = let.getV();
							ValueType valuetype = val.getType();
							if(valuetype == ValueType.RECEIVE)
							{
								//we should check if it is our turn to read
								Receive rec = (Receive) val;
								String from =rec.getFrom();
								String xi = lookupChan(from);
								if (!monitor.isTurnChan(tid, xi, true))
									continue;
								Channel ch = chans.get(xi);
								//if the channel is empty there is no need to execute the read operation
								if(ch.isEmpty())
									continue;
							
								monitor.consumeAction(tid, xi, true);
		
							}
						}
						else
							if(stm.getType() == StatementType.SEND)
							{
								Send snd = (Send) stm;
								String to = snd.getObj();
								IValue chan = store.get(to);
								if(isChan(to))
								{
									String id = ((SimpleId)chan).getId();
									String xi = lookupChan(id);
									if(!monitor.isTurnChan(tid, xi, false))
										continue;
									monitor.consumeAction(tid, xi, false);
		
								}
							}
						
						IStatement body = execute(threadlist.get(tid), tid);
						if(body!=null)
						{
							monitor.move(tid);
							threadlist.put(tid, body);
							if (monitor.hasTerminated(tid)	)
								to_remove.add(tid);
						}	
					}
				}
				//the thread 
				else
				{
					to_remove.add(tid);		
				}
				
	
			}			
			to_iterate.removeAll(to_remove);
			
		}
		
	}
	
	//better check perhaps problem with ESC
	private static void init_monitor()
	{
		Iterator<String> it = threadlist.keySet().iterator();
		HashMap <String, Integer> tmp = new HashMap<String, Integer>();
		
		//number of steps for each thread
		while(it.hasNext())
		{
			String thread = it.next();
			tmp.put(thread, history.get(thread).size());
		}
		
		it = chans.keySet().iterator();
		HashMap<String, ArrayList<Tuple<String, Boolean>>> ch = new HashMap<String, ArrayList<Tuple<String,Boolean>>>();
		while(it.hasNext())
		{
			String chan_id = it.next();
			ch.put(chan_id, chans.get(chan_id).getIOAccessSequence());
			
		}
		monitor = new Monitor(tmp, ch);
	}
	
	
	
	//first attempt of scheduler 
	private static boolean allStopped()
	{
		Iterator<String> it = threadlist.keySet().iterator();
		IStatement stm=null;
		while(it.hasNext())
		{
			stm = threadlist.get(it.next());
			if(canMove(stm))
			{
				return false;
			}
		}
		return true;
	}
	
	public static void run()
	{
		while(!allStopped())
		{
			Iterator<String> it = threadlist.keySet().iterator();
			IStatement stm=null;
			String t_id = null;
			iteratorLoop:
			while(it.hasNext())
			{
				int size = threadlist.size();
				t_id = it.next();

				stm = threadlist.get(t_id);
				if(canMove(stm))
				{
					System.out.println(" executing "+t_id);

					try {
				
						stm = execute(threadlist.get(t_id), t_id);
					} catch (BreakPointException e) {
					
						stm = normalize(e.getStm(), t_id);
						System.out.println(e.getMsg()+"\n");
						threadlist.put(t_id, stm);
						return;
					}
					catch (AssertionException e) {
						
						stm = normalize(e.getStm(), t_id);
						System.out.println(warning +e.getMsg());
						threadlist.put(t_id, stm);
						return;
					}
					
					threadlist.put(t_id, stm);
					if(threadlist.size() > size)
					{
						break iteratorLoop;
					}	
					
				}
			}
			
		}
		System.out.println(warning + " all threads are terminated or blocked\n");
	}
	
	//logs and executes all the esc in a sequence at once. Stops when there is a statement different from esc
	private static IStatement normalize(IStatement stm, String thread_name)
	{
		if(stm.getType() == StatementType.ESC)
		{
			ArrayList<IHistory> h = history.get(thread_name);
			if(!NO_MEMORY)
			{
				h.add(new HistoryEsc());
				history.put(thread_name, h);
			}
				return new Nil();

		}
		
		
		if(stm.getType() == StatementType.SEQUENCE)
		{
			Sequence seq= (Sequence)stm;
			IStatement sx = normalize(seq.getSx(), thread_name);
			if(sx.getType() !=StatementType.NIL)
			{
				return new Sequence(sx, seq.getDx());
			}
			else return normalize(seq.getDx(), thread_name);
		}
		return stm;
	}

	//executes one step forward of a given thread
	private static  IStatement execute(IStatement stm, String thread_name) throws BreakPointException, AssertionException
	{
		StatementType type = stm.getType();
		ArrayList<IHistory> h = null;
		
		switch(type)
		{
			case SEQUENCE: 
			{
				Sequence seq = (Sequence)stm;

				//executing left member of the sequence
				IStatement sx= null;
				boolean rethrow = false;
				String msg = "";
				try{
					sx= execute(seq.getSx(), thread_name);
							
				}
				catch (BreakPointException e) {
					// we are sure it is a single stm break
					sx = e.getStm();
					rethrow = true;
					msg = e.getMsg();
				}
				catch (AssertionException e) {
					// we are sure it is a single stm break
					sx = e.getStm();
					rethrow = true;
					msg = e.getMsg();
				}
					//it is a sequence with on top a breakpoint
				if(sx == null)
					return null;
				
				//if the left element has finished = Nil then we have to normalize the right one
				if(sx.getType() == StatementType.NIL && !rethrow)
				{
					return normalize(seq.getDx(), thread_name);
				}
				
				if(rethrow)
				{
					if(sx.getType() == StatementType.NIL)
						sx = normalize(seq.getDx(), thread_name);
					else
					{
						seq.setSx(sx);
						sx = seq;
					}
					throw new BreakPointException(sx,msg);
				}
				seq.setSx(sx);
				return seq;
				
			}
				
			case SPAWN:
			{
				ThreadStm th = (ThreadStm)stm;
				
				String tid = generateThreadId(thread_name);
				threadlist.put(tid, th.getBody());
				if(!NO_MEMORY)
				{
					h = history.get(thread_name);
					history.put(tid, new ArrayList<IHistory>());
					int gamma= pc++;
					parenthood.put(tid, new Tuple<String, Integer>(thread_name, gamma));
					System.out.println(thread_name +" generated "+tid +" at instruction "+gamma);
					h.add(new HistoryThread(tid,gamma));
					history.put(thread_name, h);

				}
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
						String from =rec.getFrom();
						String xi = lookupChan(from);
						if(chans.containsKey(xi))
						{
							Channel ch = chans.get(xi);
							if(ch.isEmpty())
							{
								System.out.println("empty channel "+from);
								return stm;
							}
							System.out.println("receiving from "+from +" in "+new_id);
							int gamma= pc++;
							IValue received =ch.receive(thread_name, gamma);
							store.put(new_id, received);
							variables.put(new_id, new Tuple<String, Integer>(thread_name, gamma));
							if(!NO_MEMORY)
							{
								h = history.get(thread_name);
								h.add(new HistoryReceive(from, new_id,gamma));
							}
						}
						else
						{
							System.out.println(error + "unrecognized channel "+from);
							System.out.println();
							System.exit(-1);
							//return null;
						}
						break;
					}
					case BOOLEAN:
							{
								System.out.println("putting in store variable "+new_id);
								store.put(new_id, val);
								int gamma = pc++;
								variables.put(new_id, new Tuple<String, Integer>(thread_name, gamma));
								if(!NO_MEMORY)
								{
									h = history.get(thread_name);
									h.add(new HistoryVar(new_id,gamma));
								}
									break;
							}
					case PORT:
							{
								String xi = generateChanId(thread_name);
								System.out.println("generating channel "+ new_id +" --> "+xi);
								store.put(new_id, new SimpleId(xi));
								chans.put(xi, new Channel());
								if(!NO_MEMORY)
								{
									h = history.get(thread_name);
									h.add(new HistoryPort(new_id));
								}
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
								
								if(!NO_MEMORY)
								{
									h = history.get(thread_name);
									h.add(new HistoryProc(new_id));
								}
									break;
							}
					case INT_ID:
					{
						//alias we should infer the variable type
						IntID var = (IntID)val;
						IValue id = lookupVar(var.getValue());
						System.out.println(var+ "   "+ id );

						//if it is a variable
						if(id!=null)
						{
							//maybe cloning??
							store.put(new_id, id);
							System.out.println("putting in store variable "+new_id +" = "+id);
							h = history.get(thread_name);

						}
						else
						{
							String xi = lookupChan(var.getValue());
							//it is a channel
							if(xi != null && chans.containsKey(xi))
							{
								store.put(new_id, new SimpleId(xi));
								System.out.println("putting in store variable "+new_id +" = "+id);
								h = history.get(thread_name);
							}
							else
							{
							//	xi = lookupProc(var.getValue());								
								if(xi !=null && procs.containsKey(xi))
								{
									store.put(new_id, new SimpleId(xi));
									System.out.println("putting in store variable "+new_id +" = "+id);
									h = history.get(thread_name);
					
								}
								else
								{
									System.out.println("SOMETHING BAD HAPPENED IN THE EXPRESSION ....");
								}
							}
							
						}
						int gamma = pc++;
						variables.put(new_id, new Tuple<String, Integer>(thread_name, gamma));
						h.add(new HistoryVar(new_id,gamma));

						break;
					}
					case SUM:
					case MUL:
					case DIV:
					case SUB:
					case CONST:
					{
							IntExp op = (IntExp) val;
							//saving expressions but consts
							if(! isConstantExp(op))
								expressions.put(new_id, op);
							int result = evaluateExp(op);
							System.out.println("putting in store variable "+new_id +" = "+result);
							store.put(new_id, new IntConst(result));
							//TODO put something in the history
							h = history.get(thread_name);
							int gamma = pc++;
							variables.put(new_id, new Tuple<String, Integer>(thread_name, gamma));
							h.add(new HistoryVar(new_id,gamma));

					}
							//			execute(let.getStm());
				}
				//renaming the body of a let statement
				let.getStm().rename(old_id, new_id);
			
				//logging the action
				if(!NO_MEMORY)
					history.put(thread_name, h);
				
				//putting a trailing ESC to delimit the let scope
				return new Sequence(let.getStm(), new Esc());			
			}
			case ASSERT:
			{
				String guard = null;
				Assert ass = (Assert) stm;
				boolean boolguard = false;
				IStatement ret =null;
				
				if(ass.getGuard().getType() == ValueType.ID)
				{
					guard = ((SimpleId)ass.getGuard()).getId();
					
					IValue val =  lookupVar(guard);
					//here is a constant value
					System.out.println("-->"+guard +" "+ val);
					BoolValue e = (BoolValue)val;
					if(val != null && val.getType() == ValueType.BOOLEAN)
					{
						boolguard=e.getValue();
					}
					else
					{
						if(val == null)
						{
							System.out.println(error+" undefined variable "+guard);
						}
						else
						{
							//System.out.println(error+" non boolean value for "+guard);
							throw new AssertionException(stm, " non boolean value for "+guard);
						}
						return null;
					}
				}
				else
				{

					boolguard  = evaluateExp((BoolExpr)ass.getGuard());
					//it is a conditional expression
				}
				//
				if (!boolguard)
				{
					throw new AssertionException(stm, "Assertion "+stm +" FAILED!!");
					
				}
				else
				{
					//we pass the assertion and then it is like it has been executed 
					if(!NO_MEMORY)
					{
						
						h = history.get(thread_name);
						h.add(new HistoryAssert(ass.getGuard()));
						history.put(thread_name, h);

					}
					return new Nil();
				}
				
			}
			case IF:
			{
				String guard = null;
				Conditional cond = (Conditional) stm;
				boolean boolguard = false;
				IStatement ret =null;
				
				if(cond.getGuard().getType() == ValueType.ID)
				{
					guard = ((SimpleId)cond.getGuard()).getId();
					
					IValue val =  lookupVar(guard);
					//here is a constant value
				
					if(val != null && val.getType() == ValueType.BOOLEAN)
					{
						BoolValue e = (BoolValue)val;
						boolguard=e.getValue();
					}
					else
					{
						if(val == null)
						{
							System.out.println(error+" undefined variable "+guard);
						}
						else
						{
//							System.out.println(error+" non boolean value for "+guard);
							throw new BreakPointException(stm, error+" non boolean---- value for "+guard);
						}
						return null;
					}
				}
				else
				{

					boolguard  = evaluateExp((BoolExpr)cond.getGuard());
					//it is a conditional expression
				}
				
				//we should lookup it btw
					if(boolguard)
					{
						System.out.println("reducing to then (left) branch");
						if(!NO_MEMORY)
						{
							h = history.get(thread_name);
							h.add(new HistoryIf(cond.getGuard(), cond.getRight(), true));
						}
							ret= cond.getLeft();
					}
					else
					{
						System.out.println("reducing to else (right) branch");
						if(!NO_MEMORY)
						{
							h = history.get(thread_name);
							h.add(new HistoryIf(cond.getGuard(), cond.getLeft(), false));
						}
						ret =cond.getRight();
					}
				
				
				if(!NO_MEMORY)
					history.put(thread_name, h);
				return new Sequence(ret,new Esc());
			}
			case SEND:
			{
				Send snd = (Send)stm;
				String to = snd.getObj();
				IValue chan = store.get(to);
		/*		if(chan == null)
				{
					System.out.println(error+to + " is not recognized as channel");
					return null;
				}
		*/		
				if(isChan(to))
				{
					String id = ((SimpleId)chan).getId();
					String lookup = lookupChan(id);
					//chans.put(id, chans.get(id).add(e))
	
					//IValue tosend = store.get(snd.getSub());
					IValue tosend = new SimpleId(snd.getSub());
					int gamma=pc++;
					Channel tmp = chans.get(lookup);
					tmp.send(tosend, thread_name,gamma);
					//chans.put(id, tmp );
					System.out.println("sending to channel "+to);
					if(!NO_MEMORY)
					{
						h = history.get(thread_name);
						h.add(new HistorySend(to,gamma));
						history.put(thread_name, h);
					}
						return new Nil();
				}	
				else
				{
					System.out.println(error+to +" is not a channel");
					System.out.println();
					//return null;
					System.exit(-1);
				}
			}
			case INVOKE:
			{
				Invoke call = (Invoke)stm;
				String actual_name = call.getCallee();
				String call_id = lookupProc(call.getCallee());
				
				/*we should add a check to see whether the called is a proc o a simple ig e.g. a chan*/
				SimpleId real_name = (SimpleId) store.get(call_id) ;
				if(real_name != null && real_name.getType() == ValueType.ID)
				{
					System.out.println("should execute procedure "+ real_name.getId());
					Procedure proc_def = (Procedure) procs.get(real_name.getId());

					if(!NO_MEMORY)
					{
						h = history.get(thread_name);
						h.add(new HistoryInvoke(actual_name, call.getParams()));
						history.put(thread_name, h);
					}
					if(call.getParams().isEmpty())
					{
						return new Sequence(proc_def.getBody().clone(), new Esc());
					}
					else
					{
						List<String> param = proc_def.getParams();
						List<String> actual_param = call.getParams();
						if(param.size() == actual_param.size())
						{
							//cloning the procedure body in order to rename it and to give it to the thread
							IStatement body= proc_def.getBody().clone();	
							for(int i=0; i < param.size(); i++)
							{
								body.rename(param.get(i), actual_param.get(i));
							
							}
							return new Sequence(body, new Esc());
						}
						else
						{
							System.out.println(warning + " size mismatch on invocation of procedure "+ call_id +"\n");
						}
					}
				}
				
				break;
			}
			case SKIP:
			{
				System.out.println("skip");
				if(!NO_MEMORY)
				{
					h = history.get(thread_name);
					h.add(new HistorySkip());
					history.put(thread_name, h);
				}
				return new Nil();
			}
	//better check if we need this anymore
		/*		case ESC:
			{
				if(!NO_MEMORY)
				{
					h = history.get(thread_name);

					h.add(new HistoryEsc());
					history.put(thread_name, h);
				}
					return new Nil();
			}
			*/
			case BREAK: {
						h = history.get(thread_name);
						h.add(new HistoryBreak());
						history.put(thread_name, h);
							throw new BreakPointException(new Nil(),warning +" breakpoint reached at thread "+ thread_name);
			}	
			case NIL:	return stm;
		
		}
			return null;
	}
	
	//tries to execute one step back of a given thread
	//returns -1 if it cannot perform the step, 
	// 0 for an internal back step and the gamma of a send/receive 
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
			System.out.println(warning + "empty history for thread "+thread_id+"\n");
			return -1;
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
			case BREAK: {
				if(body.getType() == StatementType.NIL)
					new_body = new BreakStatemet();
				else{
					new_body = new Sequence(new BreakStatemet(), body);
				}
				break;
			}
			case ASSERT:
			{
				HistoryAssert log = (HistoryAssert)action;

				if(body.getType() == StatementType.NIL)
					new_body = new Assert(log.getGuard());
				else{
					new_body = new Sequence(new Assert(log.getGuard()), body);
				}
				break;
				
			}
			case IF :{
				HistoryIf log = (HistoryIf)action;
				next = afterEsc(body);
				new_body = beforeEsc(body);
				if(log.isLeft())
				{
					
					//better check this part expecially if we need a clone ...
					new_body = new Conditional(log.getGuard(), new_body, log.getBody());
				}
				else
				{
					new_body = new Conditional(log.getGuard(),  log.getBody(), new_body);
					
				}
				break;
			}
			case VAR:
			{
				//var creation of a boolean value
				HistoryVar log = (HistoryVar)action;
				
				if(store.containsKey(log.getId()))
				{
					IValue val = null;
					if(expressions.containsKey(log.getId()))
					{
						val = expressions.remove(log.getId());
						store.remove(log.getId());
					}	
					else val =store.remove(log.getId());
					ret = log.getInstruction();
					//probably this check is useless since it there is always an esc delimiter of the scope
					if(body.getType() == StatementType.SEQUENCE)
					{
							
							next = afterEsc(body);
							new_body = beforeEsc(body);
							new_body = new Assignment(log.getId(), val, new_body);
					}
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
								System.out.println(warning +" cannot revert port creation of "+log.getPort_name() +" since it is not empty\n");
								return -1;
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
						//if the child has not executed (or has been fully reversed)
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
							ret = log.getInstruction();						
							threadlist.remove(xi);
							history.remove(xi);
							thread_child.remove(thread_id);
							thread_chan.remove(thread_id);
							
							System.out.println(warning+" destroying thread "+xi+"\n");
						}
						else
						{	//if the child has still some story = is not in its initial form
							throw new ChildMissingException("cannot revert thread creation of "+log.getThread_id() +" since it has not empty history \n", log.getThread_id());
						}
					}
				}
				break;
			}
			case SEND:{
				HistorySend log = (HistorySend)action;
				String id = log.getChan();
				ret = log.getInstruction();
//				SimpleId tmp = (SimpleId) store.get(id);
				String xi = lookupChan(id);
				Channel ch = chans.get(xi);
				if(ch !=null)
				{
					if(ch.isEmpty())
						//different kind of exception ... should reverse who read the msg ..
						throw new WrongElementChannel("value on channel "+xi +" does not belong to thread "+thread_id+"\n", ch.getReaders(thread_id));
					IValue val =ch.reverseSend(thread_id);
					//if val =  null means that on the channel there is something that does not belong to the thread
					if(val == null)
					{
						//System.out.println(ch.beforeThread(thread_id));
						throw new WrongElementChannel("value on channel "+xi +" does not belong to thread "+thread_id+"\n", ch.getSenders(thread_id));
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
				String xi = lookupChan(id);

//				String xi = ((SimpleId)store.get(id)).getId();
				Channel ch = chans.get(xi);
				//putting back msg
				if(!ch.reverseReceive(thread_id))
				{
					throw new WrongElementChannel("value on channel "+id +" does not belong to thread "+thread_id+"\n", ch.getbeforeMe(thread_id));
				}
				//receive is an assigment
				next = afterEsc(body);
				new_body = beforeEsc(body);
				new_body = new  Assignment(log.getVar(), new Receive(log.getFrom()), new_body);

				//erasing variables from store
				store.remove(log.getVar());
				
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
		System.out.println("... reversing thread "+thread_id +" undoing "+action);

		lst.remove(index);
		history.put(thread_id, lst);
		threadlist.put(thread_id, new_body);
		return ret;
	}
	
	
	//should look at the queue history and reverse the element
	private static void rollReceive(String chan_id, int n)
	{
		IValue chan = store.get(chan_id);
		if(isChan(chan_id))
		{
			String id = ((SimpleId)chan).getId();
			String lookup = lookupChan(id);
			Channel xi = chans.get(lookup);
			if(!xi.isEmptyHistory())
			{
				HashMap<String, Integer> map =xi.getlastNRead(n);
				rollTill(map);
			}
			else
			{
				System.out.println(warning+" empty channel history "+chan_id+"\n");
				
				
			}
		}
		else
		{
			System.out.println(warning+" invalid channel name "+chan_id+"\n");
		}
			
		
	}
	
	private static void rollSend(String chan_id, int n)
	{
		
		IValue chan = store.get(chan_id);
		if(isChan(chan_id))
		{
			String id = ((SimpleId)chan).getId();
			String lookup = lookupChan(id);
			Channel xi = chans.get(lookup);
			if(!xi.isEmpty())
			{
				HashMap<String, Integer> map =xi.getlastNSend(n);
				rollTill(map);
			}
			else
			{
				System.out.println(warning+" empty channel "+chan_id+"\n");
				
				
			}
		}
		else
		{
			System.out.println(warning+" invalid channel name "+chan_id+"\n");
		}
				
	}
	
	private static void rollThread (String thread)
	{
		if(parenthood.containsKey(thread))
		{		Tuple<String, Integer> t = parenthood.remove(thread);
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map.put(t.getFirst(), t.getSecond());
			rollTill(map);
		}
		else rollEnd(thread); 
			
	}
	
	private static void rollLet (String var_id)
	{
		if(variables.containsKey(var_id))
		{		Tuple<String, Integer> t = variables.remove(var_id);
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map.put(t.getFirst(), t.getSecond());
			rollTill(map);
		}
		else
		{
			System.out.println(warning+" variable identifier does not exists: "+var_id+"\n");
		}
	}
	
	//retrieves the first gamma !=0 in the memory starting from the end
	static int getGamma(String thread_id)
	{
		
		ArrayList<IHistory> lst=history.get(thread_id);
		 	
		
		int index = lst.size();
		while (index > 0)
		{
			index --;
			IHistory action = lst.get(index);
			int ret = action.getInstruction();
			if (ret != 0)
				return ret;
			
		}
		
		return -1;
		
	}
	
	//forces backward the execution till a certain action
	private static void rollTill(HashMap<String, Integer> map)
	{
		Iterator<String> it =  map.keySet().iterator();
		while(it.hasNext())
		{
			String id = it.next();
			int gamma = map.get(id);
			//getGamma retrieves the next gamma in the history
			while(gamma <= getGamma(id))
			{
				try {
					stepBack(id);
				} catch (WrongElementChannel e) {
					rollTill(e.getDependencies());
				} catch (ChildMissingException e) {
					rollEnd(e.getChild());
				}
				
			}
		
		}
	}
	
	//forces backward a thread to the beginning causing the failure of its caused actions
	private static void rollEnd(String thread_id)
	{

		while(history.get(thread_id).size() != 0)
		{
			try {
				stepBack(thread_id);
	//			System.out.println(nro);
			} catch (WrongElementChannel e) {
			
	//			System.out.println("roll till");
				rollTill(e.getDependencies());
				}
			 catch (ChildMissingException e) {
//				System.out.println(warning +" reversing child thread "+e.getChild() +"\n");
				rollEnd(e.getChild());
			}
		}
	}
	
	private static boolean rollNsteps(String thread_id, int steps)
	{
		boolean flag = false;
		
		while(history.get(thread_id).size() > 0 && steps >0)
		{
			try {
				stepBack(thread_id);
				steps--;
				flag = true;
			//	System.out.println(nro);
			} catch (WrongElementChannel e) {
			
	//			System.out.println("roll till");
				rollTill(e.getDependencies());
				}
			 catch (ChildMissingException e) {
//				System.out.println(warning +" reversing child thread "+e.getChild());
				rollEnd(e.getChild());
			}
		}
		return flag;
	}
	
	private static String generateChanId(String t_id)
	{
//		return "chan_"+(chan_count++);
		int nro = 0;
		
		if(thread_chan.containsKey(t_id))
			nro = thread_chan.get(t_id);
		
		nro++;
		thread_chan.put(t_id, nro);
		String cid = t_id+"_xi_"+nro;
		if(store.containsKey(cid))
			System.out.println(warning + " "+cid + " identifier already used as variable.");
		return cid;
	

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
	
	private static String generateThreadId(String t_id)
	{
		
		int nro = 0;
		
		if(thread_child.containsKey(t_id))
			nro = thread_child.get(t_id);
		
		nro++;
		thread_child.put(t_id, nro);
		String tid = t_id+nro;
		if(store.containsKey(tid))
			System.out.println(warning + " "+tid + " identifier already used as variable.");
		return t_id +"_"+nro;
		
		/*while(true)
		{
			ret="t_"+(thread_count++);
			if(!store.containsKey(ret));
				break;
		}*/
		//return ret;

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
		System.out.println("\t back (b)  thread_name (tries to execute backward one step of thread_name)");
//		System.out.println("\t undo (u)  thread_name  n (forces backward the execution of n steps of thread_name)");
		System.out.println("\t roll (r) thread_name n (rollsback a thread at its starting point)");
		System.out.println("\t rollsend (rs) chan_name n (rolls last n send on a given channel)");
		System.out.println("\t rollreceive (rr) chan_name n (rolls the last n receive on a given channel)");
		System.out.println("\t rollthread (rt) thread_name (rolls the creation of a thread)");
		System.out.println("\t rollvariable (rv) id (rolls the creation of a variable)");
		
		System.out.println("\t run  (runs the program till the first breaktpoint/false assertion or eventually terminates the execution)");
		System.out.println("\t dump (d) filename (dumps the configuration)");
		System.out.println("\t restore filename (restores a dumped configuration)");
		
			
		System.out.println("\t list (l) (displays all the available threads)");
		System.out.println("\t print (p) id (shows the state of a thread, channel, or variable)");
		System.out.println("\t history (h) id (shows thread/channel computational history)");
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
				case CONST:
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
	
	static private boolean isChan(String id)
	{
		IValue val = null;
		if ((val = store.get(id)) !=null)  
		{	
			if(val.getType() == ValueType.ID) 
			{
				String xi = ((SimpleId)val).getId();
				if(store.get(xi)!=null)
					return isChan(xi);
				
				return chans.containsKey(xi);
			}
		}
		return false;
	}
	
	static private String lookupChan(String id)
	{
		
		IValue val = null;

		//if it is a xi 
		if(chans.containsKey(id))
			return id;
		
		if ((val = store.get(id)) !=null)  
		{	
			if(val.getType() == ValueType.ID) 
			{
				String xi = ((SimpleId)val).getId();
				if(store.get(xi)!=null)
					return lookupChan(xi);
				
				return xi;
			}
		}
		
		return null;
	}
	
	//returns the id of the original variable
	private static IValue lookupVar(String id)
	{
		//perhaps also for proc
		if(chans.containsKey(id) || procs.containsKey(id))
			return new SimpleId(id);
		IValue val = store.get(id);
		if(val == null)
			return null;
		
		//better check on channels
		switch (val.getType()) {
		case ID:
			return lookupVar( ((SimpleId)val).getId());
		case INT_ID:

			return lookupVar(((IntID)val).getValue());
		default:
			return val;
		}
	}
	
	static private String lookupProc(String id)
	{
		
		IValue val = null;
		
		if(procs.containsKey(id))
			return id;
		
		if ((val = store.get(id)) !=null)  
		{	
			if(val.getType() == ValueType.ID) 
			{
				String xi = ((SimpleId)val).getId();
				if(store.get(xi)!=null && !procs.containsKey(xi))
					return lookupProc(xi);
				return id;
			}
		}
		
		return null;
	}
	
	static void printHistory(String id)
	{
		if(!history.containsKey(id) && !isChan(id))
		{
			System.out.println(warning + " no history for identifier "+ id+"\n");
			return;
		
		}
		ArrayList<IHistory> h = history.get(id);
	
		if(h != null)
		{	
			if(h.size() == 0)
			{
				System.out.println(warning +" empty history for thread "+id+"\n");
			}
			for (IHistory log : h) {
				if(log.getType() != HistoryType.ESC)
					System.out.println(log.toString());
			}
		}
		else
		{
			//prints the history of a channel (if any)
			
			String xi = lookupChan(id);//((SimpleId)store.get(id)).getId();
			Channel ch = chans.get( xi);
			if(ch.emptyStory())
				System.out.println(warning +" empty history for channel "+id+"\n");
			else printChanHistory(xi);
		}
	}
	
	static void printChanHistory(String chan_id)
	{
		Channel ch = chans.get(chan_id);
		
		if(NO_MEMORY)
		{
			System.out.println(warning + " empty history for channel "+ chan_id+"\n");
			return;
		}
		if(ch == null)
		{	
			System.out.println(warning + " no history for channel "+ chan_id+"\n");
			return;
		}
		
		LinkedList<Tuple<Tuple<IValue, String>, String>> h = ch.getStory();
		Iterator<Tuple<Tuple<IValue, String>, String>> it = h.descendingIterator();
		while(it.hasNext())
		{
			Tuple<Tuple<IValue, String>, String> ith = it.next();
			IValue val = ith.getFirst().getFirst();
			String tostring;
			if(val.getType() == ValueType.ID)
			{
				tostring = printId( ((SimpleId)val).getId() );
			}
			else tostring= ith.getFirst().getFirst().toString();
			System.out.println( "("+ tostring +" , " +ith.getFirst().getSecond() + " , "+ith.getSecond() +")");

			
		}
		
	}
	
	//there should be a better recursive implementation of this
	
	//return the rest of a statement sequence after the FIRST ESC
	
	private static boolean containEsc(IStatement stm)
	{
		ArrayList<IStatement> queue = new ArrayList<IStatement>();
		queue.add(stm);
		while(true)
		{
			if(queue.isEmpty())
				return false;
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
		return true;
		
	}
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
			//append the element at the end of the list rest
			if(el.getType() != StatementType.SEQUENCE)
				rest.add(el);
			if(el.getType()== StatementType.SEQUENCE)
			{
				Sequence seq = (Sequence) el;
				//in this way the first element will be the left element
				queue.add(0,seq.getDx());
				queue.add(0,seq.getSx());
			}
		}
		return build(rest);
	}
	
	
	
	//builds back a statement (simple or sequence) from a  list
	private static IStatement build(ArrayList<IStatement> queue)
	{
		if(queue.size() == 0)
			return null;
		if(queue.size() == 1)
			return queue.remove(0);
		else return new Sequence (queue.remove(0), build(queue));
			
	}
	
	
	//dump of the entire configuration
	@SuppressWarnings("unchecked")
	private static void dump(String filename)
	{
		HashMap<String, IValue> dstore=  new HashMap<String, IValue>();
		Iterator<String> it = store.keySet().iterator();
		String key;
		while(it.hasNext())
		{
			key = it.next();
			dstore.put(key, store.get(key).clone());
		}
		
		HashMap<String, Channel> dchans =new HashMap<String, Channel>();
		Iterator<String> itch= chans.keySet().iterator();
		while(itch.hasNext())
		{
			key = itch.next();
			dchans.put(key, chans.get(key).clone());
		}
		
		
		HashMap<String,IValue> dprocs = (HashMap<String, IValue>) procs.clone();
		HashMap<String,IStatement> dthreadlist = (HashMap<String, IStatement>) threadlist.clone();
		
		HashMap<String , ArrayList<IHistory>> dhistory = new HashMap<String, ArrayList<IHistory>>();
		Iterator<String> ith = history.keySet().iterator();
		while(ith.hasNext())
		{
			key = ith.next();
			dhistory.put(key, (ArrayList<IHistory>) history.get(key).clone());
		}
		
		HashMap<String, IValue> dexp = new HashMap<String, IValue>();
		ith = expressions.keySet().iterator();
		while(ith.hasNext())
		{
			key = ith.next();
			dexp.put(key, expressions.get(key).clone());
		}
		
		/*the copying here are useless we can use directly clone on the structure*/
		HashMap<String, Integer> dthread_child = new HashMap<String, Integer>();
		ith = thread_child.keySet().iterator();
		while(ith.hasNext())
		{
			key = ith.next();
			dthread_child.put(key, thread_child.get(key));
		}
		
		HashMap<String, Integer> dthread_chan =  new HashMap<String, Integer>();
		ith = thread_chan.keySet().iterator();
		while(ith.hasNext())
		{
			key = ith.next();
			dthread_chan.put(key, thread_chan.get(key));
		}
		
		HashMap<String,Tuple<String,Integer>> dparenthood = new HashMap<String, Tuple<String,Integer>>();
		ith = parenthood.keySet().iterator();
		while(ith.hasNext())
		{
			key = ith.next();
			dparenthood.put(key, parenthood.get(key));
		}
		HashMap<String,Tuple<String,Integer>> dvariables = new HashMap<String, Tuple<String,Integer>>();
		ith = variables.keySet().iterator();
		while(ith.hasNext())
		{
			key = ith.next();
			dvariables.put(key, variables.get(key));
		}
		dump = new DumpedConfiguration(dstore, dchans, dprocs, dthreadlist, dhistory, dexp, dthread_child, 
				dthread_chan, dparenthood, dvariables, pc);
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream("./"+filename);
			  ObjectOutputStream out = new ObjectOutputStream(fileOut);
		         out.writeObject(dump);
		         out.close();
		         fileOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		       	}

	
	static boolean evaluateExp(BoolExpr exp)
	{
		boolean ret=false;
		switch (exp.getOp()) {
		case EQ:
			 ret = (evaluateExp(exp.getSx()) == evaluateExp(exp.getDx()));
			break;
		case LT:
			 ret = (evaluateExp(exp.getSx()) < evaluateExp(exp.getDx()));
			 break;
		case LE:
			 ret = (evaluateExp(exp.getSx()) <= evaluateExp(exp.getDx()));
			 break;	 
		case GT:
			 ret = (evaluateExp(exp.getSx()) > evaluateExp(exp.getDx()));
			 break;
		case GE:
			 ret = (evaluateExp(exp.getSx()) >= evaluateExp(exp.getDx()));
			 break;
			
		default:
			break;
		}
		return ret;
	}
	
	static int evaluateExp(IntExp exp)
	{
		int ret =0;
		switch (exp.getType()) {
		case INT_ID:
		{
			String id = ((IntID) exp).getValue();
			IValue val = lookupVar(id);
			if(val == null)
			{
				//maybe a better handling
				System.out.println(error + "undeclared variable "+id);
				System.exit(-1);
			}
			//should not be null
			switch (val.getType()){
				case CONST:
						return ((IntConst) val).getValue();
				case SUM:
					{
						SumValue cast = (SumValue)val;
						int s=evaluateExp(cast.getSx()); 
						int d = evaluateExp(cast.getDx());
						return s+d;
					}
				case SUB:
				{
					SubValue cast = (SubValue)val;
					int s=evaluateExp(cast.getSx()); 
					int d = evaluateExp(cast.getDx());
					return s-d;
				}
				case DIV:
				{
					SubValue cast = (SubValue)val;
					int s=evaluateExp(cast.getSx()); 
					int d = evaluateExp(cast.getDx());
					return s/d;
				}
		
				case MUL:
				{
					DivValue cast = (DivValue)val;
					int s=evaluateExp(cast.getSx()); 
					int d = evaluateExp(cast.getDx());
					return s*d;
				}
			}
			break;
		}
		case CONST:
		{
			return ((IntConst)exp).getValue();
		}
		case SUM:
		{
			SumValue cast = (SumValue)exp;
			return evaluateExp(cast.getSx()) + evaluateExp(cast.getDx());

		}
		case SUB:
		{
			SubValue cast = (SubValue)exp;
			return evaluateExp(cast.getSx()) - evaluateExp(cast.getDx());
		}
		case MUL:
		{
			MulValue cast = (MulValue)exp;
			return evaluateExp(cast.getSx()) * evaluateExp(cast.getDx());
		}
		case DIV:
		{
			DivValue cast = (DivValue)exp;
			return evaluateExp(cast.getSx()) / evaluateExp(cast.getDx());
		}
		

		default:
			break;
		}
		return ret;
	}
	
	static private void printThreads()
	{
		Iterator<String> iterator = threadlist.keySet().iterator();
		String key;
		String toPrint="";
		while(iterator.hasNext())
		{
			key=iterator.next();
			IStatement stm = threadlist.get(key);
			if(stm.getType()== StatementType.NIL)
				toPrint+="(" + key +", T)  ";
			else
				toPrint+="(" + key +", A)  ";
					
		}
		System.out.println("\nAvailable threads (Active, Terminated) : \n"+toPrint);
		System.out.println();
	}
	
	static private boolean isConstantExp(IntExp exp)
	{
		switch (exp.getType()) {
		case CONST:
					return true;
		case SUB:
		case SUM:
		case DIV:
		case MUL:
		{
			BinaryIntExp op = (BinaryIntExp) exp;
			return isConstantExp(op.getSx()) && isConstantExp(op.getDx());
		}
		default:
			break;
		}
		return false;
	}
	
	
	//decides whether a thread can move forward : always true but when it is reading on an empty channel or it has already terminated (nil statement)
	static private boolean canMove(IStatement stm)
	{
		//shoudl be recursive
		switch (stm.getType()) {
		case SEQUENCE : {
					Sequence seq = (Sequence)stm;
					return canMove(seq.getSx());
			
		}
		case LET:
				IValue val = ((Assignment)stm).getV();
				if(val.getType() == ValueType.RECEIVE)
				{
					Receive rec = (Receive) val;
					String from =rec.getFrom();
					String xi = lookupChan(from);
					if(chans.containsKey(xi))
					{
						Channel ch = chans.get(xi);
						return !ch.isEmpty();
					}
				}
				else return true;
		case NIL: return false;

		default:
			return true;
		}

	}
	
}

