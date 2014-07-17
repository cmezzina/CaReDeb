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

public interface SequenceProtocol {
    public IValue s_concat(IValue a, IValue b);
    public int s_length(IValue a);
}
