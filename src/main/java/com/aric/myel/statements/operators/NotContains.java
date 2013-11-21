package com.aric.myel.statements.operators;

import com.aric.myel.exceptions.InvalidTypeForOperatorException;
import com.aric.myel.statements.components.ReturnValue;
import com.aric.myel.utils.TypeUtils;


/**
 *
 * @author TCDUKOC
 *
 */
public class NotContains extends UnaryOperator {
	private Contains contains;

	public NotContains() {
		contains = new Contains();
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
		return !contains.compare(valueLeft, valueRight);
	}

}
