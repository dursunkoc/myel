package com.aric.myel.statements.operators;

import com.aric.myel.statements.components.ReturnValue;


public interface IOperator {
	/**
	 * compare valueLeft using valueRight
	 *
	 * @param valueLeft
	 * @param valueRight
	 * @return
	 */
	public boolean compare(ReturnValue valueLeft, ReturnValue valueRight);

	/**
	 * compare centerValue using valuLeft and valueRight
	 *
	 * @param valueLeft
	 * @param valueRight
	 * @return
	 */
	public boolean compare(ReturnValue valueLeft, ReturnValue valueRight,
			ReturnValue centerValue);
	
	
}
