package com.aric.myel.statements.operators;

import com.aric.myel.statements.components.ReturnValue;

public class NotIn extends UnaryOperator {

	private In in;

	public NotIn() {
		in = new In();
	}

	@Override
	public boolean compare(ReturnValue valueLeft, ReturnValue valueRight) {
		return !in.compare(valueLeft, valueRight);

	}
}
