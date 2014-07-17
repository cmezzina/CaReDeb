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
package language.value;

import language.value.type.PortType;

public class PortCreation extends IValue {

    private static final long serialVersionUID = 1L;
    
    public PortCreation() {
        type = PortType.getInstance();
    }

    @Override
    public String toString() {
        return "port";
    }

    @Override
    public void rename(String old_id, String new_id) {
    }

    @Override
    public PortCreation clone() {
        return new PortCreation();
    }

}
