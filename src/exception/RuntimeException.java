/*******************************************************************************
 * Copyright (c) 2014 Davide Riccardo Caliendo .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package exception;

import language.statement.IStatement;


/* Represents all runtime errors
 * 
 */
public class RuntimeException extends Exception {
    private static final long serialVersionUID = 2L;
    // similar to breakpoint exception
    private String msg;
    protected String msgPrefix;
    private IStatement stm;

    public RuntimeException(IStatement stm, String msg) {
        this.stm = stm;
        this.msg = msg;
        this.msgPrefix = "";
    }

    public String getMsg() {
        return msgPrefix + " " + msg;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public IStatement getStm() {
        return stm;
    }

    public void setStm(IStatement stm) {
        this.stm = stm;
    }
}
