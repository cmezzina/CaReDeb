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

/* Used for backward logic, stm is not needed as in common RuntimeException
 */
public class ChildMissingException extends RuntimeException {

    private static final long serialVersionUID = 2L;
    String child;

    public ChildMissingException(String msg, String child) {
        super(null, msg);
        msgPrefix = "Child missing exception:";
        this.child = child;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
