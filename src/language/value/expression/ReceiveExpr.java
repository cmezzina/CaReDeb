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
import language.value.Receive;
import language.value.type.ReceiveType;
import language.value.type.Type;
import exception.CoercionException;
import exception.WrongIdException;

public class ReceiveExpr extends Expression {

    private static final long serialVersionUID = 2L;

    public ReceiveExpr(String v) {
        val = v;
    }
    
    @Override
    public IValue getValue(LambdaIdLookup<String, IValue> idl)
            throws WrongIdException, CoercionException {
        return new Receive(val);
    }

    @Override
    public Type getType() {
        return ReceiveType.getInstance();
    }

    @Override
    public ExprType getExprType() {
        return ExprType.RECEIVE;
    }

    @Override
    public void rename(String old_id, String new_id) {
        if (val.equals(old_id))
            val = new_id;
    }

    @Override
    public IValue clone() {
        return new ReceiveExpr(val);
    }

}
