/*******************************************************************************
 * Copyright (c) 2014 Davide Riccardo Caliendo .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package language.value.type;

import language.util.Tuple;
import language.value.IValue;

/* Various types must be designed as singletons implementing various protocols,
 * the singleton design is needed to have shared resources and a simple comparison
 * method among object types.
 * Type implements all the protocols, for example NumberProtocol defines n_add, 
 * n_sub ...
 * If a new type inheriting from Type does not implements a protocol it simply 
 * returns null from that particular method, look at BinaryOp how that is handled.
 * The type system implementation is loosely inspirated from python.
 */
public abstract class Type {

    // it is private only to force subclasses to redefine a static member like this
    private static Type instance = null;
    
    protected Type() {
        // avoid direct instantiation
    }
    
    /*
     * Child must write a singleton implementation of this method
     */
    public static Type getInstance() throws Exception{
        if (instance == null) throw new Exception("Type instance cannot be null");
        return instance;
    }
    
    public abstract int compare(IValue a, IValue b);
    
    public abstract Tuple<IValue, IValue> coerce(IValue a, IValue b);
}
