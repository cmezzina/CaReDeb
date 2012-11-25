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

public class HistoryThread implements IHistory {

	private String thread_id;
	public HistoryThread(String thread_id) {
		super();
		this.thread_id = thread_id;
	}
	public String getThread_id() {
		return thread_id;
	}
	public void setThread_id(String thread_id) {
		this.thread_id = thread_id;
	}
	@Override
	public HistoryType getType() {
		return HistoryType.THREAD;
	}
	public String toString()
	{
		return "new thread "+thread_id;
	}
}
