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

import java.io.Serializable;

public interface IStatement extends Cloneable, Serializable {

    StatementType getType();

    public void rename(String old_id, String new_id);

    // clone should be of an abstract class
    public IStatement clone();
}
