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

import language.statement.Skip;

public class HistorySkip implements IHistory {

	@Override
	public HistoryType getType() {
		// TODO Auto-generated method stub
		return HistoryType.SKIP;
	}
	public String toString()
	{
		return "skip";
	}
	public IHistory clone()
	{
		return new HistorySkip();
	}
}
