package language.util;

import java.util.ArrayList;

import language.value.IValue;

public class Channel {

	//the first element is the last one inserted
	private ArrayList<IValue> value;
	private ArrayList<String> sender;
	private int index;
	
	public Channel()
	{
		value = new ArrayList<IValue>();
		sender = new ArrayList<String>();
		index = 0;
	}
	
	public boolean isEmpty()
	{
//		if(index == 0)
		return (index == 0);
	}
	
	public void add(IValue val, String thread)
	{
		value.add(val);
		sender.add(thread);
		index++;
//		System.out.println(" ... inserted value "+val +" by thread "+thread );
	}
	
	public IValue getHead()
	{
		if(index == 0)
			return null;
		
		int tmp = index;
		index--;
		return value.get(value.size() - tmp );
	}
	
	//removes the head of the channel (reverse send)
	public IValue sendBack(String thread)
	{
		IValue ret=null;
		int i = sender.size() - index;
		if(thread.equals(sender.get(i)))
		{
			ret = value.remove(i);
			sender.remove(i);
			index--;
		}
		return ret;
	}
	
	//augment the index
	public void receiveBack()
	{
		index++;
	}
	
	//for printing purpose
	public ArrayList<IValue> getValues()
	{
		ArrayList<IValue> ret = new ArrayList<IValue>();
		if(!value.isEmpty())
		{	
			int i = index-1;
			
			for(int j=0;  j <=i ; j++ )
				ret.add(value.get(j));
		}
				return ret;
	}
	

}
