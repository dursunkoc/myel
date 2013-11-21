/**
 * 
 */
package com.aric.myel.symbols;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.aric.myel.exceptions.InvalidStatementException;

import freemarker.core.ArithmeticEngine;
import freemarker.template.TemplateException;

/**
 * @author Dursun KOC
 * 
 */
public class MinusSymbol extends Symbol {
	private static final Symbol instance = new MinusSymbol("-");
	private static final ArithmeticEngine.ConservativeEngine engine = new ArithmeticEngine.ConservativeEngine();

	/**
	 * @return
	 */
	public static Symbol getInstance() {
		return instance;
	}

	/**
	 * @param symbolName
	 */
	private MinusSymbol(String symbolName) {
		super(symbolName);
	}

	@Override
	public boolean isDecisionOperator() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.symbols.Symbol#doNumericOp
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	protected Object doNumericOp(Object lhsValue, Object rhsValue) {
		Number result;
		try {
			result = engine.subtract((Number) lhsValue, (Number) rhsValue);
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * Both of the parameters should be Numeric.
	 * 
	 * @param lhsValue
	 * @param rhsValue
	 * @return
	 */
	@Override
	protected boolean isNumeric(Object lhsValue, Object rhsValue) {
		return (lhsValue instanceof Number) && (rhsValue instanceof Number);
	}

	@Override
	protected Object doStringOp(Object lhsValue, Object rhsValue) {
		throw new InvalidStatementException(
				"Subtraction operation is not avaliable for String objects.");
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
		if (lhsValue instanceof Date && rhsValue instanceof Number) {
			return subtractDays((Date) lhsValue, (Number) rhsValue);
		} else if (lhsValue instanceof Number && rhsValue instanceof Date) {
			return subtractDays((Date) rhsValue, (Number) lhsValue);
		}
		throw new InvalidStatementException("LHS '" + lhsValue + "' and RHS '"
				+ rhsValue + "' does not fit Date operation!");
	}

	/**
	 * @param date
	 * @param number
	 * @return
	 */
	private Date subtractDays(Date date, Number number) {
		Date result = DateUtils.addDays(date, -number.intValue());
		return result;
	}

	/**
	 * Only one of the parameters should be date, the other one should be
	 * numeric.
	 * 
	 * @param lhsValue
	 * @param rhsValue
	 * @return
	 */
	@Override
	protected boolean isDate(Object lhsValue, Object rhsValue) {
		boolean isValueDate = lhsValue instanceof Date;
		boolean isRhsDate = rhsValue instanceof Date;
		boolean isValueNumeric = lhsValue instanceof Number;
		boolean isRhsNumeric = rhsValue instanceof Number;
		return (isValueDate && isRhsNumeric) || (isRhsDate && isValueNumeric);
	}

}
