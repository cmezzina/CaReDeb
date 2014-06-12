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

public class ThreadStm implements IStatement {

	private IStatement body;

	public IStatement getBody() {
		return body;
	}

	public void setBody(IStatement body) {
		this.body = body;
	}

	public ThreadStm(IStatement body) {
		super();
		this.body = body;
	}

	@Override
	public StatementType getType() {
		return StatementType.SPAWN;
	}
	
	public String toString()
	{
		return "thread "+ body + " end ";
	}

	@Override
	public void rename(String old_id, String new_id) {
		// TODO Auto-generated method stub
		body.rename(old_id, new_id);
	}
	
	public ThreadStm clone()
	{
		return new ThreadStm(body.clone());
	}
}
