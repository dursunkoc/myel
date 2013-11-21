/**
 * 
 */
package com.aric.myel.symbols;

import java.util.Date;

import com.aric.myel.exceptions.InvalidStatementException;

import freemarker.core.ArithmeticEngine;
import freemarker.template.TemplateException;

/**
 * @author Dursun KOC
 * 
 */
public class MultiplicationSymbol extends Symbol {
	private static final Symbol instance = new MultiplicationSymbol("x");
	private static final ArithmeticEngine.ConservativeEngine engine = new ArithmeticEngine.ConservativeEngine();

	public static Symbol getInstance() {
		return instance;
	}

	/**
	 * @param symbolName
	 */
	private MultiplicationSymbol(String symbolName) {
		super(symbolName);
	}

	@Override
	public boolean isDecisionOperator() {
		return false;
	}

	@Override
	protected Object doNumericOp(Object lhsValue, Object rhsValue) {
		Number result;
		try {
			result = engine.multiply((Number) lhsValue, (Number) rhsValue);
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * both lhs and rhs should be numeric.
	 * 
	 * @see com.turkcelltech.comet.commonutil.utils.el.symbols.Symbol#isNumeric(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	protected boolean isNumeric(Object lhsValue, Object rhsValue) {
		return lhsValue instanceof Number && rhsValue instanceof Number;
	}

	@Override
	protected Object doStringOp(Object lhsValue, Object rhsValue) {
		throw new com.aric.myel.exceptions.InvalidStatementException(
				"Multiplication operation is not avaliable for String objects.");
	}

	/**
	 * any of lhs or rhs may be String.
	 * 
	 * @see com.turkcelltech.comet.commonutil.utils.el.symbols.Symbol#isString(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	protected boolean isString(Object lhsValue, Object rhsValue) {
		return lhsValue instanceof String || rhsValue instanceof String;
	}

	@Override
	protected Object doDateOp(Object lhsValue, Object rhsValue) {
		throw new InvalidStatementException(
				"Multiplication operation is not avaliable for Date objects.");
	}

	@Override
	protected boolean isDate(Object lhsValue, Object rhsValue) {
		return lhsValue instanceof Date || rhsValue instanceof Date;
	}

}
