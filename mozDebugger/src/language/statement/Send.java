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
package language.statement;

public class Send implements IStatement {
	private String obj; //channel on which to write
	private String sub; //identifier sent
	public String getObj() {
		return obj;
	}
	public void setObj(String obj) {
		this.obj = obj;
	}
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public Send(String obj, String sub) {
		super();
		this.obj = obj;
		this.sub = sub;
	}
	@Override
	public StatementType getType() {
		return StatementType.SEND;
	}
	
	public String toString()
	{
		return  "{send " + obj + " " + sub + "}";
	}
	@Override
	public void rename(String old_id, String new_id) {
		
		if(obj.equals(old_id))
		{
			obj = new_id;
			return;
		}

		if(sub.equals(old_id))
		{
			sub = new_id;
			return;
		}
		
	}
	public Send clone()
	{
		return new Send(obj, sub);
	}
}
