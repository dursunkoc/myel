/**
 * 
 */
package com.aric.myel.symbols;

import com.aric.myel.Executable;
import com.aric.myel.SimpleExecutable;
import com.aric.myel.exceptions.InvalidStatementException;


/**
 * @author TTDKOC
 * 
 */
public abstract class Symbol {
	/**
	 * @return
	 */
	public abstract boolean isDecisionOperator();

	private String symbol;

	/**
	 * @param symbol
	 */
	public Symbol(String symbol) {
		this.symbol = symbol == null ? "" : symbol;
	}

	/**
	 * @param symbol
	 * @return
	 */
	public boolean isMe(String symbol) {
		return this.symbol.equalsIgnoreCase(symbol);
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public Executable operate(Executable lhs, Executable rhs) {
		Object rhsValue = ((SimpleExecutable) rhs).getValue();
		Object lhsValue = ((SimpleExecutable) lhs).getValue();
		if (isDate(lhsValue, rhsValue)) {
			return new SimpleExecutable(doDateOp(lhsValue, rhsValue));
		} else if (isString(lhsValue, rhsValue)) {
			return new SimpleExecutable(doStringOp(lhsValue, rhsValue));
		} else if (isNumeric(lhsValue, rhsValue)) {
			return new SimpleExecutable(doNumericOp(lhsValue, rhsValue));
		}
		throw new InvalidStatementException("Unknown Object types! this:'"
				+ rhs + "'; rhs:'" + rhs + "'");
	}

	/**
	 * @param lhsValue
	 * @param rhsValue
	 * @return
	 */
	protected abstract Object doNumericOp(Object lhsValue, Object rhsValue);

	/**
	 * @param lhsValue
	 * @param rhsValue
	 * @return
	 */
	protected abstract boolean isNumeric(Object lhsValue, Object rhsValue);

	/**
	 * @param lhsValue
	 * @param rhsValue
	 * @return
	 */
	protected abstract Object doStringOp(Object lhsValue, Object rhsValue);

	/**
	 * @param lhsValue
	 * @param rhsValue
	 * @return
	 */
	protected abstract boolean isString(Object lhsValue, Object rhsValue);

	/**
	 * @param lhsValue
	 * @param rhsValue
	 * @return
	 */
	protected abstract Object doDateOp(Object lhsValue, Object rhsValue);

	/**
	 * @param lhsValue
	 * @param rhsValue
	 * @return
	 */
	protected abstract boolean isDate(Object lhsValue, Object rhsValue);

}
