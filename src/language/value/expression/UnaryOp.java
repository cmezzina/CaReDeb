/*******************************************************************************
 * Copyright (c) 2014 Davide Riccardo Caliendo .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package language.value.expression;

import exception.CoercionException;
import exception.WrongIdException;

import language.util.LambdaIdLookup;
import language.value.IValue;
import language.value.type.NumberProtocol;
import language.value.type.Type;

public class UnaryOp extends Expression {
    private static final long serialVersionUID = 2L;
    protected Expression v;
    protected UnaryOpType op;
    protected String ops;
    
    public UnaryOp(Expression a, UnaryOpType opp, String ops) {
        v = a;
        op = opp;
        this.ops = ops;
    }
    
    
    @Override
    public IValue getValue(LambdaIdLookup<String, IValue> idl) throws WrongIdException, CoercionException {
        Type tv;
        IValue vv;
        if (cached_ival != null)
            return cached_ival;
        vv = v.getValue(idl);
        tv = vv.getType();
        cached_ival = callOperator(vv, tv, op);
        return cached_ival;
    }

    protected IValue callOperator(IValue vv, Type tv, UnaryOpType op) {
        IValue res;
        res = null;
        if (tv instanceof NumberProtocol){
            switch (op) {
                case NEG:
                    res = ((NumberProtocol)tv).n_negative(vv);
                    break;
                case POS:
                    res = ((NumberProtocol)tv).n_positive(vv);
                    break;
                case INV:
                    res = ((NumberProtocol)tv).n_invert(vv);
                    break;
                default:
                    break;        
            }
        }
        return res;
    }


    @Override
    public ExprType getExprType() {
        return ExprType.OP;
    }

    @Override
    public void rename(String old_id, String new_id) {
        v.rename(old_id, new_id);
    }

    @Override
    public IValue clone() {
        return new UnaryOp((Expression)v.clone(), op, ops);
    }

    @Override
    public String toString() {
        return ops + " " + v.toString();
    }
}
