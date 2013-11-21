package com.aric.myel.statements.operators;

import com.aric.myel.statements.components.ReturnValue;

public class NotEqual extends UnaryOperator {
	private Equal equal;

	public NotEqual() {
		equal = new Equal();
	}

	@Override
	public boolean compare(ReturnValue valueLeft, ReturnValue valueRight) {
		return !equal.compare(valueLeft, valueRight);
	}
}
