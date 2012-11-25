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
package expection;


public class ChildMissingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8770333969465630446L;
	String msg;
	String child;
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getChild() {
		return child;
	}
	public void setChild(String child) {
		this.child = child;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public ChildMissingException(String msg, String child) {
		super();
		this.msg = msg;
		this.child = child;
	}
	
}
