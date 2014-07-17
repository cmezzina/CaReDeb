/*******************************************************************************
 * Copyright (c) 2014 Davide Riccardo Caliendo .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package exception;

import language.statement.IStatement;
import exception.ExpressionErrorException;

public class CoercionException extends ExpressionErrorException {

    private static final long serialVersionUID = 2L;

    public CoercionException(IStatement stm, String msg) {
        super(stm, msg);
        msgPrefix = "Coercion exception:";
    }
}