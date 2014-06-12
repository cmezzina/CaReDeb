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



public class BoolValue implements IValue {
	private boolean value;
	
	public BoolValue(boolean v)
	{
		this.value = v;
	}
	
	public boolean getValue()
	{
		return this.value;
	}
	
	public void setValue(boolean v)
	{
		this.value = v;
	}
	public String toString()
	{
		return new Boolean(value).toString();
	}

	@Override
	public ValueType getType() {
		return ValueType.BOOLEAN;
	}

	@Override
	public void rename(String old_id, String new_id) {
		// TODO Auto-generated method stub
		
	}
	
	public IValue clone()
	{
		return new BoolValue(value);
	}

	
	
}
