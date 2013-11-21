package com.aric.myel.statements.operators;

import com.aric.myel.exceptions.InvalidTypeForOperatorException;
import com.aric.myel.statements.components.ReturnValue;
import com.aric.myel.statements.components.TYPE;
import com.aric.myel.utils.TypeUtils;

public class Like extends UnaryOperator {

	@Override
	public boolean compare(ReturnValue valueLeft, ReturnValue valueRight) {
		TypeUtils.validateTypes(valueLeft, valueRight);
		if (!valueLeft.getType().equals(TYPE.STRING) && !valueLeft.getType().equals(TYPE.NILL)) {
			throw new InvalidTypeForOperatorException(
					"LIKE operation can not be applied for '"
							+ valueLeft.getType() + "' type variables!");
		}
		if (valueLeft.getCastedValue() == null
				|| valueLeft.getCastedValue().toString().isEmpty()
				|| valueRight.getCastedValue() == null
				|| valueRight.getCastedValue().toString().isEmpty()) {
			return false;
		}
		String localLeftValue = (String) valueLeft.getCastedValue();
		String localRightValue = (String) valueRight.getCastedValue();
		localRightValue = localRightValue.replace("\\", "\\\\").replace("[",
				"\\[").replace("^", "\\^").replace("$", "\\$").replace(".",
				"\\.").replace("|", "\\|").replace("?", "\\?").replace("*",
				"\\*").replace("+", "\\+").replace("(", "\\(").replace(")",
				"\\)").replace("_", ".").replace("%", "(.*)");

		return localLeftValue.matches(localRightValue);
	}

	public void setDelimiter(String delimiter) {
	}
}
