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

public class SimpleId implements IValue {

	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	public SimpleId(String id) {
		super();
		this.id = id;
	}

	@Override
	public ValueType getType() {
		return ValueType.ID;
	}
	
	public String toString()
	{
		return id;
		
	}

	@Override
	public void rename(String old_id, String new_id) {
		// TODO Auto-generated method stub
		System.out.println("SimpleIDclass ... call rename on simple id? WHY???");
	}
	
	public SimpleId clone()
	{
		return new SimpleId(id);
	}

}
