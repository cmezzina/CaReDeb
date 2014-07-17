/*******************************************************************************
 * Copyright (c) 2014 Davide Riccardo Caliendo .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package language.value.expression;

import language.util.LambdaIdLookup;
import language.value.IValue;
import language.value.IntValue;
import language.value.type.IntType;
import language.value.type.Type;

public class IntConst extends Expression {

    private static final long serialVersionUID = 2L;
    
    public IntConst(String v) {
        val = v;
    }
    
    @Override
    public Type getType() {
        return IntType.getInstance();
    }

    @Override
    public void rename(String old_id, String new_id) {
    }

    @Override
    public IValue getValue(LambdaIdLookup<String, IValue> idl) {
        return new IntValue(Integer.parseInt(val));
    }

    @Override
    public ExprType getExprType() {
        return ExprType.CONST;
    }
    
    @Override
    public IValue clone() {
        return new IntConst(val);
    }
}