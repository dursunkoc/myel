/**
 *
 */
package com.aric.myel.statements;

import com.aric.myel.exceptions.InvalidStatementException;
import com.aric.myel.statements.components.ReturnValue;

/**
 * @author Dursun KOC
 *
 */
public class StatementResult {
	private ReturnValue result;
	private StatementReason reason;

	/**
	 * @param result
	 * @param reason
	 */
	private StatementResult(ReturnValue result, StatementReason reason) {
		if (result == null) {
			throw new RuntimeException(
					"A StatementResult cannot be built with a null result!");
		}
		this.result = result;
		this.reason = reason;
	}

	/**
	 * @return the result
	 */
	public ReturnValue getResult() {
		return this.result;
	}

	/**
	 * @return the reason
	 */
	public StatementReason getReason() {
		return this.reason;
	}

	/**
	 * @return
	 */
	public boolean isSuccessful() {
		Object objResult = this.result.getCastedValue();
		if (objResult instanceof Boolean) {
			Boolean bool = (Boolean) objResult;
			return bool;
		}
		throw new InvalidStatementException(
				"The type of statement-result should be Boolean!");
	}

	public static StatementResult resultForSimpleStatements(ReturnValue result) {
		return new StatementResult(result, StatementReason.EMPTY_REASON);
	}

	public static StatementResult resultForComplexStatements(
			ReturnValue result, StatementReason reason) {
		return new StatementResult(result, reason);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("result: " + this.result);
		if (this.reason != null)
			buffer.append("| reason:" + this.reason.toString());
		return buffer.toString();
	}

}
