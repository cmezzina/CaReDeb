/*******************************************************************************
 * Copyright (c) 2014 Davide Riccardo Caliendo .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package language.value;

import language.value.type.FloatType;

public class FloatValue extends IValue {
    private static final long serialVersionUID = 2L;
    private float value;
    
    public FloatValue(float v) {
        value = v;
        type = FloatType.getInstance();
    }
            
    public float getTerm() {
        return value;
    }
            
    @Override
    public void rename(String old_id, String new_id) {
    }
    
    @Override
    public String toString() {
        return "" + value;
    }     
    
    public IValue clone() {
        return new FloatValue(value);
    }
}
    
