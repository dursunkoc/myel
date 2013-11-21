/**
 *
 */
package com.aric.myel.statements.operators;

import com.aric.myel.statements.components.ReturnValue;
import com.aric.myel.utils.TypeUtils;

/**
 * @author Dursun KOC
 *
 */
public class SmallerThanOrEqual extends UnaryOperator {

	private GreaterThan greaterThan;

	public SmallerThanOrEqual() {
		greaterThan = new GreaterThan();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.turkcelltech.comet.flow.operators.IOperator#compare(com.turkcelltech
	 * .comet.flow.engine.basicflow.node.components.ReturnValue,
	 * com.turkcelltech.comet.flow.engine.basicflow.node.components.ReturnValue)
	 */
	@Override
	public boolean compare(ReturnValue valueLeft, ReturnValue valueRight) {
		TypeUtils.validateTypes(valueLeft, valueRight);
		if (TypeUtils.checkNullValues(valueLeft, valueRight))
			return false;

		return !(greaterThan.compare(valueLeft, valueRight));
	}
}
