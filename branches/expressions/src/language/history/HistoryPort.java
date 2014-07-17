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

public class HistoryPort implements IHistory {

    private String port_name;

    public HistoryPort(String port_name) {
        super();
        this.port_name = port_name;
    }

    public String getPort_name() {
        return port_name;
    }

    public void setPort_name(String port_name) {
        this.port_name = port_name;
    }

    @Override
    public HistoryType getType() {
        return HistoryType.PORT;
    }

    @Override
    public String toString() {
        return "new port " + port_name;
    }

    @Override
    public IHistory clone() {
        return new HistoryPort(port_name);
    }

    @Override
    public int getInstruction() {
        // TODO Auto-generated method stub
        return 0;
    }
}
