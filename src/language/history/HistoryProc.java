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
package language.history;

public class HistoryProc implements IHistory {
	private String id;
	
	public HistoryProc(String id) {
		super();
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public HistoryType getType() {
		// TODO Auto-generated method stub
		return HistoryType.PROCEDURE;
	}

	public String toString()
	{
		return "new function "+id;
	}
	
	public IHistory clone()
	{
		return new HistoryProc(id);
	}
	@Override
	public int getInstruction() {
		// TODO Auto-generated method stub
		return 0;
	}
}
