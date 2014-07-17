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

public class HistoryReceive implements IHistory {

    private String from;
    private String var;

    private int instruction;

    public HistoryReceive(String from, String var, int instruction) {
        super();
        this.from = from;
        this.var = var;
        this.instruction = instruction;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    @Override
    public int getInstruction() {
        return instruction;
    }

    public void setInstruction(int instruction) {
        this.instruction = instruction;
    }

    @Override
    public HistoryType getType() {
        return HistoryType.RECEIVE;
    }

    @Override
    public String toString() {
        return "read in " + var + " from " + from;
    }

    @Override
    public IHistory clone() {
        return new HistoryReceive(from, var, instruction);
    }
}
