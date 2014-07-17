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
import language.value.IntValue;

public class IntType extends Type implements NumberProtocol {
    private static Type instance = null;

    protected IntType() {}
    
    public static Type getInstance() {
        if (instance == null) 
            instance = new IntType();
        return instance;
    }
    
    @Override
    public IValue n_add(IValue a, IValue b) {
        return new IntValue(((IntValue)a).getTerm() + ((IntValue)b).getTerm()); 
    }

    @Override
    public IValue n_subtract(IValue a, IValue b) {
        return new IntValue(((IntValue)a).getTerm() - ((IntValue)b).getTerm());
    }

    @Override
    public IValue n_multiply(IValue a, IValue b) {
        return new IntValue(((IntValue)a).getTerm() * ((IntValue)b).getTerm());
    }

    @Override
    public IValue n_divide(IValue a, IValue b) {
        return new IntValue(((IntValue)a).getTerm() / ((IntValue)b).getTerm());
    }

    @Override
    public IValue n_divmod(IValue a, IValue b) {
        return new IntValue(((IntValue)a).getTerm() % ((IntValue)b).getTerm());
    }

    @Override
    public IValue n_and(IValue a, IValue b) {
        return new IntValue(((IntValue)a).getTerm() & ((IntValue)b).getTerm());
    }

    @Override
    public IValue n_or(IValue a, IValue b) {
        return new IntValue(((IntValue)a).getTerm() | ((IntValue)b).getTerm());
    }

    @Override
    public IValue n_negative(IValue a) {
        return new IntValue(-((IntValue)a).getTerm());
    }
    
    @Override
    public IValue n_positive(IValue a) {
        return new IntValue(((IntValue)a).getTerm());
    }

    @Override
    public IValue n_invert(IValue a) {
        return new IntValue(~((IntValue)a).getTerm());
    }
    
    @Override
    public int compare(IValue a, IValue b) {
        int ia, ib;
        ia = ((IntValue)a).getTerm();
        ib = ((IntValue)b).getTerm();
        return ia > ib ? 1 : ia == ib ? 0 : -1;
    }

    @Override
    public Tuple<IValue, IValue> coerce(IValue a, IValue b) {
        if (a.getType() == b.getType()) {
            return new Tuple<IValue, IValue>(a, b);
        }
        return null;
    }
}