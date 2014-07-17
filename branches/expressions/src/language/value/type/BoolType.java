/*******************************************************************************
 * Copyright (c) 2014 Davide Riccardo Caliendo .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package language.value.type;

import language.value.IValue;
import language.value.BoolValue;

public class BoolType extends IntType {
    private static Type instance = null;

    protected BoolType() {}
    
    public static Type getInstance() {
        if (instance == null) 
            instance = new BoolType();
        return instance;
    }
    
    @Override
    public IValue n_and(IValue a, IValue b) {
        return new BoolValue(((BoolValue)a).getTerm() && ((BoolValue)b).getTerm());
    }

    @Override
    public IValue n_or(IValue a, IValue b) {
        return new BoolValue(((BoolValue)a).getTerm() || ((BoolValue)b).getTerm());
    }

    @Override
    public IValue n_negative(IValue a) {
        return null;
    }
    
    @Override
    public IValue n_positive(IValue a) {
        return null;
    }
    
    @Override
    public IValue n_invert(IValue a) {
        return new BoolValue(!((BoolValue)a).getTerm());
    }
    
    @Override
    public int compare(IValue a, IValue b) {
        boolean ia, ib;
        ia = ((BoolValue)a).getTerm();
        ib = ((BoolValue)b).getTerm();
        return ia == ib ? 0 : 1;
    }
}
