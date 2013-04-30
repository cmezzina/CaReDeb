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
package language.util;

public class Tuple<E,V> {
	
	private E first;
	private V second;
	public Tuple(E first, V second) {
		super();
		this.first = first;
		this.second = second;
	}
	public E getFirst() {
		return first;
	}
	public void setFirst(E firt) {
		this.first = firt;
	}
	public V getSecond() {
		return second;
	}
	public void setSecond(V second) {
		this.second = second;
	}
	
	public String toString()
	{
		return "("+first.toString() + " , "+ second.toString()+")";
	}
}
