/**
 *
 */
package com.aric.myel.statements.operators;

import com.aric.myel.exceptions.InvalidTypeForOperatorException;
import com.aric.myel.statements.components.ReturnValue;


/**
 * @author Dursun KOC
 *
 */
public abstract class UnaryOperator implements IOperator {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.turkcelltech.comet.flow.operators.IOperator#compare(com.turkcelltech
	 * .comet.flow.engine.components.ReturnValue,
	 * com.turkcelltech.comet.flow.engine.components.ReturnValue)
	 */
	@Override
	public abstract boolean compare(ReturnValue valueLeft, ReturnValue valueRight);

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.turkcelltech.comet.flow.operators.IOperator#compare(com.turkcelltech
	 * .comet.flow.engine.components.ReturnValue,
	 * com.turkcelltech.comet.flow.engine.components.ReturnValue,
	 * com.turkcelltech.comet.flow.engine.components.ReturnValue)
	 */
	@Override
	public boolean compare(ReturnValue valueLeft, ReturnValue valueRight,
			ReturnValue centerValue) {
		throw new InvalidTypeForOperatorException(
				"Two value Comparesion is not supported!");
	}

}
