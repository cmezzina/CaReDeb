/*******************************************************************************
 * Copyright (c) 2012 Claudio Antares Mezzina.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Claudio Antares Mezzina - initial API and implementation
 *     Davide Riccardo Caliendo - implementation
 ******************************************************************************/
package language.value.expression;

import exception.CoercionException;
import exception.WrongIdException;
import language.util.LambdaIdLookup;
import language.value.IValue;
import language.value.type.Type;

public class SimpleId extends Expression {

    private static final long serialVersionUID = 2L;

    public String getId() {
        return val;
    }

    public void setId(String id) {
        this.val = id;
    }

    public SimpleId(String id) {
        super();
        this.val = id;
    }

    @Override
    public void rename(String old_id, String new_id) {
        if (this.val.equals(old_id)) {
            this.val = new_id;
        }
    }

    @Override
    public ExprType getExprType() {
        return ExprType.ID;
    }
    
    @Override
    public SimpleId clone() {
        return new SimpleId(val);
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public IValue getValue(LambdaIdLookup<String, IValue> idl) throws WrongIdException, CoercionException {
        if (cached_ival != null)
            return cached_ival;
        cached_ival = (IValue) idl.caller(val);
        return cached_ival;
    }
}