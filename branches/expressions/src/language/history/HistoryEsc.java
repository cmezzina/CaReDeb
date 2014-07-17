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
package language.history;

public class HistoryEsc implements IHistory {

    @Override
    public HistoryType getType() {
        // TODO Auto-generated method stub
        return HistoryType.ESC;
    }

    @Override
    public String toString() {
        // return "esc";
        return "";
    }

    @Override
    public IHistory clone() {
        return new HistoryEsc();
    }

    @Override
    public int getInstruction() {
        // TODO Auto-generated method stub
        return 0;
    }
}
