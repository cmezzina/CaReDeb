/*******************************************************************************
 * Copyright (c) 2014 Davide Riccardo Caliendo .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package language.value.type;

import language.value.IValue;
import language.util.Tuple;

public interface NumberProtocol{
    public IValue n_add(IValue a, IValue b);
    public IValue n_subtract(IValue a, IValue b);

    public IValue n_multiply(IValue a, IValue b);
    public IValue n_divide(IValue a, IValue b);

    public IValue n_divmod(IValue a, IValue b);
    
    public IValue n_and(IValue a, IValue b);
    public IValue n_or(IValue a, IValue b);
    public IValue n_negative(IValue a);
    public IValue n_positive(IValue a);
    public IValue n_invert(IValue a);
}
