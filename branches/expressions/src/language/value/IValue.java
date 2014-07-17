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

package language.value;

import java.io.Serializable;

import language.value.type.Type;

public abstract class IValue implements Cloneable, Serializable {
    private static final long serialVersionUID = 2L;
    protected Type type;
    public Type getType() {
        return type;
    }
    public abstract void rename(String old_id, String new_id);
    public abstract IValue clone();
    public abstract String toString();
}