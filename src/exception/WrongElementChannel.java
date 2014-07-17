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

/* Used for backward logic, stm is not needed as in common RuntimeException
 */
public class WrongElementChannel extends RuntimeException {

    private static final long serialVersionUID = 2L;
    HashMap<String, Integer> dependencies;

    public WrongElementChannel(String msg, HashMap<String, Integer> dependencies) {
        super(null, msg);
        msgPrefix = "Wrong element channel:";
        this.dependencies = dependencies;
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
}
