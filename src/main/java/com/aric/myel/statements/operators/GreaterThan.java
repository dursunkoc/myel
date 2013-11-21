package com.aric.myel.statements.operators;

import com.aric.myel.statements.components.ReturnValue;
import com.aric.myel.utils.TypeUtils;

public class GreaterThan extends UnaryOperator {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean compare(ReturnValue valueLeft, ReturnValue valueRight) {
		TypeUtils.validateTypes(valueLeft, valueRight);
		if (TypeUtils.checkNullValues(valueLeft, valueRight))
			return false;
		Comparable castedLeftValue = (Comparable) valueLeft.getCastedValue();
		Comparable castedRightValue = (Comparable) valueRight.getCastedValue();
		return (castedLeftValue.compareTo(castedRightValue) == 1);

	}

}
