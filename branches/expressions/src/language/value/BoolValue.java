/*******************************************************************************
 * Copyright (c) 2014 Davide Riccardo Caliendo .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package language.value;

import language.value.type.BoolType;

public class BoolValue extends IValue {

    private static final long serialVersionUID = 2L;
    private boolean value;
    
    public BoolValue(boolean val) {
        value = val;
        type = BoolType.getInstance();
    }

    @Override
    public void rename(String old_id, String new_id) {
    }    

    public boolean getTerm() {
        return value;
    }

    public String toString() {
        if (value)
            return "true";
        else
            return "false";
    }
    
    public IValue clone() {
        return new BoolValue(value);
    }
}