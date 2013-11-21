package com.aric.myel.statements.operators;

import com.aric.myel.statements.components.ReturnValue;
import com.aric.myel.utils.TypeUtils;

public class In extends UnaryOperator {

	@Override
	public boolean compare(ReturnValue valueLeft, ReturnValue valueRight) {
		if (valueRight.getType().isArray()) {
			TypeUtils.validateTypes(valueRight.getType().getBaseType(),
					valueLeft.getType());
		} else {
			TypeUtils.validateTypes(valueLeft, valueRight);
		}

		if (TypeUtils.checkNullValues(valueLeft, valueRight)) {
			for (int i = 0; i < valueRight.getListOfValues().size(); i++) {
				if (valueLeft.getCastedValue() == valueRight.getCastedValue(i))
					return true;
			}
		} else {
			for (int i = 0; i < valueRight.getListOfValues().size(); i++) {
				if (valueLeft.getCastedValue().equals(valueRight.getCastedValue(i)))
					return true;
			}
		}

		return false;
	}
}
