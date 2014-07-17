/*******************************************************************************
 * Copyright (c) 2012 Claudio Antares Mezzina.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Claudio Antares Mezzina - initial API and implementation
 *     Davide Riccardo Caliendo - implementation
 ******************************************************************************/

package language.statement;

import language.value.expression.Expression;;

public class Assert implements IStatement {

    private static final long serialVersionUID = 2L;
    private Expression guard;

    public Assert(Expression guard) {
        this.guard = guard;
    }

    public Expression getGuard() {
        return guard;
    }

    @Override
    public StatementType getType() {
        return StatementType.ASSERT;
    }

    @Override
    public void rename(String old_id, String new_id) {
        guard.rename(old_id, new_id);
    }

    @Override
    public Assert clone() {
        return new Assert((Expression)guard.clone());
    }

    @Override
    public String toString() {
        return "assert( " + guard.toString() + " )";
    }
}
