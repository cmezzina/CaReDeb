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

import java.util.ArrayList;


public class HistoryInvoke implements IHistory {

	private String proc_name;
	private ArrayList<String> param;
	
	public String getProc_name() {
		return proc_name;
	}
	public void setProc_name(String proc_name) {
		this.proc_name = proc_name;
	}
	public ArrayList<String> getParam() {
		return param;
	}
	public void setParam(ArrayList<String> param) {
		this.param = param;
	}
	@Override
	
	public HistoryType getType() {
		// TODO Auto-generated method stub
		return HistoryType.INVOKE;
	}
	public HistoryInvoke(String proc_name, ArrayList<String> param) {
		super();
		this.proc_name = proc_name;
		this.param = param;
	}

	public String toString()
	{
		String ret = "{ "+proc_name ;
		for (String var : param) {
			ret+=" "+var;
		}
		return ret+" }";
	}
}
