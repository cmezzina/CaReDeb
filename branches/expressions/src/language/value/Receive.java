/*******************************************************************************
 * Copyright (c) 2012 Claudio Antares Mezzina.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Claudio Antares Mezzina - initial API and implementation
 ******************************************************************************/
package language.value;



public class Receive implements IValue {

	private String from;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Receive(String from) {
		super();
		this.from = from;
	}

	@Override
	public ValueType getType() {
		return ValueType.RECEIVE;
	}
	
	public String toString()
	{
		return  "{ receive "+ from +" }";
	}

	@Override
	public void rename(String old_id, String new_id) {
	
		if(from.equals(old_id))
			from = new_id;
	}
	
	public Receive clone()
	{
		return new Receive(from);
	}

	

}
