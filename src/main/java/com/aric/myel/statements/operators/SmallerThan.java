package com.aric.myel.statements.operators;


import com.aric.myel.statements.components.ReturnValue;
import com.aric.myel.utils.TypeUtils;

/**
 *
 * @author TCDUKOC
 *
 */
public class SmallerThan extends UnaryOperator {

	private GreaterThan greaterThan;
	private Equal equal;

	public SmallerThan() {
		greaterThan = new GreaterThan();
		equal = new Equal();
	}

	@Override
	public boolean compare(ReturnValue valueLeft, ReturnValue valueRight) {
		TypeUtils.validateTypes(valueLeft, valueRight);
		if (TypeUtils.checkNullValues(valueLeft, valueRight))
			return false;

		return !(greaterThan.compare(valueLeft, valueRight)||equal.compare(valueLeft, valueRight));
	}

}
