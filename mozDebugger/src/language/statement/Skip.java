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

public class Skip implements IStatement {

	@Override
	public StatementType getType() {
		return StatementType.SKIP;
	}
	
	public String toString()
	{
		return "skip";
	}

	@Override
	public void rename(String old_id, String new_id) {
		// TODO Auto-generated method stub
		
	}
	public Skip clone()
	{
		return new Skip();
		
	}
}
