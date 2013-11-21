/**
 * 
 */
package com.aric.myel.symbols;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aric.myel.Config;
import com.aric.myel.exceptions.InvalidStatementException;

/**
 * @author TTDKOC
 * 
 */
public class FormatToSymbol extends Symbol {
	private static final Symbol instance = new FormatToSymbol("formatTo");

	/**
	 * @return
	 */
	public static Symbol getInstance() {
		return instance;
	}

	/**
	 * @param symbolName
	 */
	private FormatToSymbol(String symbolName) {
		super(symbolName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.symbols.Symbol#isDecisionOperator
	 * ()
	 */
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
		throw new InvalidStatementException(
				"FormatTo operation is not avaliable for numeric objects.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.symbols.Symbol#isNumeric(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	protected boolean isNumeric(Object lhsValue, Object rhsValue) {
		return (lhsValue instanceof Number || rhsValue instanceof Number);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.symbols.Symbol#doStringOp(
	 * java.lang.Object, java.lang.Object)
	 */
	@Override
	protected Object doStringOp(Object lhsValue, Object rhsValue) {
		try {
			String lhsValueStr = lhsValue.toString();
			SimpleDateFormat dateFormatForlhs;
			//Eger systemdateFormat ile formatlanmisa onu kullaniyoruz,
			if (lhsValueStr.trim().length() <= Config.SYSTEM_DATE_FORMAT
					.length()) {
				dateFormatForlhs = new SimpleDateFormat(
						Config.SYSTEM_DATE_FORMAT);
			} else {
			//Eger systemdateFormatLong ile formatlanmisa onu kullaniyoruz,				
				dateFormatForlhs = new SimpleDateFormat(
						Config.SYSTEM_DATE_FORMAT_LONG);
			}

			Date date = dateFormatForlhs.parse(lhsValueStr);
			return doDateOp(date, rhsValue);
		} catch (ParseException e) {
			throw new InvalidStatementException(e);
		}

		// throw new
		// InvalidStatementException("FormatTo operation is not avaliable for String objects.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.symbols.Symbol#isString(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	protected boolean isString(Object lhsValue, Object rhsValue) {
		return (lhsValue instanceof String);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.symbols.Symbol#doDateOp(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	protected Object doDateOp(Object lhsValue, Object rhsValue) {
		Date date = ((Date) lhsValue);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				rhsValue.toString());
		return simpleDateFormat.format(date);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.symbols.Symbol#isDate(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	protected boolean isDate(Object lhsValue, Object rhsValue) {
		return (lhsValue instanceof Date && rhsValue instanceof String);
	}
}
