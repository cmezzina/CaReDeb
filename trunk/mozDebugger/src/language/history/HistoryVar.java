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

public class HistoryVar implements IHistory {

	private String id;
	private int instruction;

	public HistoryVar(String id, int instruction) {
		super();
		this.instruction=instruction;
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
		return HistoryType.VAR;
	}

	public String toString()
	{
		return "new variable "+id;
	}
	
	public int getInstruction() {
		return instruction;
	}
	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}
	public IHistory clone()
	{
		return new HistoryVar(id, instruction);
	}
}
