package com.aric.myel.statements.operators;

import com.aric.myel.exceptions.InvalidTypeForOperatorException;
import com.aric.myel.statements.components.ReturnValue;
import com.aric.myel.utils.TypeUtils;


/**
 *
 * @author TCDUKOC
 *
 */
public class Contains extends UnaryOperator {
	private In in;

	public Contains() {
		in = new In();
	}

	@Override
	public boolean compare(ReturnValue valueLeft, ReturnValue valueRight) {
		if (!valueLeft.getType().isArray()) {
			throw new InvalidTypeForOperatorException("Type not applicable:"
					+ valueLeft.getType() + ". It should be an Array");
		}
		TypeUtils.validateTypes(valueLeft.getType().getBaseType(),
				valueRight.getType());
		if (valueLeft.getCastedValue() == null
				|| valueRight.getCastedValue() == null) {
			return false;
		}
		return in.compare(valueRight, valueLeft);
	}

}
