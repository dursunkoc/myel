package com.aric.myel.statements.operators;

import com.aric.myel.exceptions.InvalidTypeForOperatorException;
import com.aric.myel.statements.components.ReturnValue;
import com.aric.myel.statements.components.TYPE;
import com.aric.myel.utils.TypeUtils;

/**
 * @author TCDUKOC
 *
 */
public class Or extends UnaryOperator {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.turkcelltech.comet.flow.operators.IOperator#compare(com.turkcelltech
	 * .comet.flow.engine.basicflow.domain .ReturnValue,
	 * com.turkcelltech.comet.flow.engine.basicflow.domain.ReturnValue)
	 */
	@Override
	public boolean compare(ReturnValue valueLeft, ReturnValue valueRight) {
		TypeUtils.validateTypes(valueLeft, valueRight);

		if (TypeUtils.checkNullValues(valueLeft, valueRight))
			return false;

		if (valueLeft.getType().equals(TYPE.BOOLEAN)) {
			return (Boolean) valueRight.getCastedValue()
					|| (Boolean) valueLeft.getCastedValue();
		}
		throw new InvalidTypeForOperatorException("Type not applicable:"
				+ valueLeft.getType());
	}

}
