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

public class Esc implements IStatement {

    @Override
    public void rename(String old_id, String new_id) {
        // TODO Auto-generated method stub

    }

    @Override
    public StatementType getType() {
        return StatementType.ESC;
    }

    @Override
    public Esc clone() {
        return new Esc();
    }

    @Override
    public String toString() {
        // return "esc";
        return "";
    }
}
