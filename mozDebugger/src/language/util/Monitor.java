package language.util;

import java.util.ArrayList;
import java.util.HashMap;

public class Monitor {

	//a monitor contains the number of move for each thread and the IO sequence on channels
	private HashMap<String,Integer> move; 
	private HashMap<String, ArrayList<Tuple<String, Boolean>>> ch;
	
	public Monitor(HashMap<String,Integer> s, HashMap<String, ArrayList<Tuple<String, Boolean>>> chanStory)
	{
		this.move=s;
		this.ch = chanStory;
	}
	
	public boolean canMove(String tid)
	{
		//should always contain it ...
		if(move.containsKey(tid))
		{
			if (move.get(tid)>0)
				return true;
			
		}	
		return false;
		
	}
	public void moved(String tid)
	{
		move.put(tid, move.get(tid)-1);
		
	}
	
	/*
	 * operation true if it is read, false if it is send
	 * */
	public boolean isTurnChan(String tid, String chan, Boolean operation)
	{
		ArrayList<Tuple<String,Boolean>>lst = ch.get(chan);
		Tuple<String,Boolean>action = lst.get(0);
		if(action.getFirst() == tid && action.getSecond() == operation)
		{
			lst.remove(0);
			return true;
		}
		return false;
	}
	
	
	public static void main(String args[])
	{
		HashMap<String, Integer> s = new HashMap<String, Integer>();
		HashMap<String, ArrayList<Tuple<String, Boolean>>> chans = new HashMap<String, ArrayList<Tuple<String,Boolean>>>();
		
		ArrayList<Tuple<String,Boolean>>	tmp = new ArrayList<Tuple<String, Boolean>>();
		tmp.add(new Tuple("t_0",false));
		tmp.add(new Tuple("t_1",true));
		tmp.add(new Tuple("t_1", false));
		chans.put("xi", tmp);
		Monitor m = new Monitor(s,chans);
		
		System.out.println(m.isTurnChan("t_0", "xi", false));
		System.out.println(m.isTurnChan("t_1", "xi", true));
		System.out.println(m.isTurnChan("t_1", "xi", false));

		
		
	}
}
