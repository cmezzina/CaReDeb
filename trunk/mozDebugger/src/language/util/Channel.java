package language.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import language.value.IValue;
import language.value.SimpleId;

public class Channel {

	//the first element in the channel is the last one inserted
	
	//value, sender
	private LinkedList<Tuple<IValue,String>> value;
	private LinkedList<Integer> pc_sender;
	//value, sender, receiver
	private LinkedList<Tuple<Tuple<IValue,String>,String>> story;
	private LinkedList<Integer> pc_reader;
	
	
	
	public Channel()
	{
		value = new LinkedList<Tuple<IValue,String>>();
		story = new LinkedList<Tuple<Tuple<IValue,String>,String>>();
		pc_sender = new LinkedList<Integer>();
		pc_reader = new LinkedList<Integer>();
	}
	
	public boolean isEmpty()
	{
		return value.isEmpty();
	}
	
	public void send(IValue val, String thread, int gamma)
	{
		value.add(new Tuple<IValue, String>(val, thread));
		pc_sender.add(gamma);
//		System.out.println(" ... inserted value "+gamma +" by thread "+thread );
	}
	
	public IValue receive(String thread, int gamma)
	{
		if(value.isEmpty())
			return null;
		
		Tuple<IValue, String> ret= value.removeFirst();
		pc_reader.addFirst(gamma);
		story.addFirst(new Tuple<Tuple<IValue,String>,String>(ret,thread));
	//	System.out.println(" ... reading "+thread +" "+gamma );

		return ret.getFirst();
	}
	
	//removes the tail of the channel (reverse send)
	public IValue reverseSend(String thread)
	{
		if(value.isEmpty())
			return null;
		Tuple<IValue, String> head = value.getLast();
		//the head of the channel belongs to the thread
		if(head.getSecond().equals(thread))
		{
			value.removeLast();
			pc_sender.removeLast();
//			int i=pc_sender.removeLast();
	//		System.out.println(i);
			return head.getFirst();
		}
		else return null;
			
	}
	
	//augment the index
	public boolean reverseReceive(String thread)
	{
		if(story.isEmpty())
			return false;
		//the receiver of the top (tuple) of the history shold be = thread
		Tuple<Tuple<IValue, String>, String> log = story.getFirst();
		if(log.getSecond().equals(thread))
		{
			story.remove();
			pc_reader.remove();
			value.addFirst(log.getFirst());
			return true;
		}
			return false;
	}
	
	public List<Tuple<IValue,String>> getValues()
	{
		return value;
	}
	
	public LinkedList<Tuple<Tuple<IValue, String>, String>> getStory()
	{
		return story;
	}
	
	
	//returns a list of readers that have to release their msg
	public HashMap<String, Integer> getReaders(String thread)
	{
		HashMap<String,Integer> ret= new HashMap<String, Integer>();
		Iterator<Tuple<Tuple<IValue,String>,String>> it= story.iterator();
		LinkedList<Integer> tmp_reader = new LinkedList<Integer>(pc_reader);

		while(it.hasNext())
		{	
			Tuple<Tuple<IValue, String>, String> val = it.next();
			int gamma = tmp_reader.removeLast();
			
			String sender = val.getFirst().getSecond();
				//first occurrence
				if(!ret.containsKey(val.getSecond()) || ret.get(val.getSecond()) > gamma)
				{
					ret.put(val.getSecond(),gamma);
				}
				
			//	ret.add(val.getSecond());
				if(sender.equals(thread))
					break;
				
		}
		return ret;
	}
	
	
	public HashMap<String, Integer> getSenders(String thread)
	{
		HashMap<String,Integer> ret = new HashMap<String,Integer>();
		Tuple<IValue,String> val;
		LinkedList<Integer> tmp_sender = new LinkedList<Integer>(pc_sender);
		Iterator<Tuple<IValue, String>> it = value.descendingIterator();
		while(it.hasNext())
		{
			val = it.next();
			if(val.getSecond().equals(thread))
				return ret;
			else
			{
				ret.put(val.getSecond(), tmp_sender.removeLast());
			}
		}
		return ret;
	}
	
	
	
	//for testing purposes
	public static void main(String args[])
	{
		Channel ch = new Channel();
		ch.send(new SimpleId("pippo"), "t3", 4);
		ch.send(new SimpleId("pluto"), "t2", 1);
		ch.send(new SimpleId("paperino"), "t1", 2);
		
		
	//	System.out.println(ch.reverseSend("t1"));
		System.out.println(ch.getValues());
		System.out.println(ch.receive("t4",18));
		System.out.println(ch.receive("t3",12));
		System.out.println(ch.getReaders("t1"));
		System.out.println(ch.reverseReceive("t4"));
		System.out.println(ch.reverseReceive("t4"));
		
		System.out.println(ch.getValues());

		
		
		
	}
}
