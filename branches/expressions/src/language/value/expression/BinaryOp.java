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

import language.statement.Nil;
import language.util.LambdaIdLookup;
import language.util.Tuple;
import language.value.BoolValue;
import language.value.IValue;
import language.value.type.NumberProtocol;
import language.value.type.SequenceProtocol;
import language.value.type.Type;
import language.value.expression.BinaryOpType;

public class BinaryOp extends Expression {
    private static final long serialVersionUID = 2L;
    protected Expression v, w;
    protected BinaryOpType op;
    protected String ops;
    
    public BinaryOp(Expression a, Expression b, BinaryOpType opp, String ops) {
        v = a;
        w = b;
        op = opp;
        this.ops = ops;
    }
    
    @Override
    public IValue getValue(LambdaIdLookup<String, IValue> idl) throws WrongIdException, CoercionException {
        Type tv, tw;
        IValue vv, vw;
        // Avoid double evaluation
        if (cached_ival != null)
            return cached_ival;
        vv = v.getValue(idl);
        vw = w.getValue(idl);
        if (vv == null || vw == null)
            return null;
        
        tv = vv.getType();
        tw = vw.getType();
        if (tv == null || tw == null)
            return null;
        if (tv != tw) {
            Tuple<IValue, IValue> vvw;
            vvw = tv.coerce(vv, vw);
            if (vvw == null)
                vvw = tw.coerce(vv, vw);
            if (vvw == null)
                throw new CoercionException(new Nil(), "Cannot coerce " + tv.toString() + " and " + tw.toString());
            vv = vvw.getFirst();
            vw = vvw.getSecond();
            tv = vv.getType();
            tw = vw.getType();
        }
        cached_ival = callOperator(vv, vw, tv, tw, op);
        return cached_ival;
    }

    protected IValue callOperator(IValue vv, IValue vw, Type tv, Type tw, BinaryOpType op) {
        IValue res;
        int order;
        res = null;
        switch (op) {
            case ADD:
                if (tv instanceof NumberProtocol) {
                    res = ((NumberProtocol)tv).n_add(vv, vw);
                    if (res == null && tw instanceof NumberProtocol)
                        res = ((NumberProtocol)tw).n_add(vv, vw);
                }
                if (res == null && tv instanceof SequenceProtocol){
                    res = ((SequenceProtocol)tv).s_concat(vv, vw);
                    if (res == null && tw instanceof SequenceProtocol)
                        res = ((SequenceProtocol)tw).s_concat(vv, vw);
                }
                break;
            case AND:
                if (tv instanceof NumberProtocol && tw instanceof NumberProtocol){
                    res = ((NumberProtocol)tv).n_and(vv, vw);
                    if (res == null)
                        res = ((NumberProtocol)tw).n_and(vv, vw);
                }
                break;
            case DIV:
                if (tv instanceof NumberProtocol && tw instanceof NumberProtocol){
                    res = ((NumberProtocol)tv).n_divide(vv, vw);
                    if (res == null)
                        res = ((NumberProtocol)tw).n_divide(vv, vw);
                }
                break;
            case EQ:
                order = tv.compare(vv, vw);
                res = new BoolValue(order == 0);
                break;
            case NEQ:
                order = tv.compare(vv, vw);
                res = new BoolValue(order != 0);
                break;
            case GT:
                order = tv.compare(vv, vw);
                res = new BoolValue(order > 0);
                break;
            case GTE:
                order = tv.compare(vv, vw);
                res = new BoolValue(order >= 0);
                break;
            case LT:
                order = tv.compare(vv, vw);
                res = new BoolValue(order < 0);
                break;
            case LTE:
                order = tv.compare(vv, vw);
                res = new BoolValue(order <= 0);
                break;
            case MOD:
                if (tv instanceof NumberProtocol && tw instanceof NumberProtocol){
                    res = ((NumberProtocol)tv).n_divmod(vv, vw);
                    if (res == null)
                        res = ((NumberProtocol)tw).n_divmod(vv, vw);
                }
                break;
            case MUL:
                if (tv instanceof NumberProtocol && tw instanceof NumberProtocol){
                    res = ((NumberProtocol)tv).n_multiply(vv, vw);
                    if (res == null)
                        res = ((NumberProtocol)tw).n_multiply(vv, vw);
                }
                break;
            case OR:
                if (tv instanceof NumberProtocol && tw instanceof NumberProtocol){    
                    res = ((NumberProtocol)tv).n_or(vv, vw);
                    if (res == null)
                        res = ((NumberProtocol)tw).n_or(vv, vw);
                }
                break;
            case SUB:
                if (tv instanceof NumberProtocol && tw instanceof NumberProtocol){    
                    res = ((NumberProtocol)tv).n_subtract(vv, vw);
                    if (res == null)
                        res = ((NumberProtocol)tw).n_subtract(vv, vw);
                }
                break;
            default:
                break;
            
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
        w.rename(old_id, new_id);
    }

    @Override
    public IValue clone() {
        return new BinaryOp((Expression)v.clone(), (Expression)w.clone(), op, ops);
    }

    @Override
    public String toString() {
        
        return v.toString() + " " + ops + " " + w.toString();
    }
}
