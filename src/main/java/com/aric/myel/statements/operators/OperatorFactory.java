package com.aric.myel.statements.operators;

import com.aric.myel.exceptions.NoSuchOperatorException;

public class OperatorFactory {

	/**
	 *
	 * @param operator
	 * @return
	 */
	public static IOperator getOperator(String operator) {
		return getEOperator(operator).getiOperator();
	}

	/**
	 *
	 * @param operator
	 * @return
	 */
	public static EOperator getEOperator(String operator) {
		for (EOperator eOperator : EOperator.values()) {
			if (eOperator.isMe(operator)) {
				return eOperator;
			}
		}
		throw new NoSuchOperatorException("(" + operator
				+ ")No such IOperator implemented", operator);
	}
}
