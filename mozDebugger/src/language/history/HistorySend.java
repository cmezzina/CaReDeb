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

public class HistorySend implements IHistory {

	private String chan;
	private int instruction;
	@Override
	public HistoryType getType() {
		// TODO Auto-generated method stub
		return HistoryType.SEND;
	}
	public String getChan() {
		return chan;
	}
	public void setChan(String chan) {
		this.chan = chan;
	}
	
	public HistorySend(String chan, int instruction) {
		super();
		this.instruction=instruction;
		this.chan = chan;
	}
	public int getInstruction() {
		return instruction;
	}
	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}
	@Override
	public String toString() {
		return "wrote on " + chan;
	}

	public IHistory clone()
	{
		return new HistorySend(chan, instruction);
	}
}
