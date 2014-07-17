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
package language.statement;

import java.util.ArrayList;

public class Invoke implements IStatement {

    private String callee;
    private ArrayList<String> params;

    public String getCallee() {
        return callee;
    }

    public void setCallee(String callee) {
        this.callee = callee;
    }

    public ArrayList<String> getParams() {
        return params;
    }

    public void setParams(ArrayList<String> params) {
        this.params = params;
    }

    public Invoke(String callee, ArrayList<String> params) {
        super();
        this.callee = callee;
        this.params = params;
    }

    @Override
    public StatementType getType() {
        return StatementType.INVOKE;
    }

    @Override
    public String toString() {
        String ret = "{ " + callee;
        for (String var : params) {
            ret += " " + var;
        }
        return ret + " }";
    }

    @Override
    public void rename(String old_id, String new_id) {
        if (callee.equals(old_id)) {
            callee = new_id;
        }

        // should be better implemented
        while (params.contains(old_id)) {
            int index = params.indexOf(old_id);
            params.remove(index);
            params.add(index, new_id);
        }
        return;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Invoke clone() {
        return new Invoke(callee, (ArrayList<String>) params.clone());
    }
}
