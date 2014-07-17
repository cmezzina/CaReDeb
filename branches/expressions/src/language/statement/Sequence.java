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

public class Sequence implements IStatement {

    private IStatement sx;
    private IStatement dx;

    public Sequence(IStatement sx, IStatement dx) {
        this.sx = sx;
        this.dx = dx;
    }

    public IStatement getSx() {
        return sx;
    }

    public void setSx(IStatement sx) {
        this.sx = sx;
    }

    public IStatement getDx() {
        return dx;
    }

    public void setDx(IStatement dx) {
        this.dx = dx;
    }

    @Override
    public StatementType getType() {
        return StatementType.SEQUENCE;
    }

    @Override
    public String toString() {
        String next;
        if (dx != null && dx.getType() != StatementType.ESC) {
            next = dx.toString();
            if (!next.equals("")) {
                return sx.toString() + ";" + dx.toString();
            } else {
                return sx.toString();
            }
        }
        return sx.toString();
    }

    @Override
    public void rename(String old_id, String new_id) {
        this.sx.rename(old_id, new_id);
        this.dx.rename(old_id, new_id);
    }

    @Override
    public Sequence clone() {
        return new Sequence(sx.clone(), dx.clone());
    }
}
