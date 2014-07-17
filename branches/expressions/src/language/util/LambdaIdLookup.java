/*******************************************************************************
 * Copyright (c) 2014 Davide Riccardo Caliendo .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package language.util;

import exception.WrongIdException;

/* Not really a lambda but follows that style of programming, serving as an 
 * anonymous class parent for id lookup in various expression evaluation
 * It is needed because the store is external of various classes and we need
 * to catch that context inside Expression evaluation methods.
 */
public abstract class LambdaIdLookup<T,U> {
    public abstract U caller(T id) throws WrongIdException;
}
