/*******************************************************************************
 * Copyright (c) 2014 Davide Riccardo Caliendo .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package language.value.type;

import language.util.Tuple;
import language.value.IValue;

public class ReceiveType extends Type {
    private static Type instance = null;

    protected ReceiveType() {}
    
    public static Type getInstance() {
        if (instance == null) 
            instance = new ReceiveType();
        return instance;
    }

    @Override
    public int compare(IValue a, IValue b) {
        return -1;
    }

    @Override
    public Tuple<IValue, IValue> coerce(IValue a, IValue b) {
        return null;
    }
}