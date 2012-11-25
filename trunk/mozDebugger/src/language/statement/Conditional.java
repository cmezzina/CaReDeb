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

public class Conditional implements IStatement {

	

	String guard;
	IStatement left;
	IStatement right;
	public String getGuard() {
		return guard;
	}
	public void setGuard(String guard) {
		this.guard = guard;
	}
	public IStatement getLeft() {
		return left;
	}
	public void setLeft(IStatement left) {
		this.left = left;
	}
	public IStatement getRight() {
		return right;
	}
	public void setRight(IStatement right) {
		this.right = right;
	}
	public Conditional(String guard, IStatement left, IStatement right) {
		super();
		this.guard = guard;
		this.left = left;
		this.right = right;
	}
	
	public String toString()
	{
		return "if "+ guard +" then " +left +" else "+ right + " end";
	}
	@Override
	public StatementType getType() {
		return StatementType.IF;
	}
	@Override
	public void rename(String old_id, String new_id) {
		if(guard.equals(old_id))
		{
			guard=new_id;
		}
		left.rename(old_id, new_id);
		right.rename(old_id, new_id);
		return;
	}
	
	public Conditional clone()
	{
		return new Conditional(guard, left.clone(), right.clone());
	}
	
}
