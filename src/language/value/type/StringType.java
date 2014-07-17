/*******************************************************************************
 * Copyright (c) 2014 Davide Riccardo Caliendo .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package language.value.type;

import language.util.Tuple;
import language.value.FloatValue;
import language.value.IValue;
import language.value.IntValue;
import language.value.StringValue;

public class StringType extends Type implements SequenceProtocol {
    private static Type instance = null;

    protected StringType() {}
    
    public static Type getInstance() {
        if (instance == null) {
            instance = new StringType();
        }
        return instance;
    }

    @Override
    public IValue s_concat(IValue a, IValue b) {
        String sa, sb;
        sa = ((StringValue)a).getTerm();
        sb = ((StringValue)b).getTerm();
        return new StringValue(sa + sb); 
    }

    @Override
    public int s_length(IValue a) {
        return ((StringValue)a).getTerm().length();
    }
    
    @Override
    public int compare(IValue a, IValue b) {
        String s1, s2;
        s1 = ((StringValue)a).getTerm();
        s2 = ((StringValue)b).getTerm();
        return s1.equals(s2) ? 0 : s1.length() > s2.length() ? 1 : 0;
    }
    
    @Override
    public Tuple<IValue, IValue> coerce(IValue a, IValue b) {
        if (a.getType() == b.getType()) {
            return new Tuple<IValue, IValue>(a, b);
        }
        if (b.getType() == IntType.getInstance()) {
            return new Tuple<IValue, IValue>(a, new StringValue(Integer.toString(((IntValue)b).getTerm())));
        }
        if (b.getType() == FloatType.getInstance()) {
            return new Tuple<IValue, IValue>(a, new StringValue(Float.toString(((FloatValue)b).getTerm())));
        }
        if (a.getType() == IntType.getInstance()) {
            return new Tuple<IValue, IValue>(new StringValue(Integer.toString(((IntValue)a).getTerm())), b);
        }
        if (a.getType() == FloatType.getInstance()) {
            return new Tuple<IValue, IValue>(new StringValue(Float.toString(((FloatValue)a).getTerm())), b);
        }
        return null;
    }
}
