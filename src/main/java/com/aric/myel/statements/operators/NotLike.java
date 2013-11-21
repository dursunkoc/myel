package com.aric.myel.statements.operators;

import com.aric.myel.statements.components.ReturnValue;

public class NotLike extends UnaryOperator {
	private Like like;

	public NotLike() {
		like = new Like();
	}

	public boolean compare(ReturnValue valueLeft, ReturnValue valueRight) {
		return !like.compare(valueLeft, valueRight);
	}

	public void setDelimiter(String delimiter) {
	}
}
