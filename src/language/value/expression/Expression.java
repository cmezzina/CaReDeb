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
import language.value.IValue;
import language.value.expression.ExprType;
import language.value.type.Type;
import language.util.LambdaIdLookup;

public abstract class Expression extends IValue {
 
    private static final long serialVersionUID = 2L;
    /* the string value to represent
     */
    protected String val;
    protected IValue cached_ival;
    
    public Expression() {
        cached_ival = null;
    }
    public abstract IValue getValue(LambdaIdLookup<String, IValue> idl) throws WrongIdException, CoercionException;
    
    public abstract ExprType getExprType() ;
    
    @Override
    public Type getType() {
        if (cached_ival != null)
            return cached_ival.getType();
        
        return null;
    }
    
    public String toString() {
            return val;
    }
}