package com.aric.myel.statements.operators;

import com.aric.myel.statements.components.ReturnValue;
import com.aric.myel.utils.TypeUtils;

public class Equal extends UnaryOperator {
	@Override
	public boolean compare(ReturnValue valueLeft, ReturnValue valueRight) {
		TypeUtils.validateTypes(valueLeft, valueRight);

		if (TypeUtils.checkNullValues(valueLeft, valueRight)) {
			return valueLeft.getCastedValue() == valueRight.getCastedValue();
		}

		return (valueLeft.getCastedValue().equals(valueRight.getCastedValue()));
	}
}
