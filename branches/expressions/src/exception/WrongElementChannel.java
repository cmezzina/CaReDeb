/*******************************************************************************
 * Copyright (c) 2012 Claudio Antares Mezzina.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Claudio Antares Mezzina - initial API and implementation
 ******************************************************************************/
package exception;

import java.util.HashMap;

public class WrongElementChannel extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8770333969465630446L;
    String msg;
    HashMap<String, Integer> dependencies;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public HashMap<String, Integer> getDependencies() {
        return dependencies;
    }

    public void setDependencies(HashMap<String, Integer> dependencies) {
        this.dependencies = dependencies;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public WrongElementChannel(String msg, HashMap<String, Integer> dependencies) {
        super();
        this.msg = msg;
        this.dependencies = dependencies;
    }

}
