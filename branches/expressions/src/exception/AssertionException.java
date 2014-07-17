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

package exception;

import language.statement.IStatement;

public class AssertionException extends RuntimeException {

    public AssertionException(IStatement stm, String msg) {
        super(stm, msg);
        msgPrefix = "Assertion exception:";
    }
}