/*******************************************************************************
 * Copyright (c) 2014 Davide Riccardo Caliendo .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package language.value;

import language.value.type.StringType;

public class StringValue extends IValue {

    private static final long serialVersionUID = 2L;
    private String value;
    
    public StringValue(String val) {
        value = val;
        type = StringType.getInstance();
    }

    @Override
    public void rename(String old_id, String new_id) {
    }    

    public String getTerm() {
        return value;
    }

    @Override
    public String toString() {
        return '"' + value + '"';
    }
    
    @Override
    public IValue clone() {
        return new StringValue(value);
    }
}
