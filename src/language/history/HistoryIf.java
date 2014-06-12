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

import java.io.Serializable;

import language.statement.IStatement;
import language.value.IValue;

public class HistoryIf implements IHistory {

	private IValue guard;
	private IStatement body;
	//indicates which part of the branch was not executed
	private boolean left;
	
	public HistoryIf(IValue guard, IStatement body, boolean left) {
		super();
		this.guard = guard;
		this.body = body;
		this.left = left;
	}


	public IValue getGuard() {
		return guard;
	}


	public void setGuard(IValue guard) {
		this.guard = guard;
	}


	public IStatement getBody() {
		return body;
	}


	public void setBody(IStatement body) {
		this.body = body;
	}


	public boolean isLeft() {
		return left;
	}


	public void setLeft(boolean left) {
		this.left = left;
	}


	@Override
	public HistoryType getType() {
		// TODO Auto-generated method stub
		return HistoryType.IF;
	}

	public IHistory clone()
	{
		return new HistoryIf(guard, body.clone(), left);
	}
	public String toString()
	{
		String ret = "if ( "+guard.toString() +" ) ";
		if(left)
			return ret +" then _ else "+body.toString() + " end ";
		
		return ret +" then  "+body.toString() + " else _ end ";
		
	}


	@Override
	public int getInstruction() {
		// TODO Auto-generated method stub
		return 0;
	}
}
