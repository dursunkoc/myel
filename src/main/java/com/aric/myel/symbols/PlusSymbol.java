/**
 * 
 */
package com.aric.myel.symbols;

import java.util.Date;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.aric.myel.SimpleStatementParser;
import com.aric.myel.exceptions.InvalidStatementException;

import freemarker.core.ArithmeticEngine;
import freemarker.template.TemplateException;

/**
 * @author Dursun KOC
 * 
 */
public class PlusSymbol extends Symbol {
	private static final Symbol instance = new PlusSymbol("+");
	private static final ArithmeticEngine.ConservativeEngine engine = new ArithmeticEngine.ConservativeEngine();
	
	private static final String _SHIFTTYPE_DAY    = "1";
	private static final String _SHIFTTYPE_HOUR   = "2";
	private static final String _SHIFTTYPE_MINUTE = "3";

	public static Symbol getInstance() {
		return instance;
	}

	/**
	 * @param symbolName
	 */
	private PlusSymbol(String symbolName) {
		super(symbolName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.symbols.Symbol#doNumericOp
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	protected Number doNumericOp(Object lhsValue, Object rhsValue) {
		Number result;
		try {
			result = engine.add((Number) lhsValue, (Number) rhsValue);
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.symbols.Symbol#doStringOp(
	 * java.lang.Object, java.lang.Object)
	 */
	@Override
	protected String doStringOp(Object lhsValue, Object rhsValue) {
		StringBuffer result = new StringBuffer(castToString(lhsValue));
		result.append(SimpleStatementParser._TOKEN_DELIM);
		result.append(castToString(rhsValue));
		return result.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.symbols.Symbol#doDateOp(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	protected Date doDateOp(Object lfsValue, Object rhsValue) {
		Date result = null;

		if (lfsValue instanceof Date) {
			result = doDateOp((Date)lfsValue, rhsValue);
		} 
		else if (rhsValue instanceof Date) {
			result = doDateOp(lfsValue, (Date)rhsValue);
		}
		else {
			throw new InvalidStatementException("LFS '" 	+ lfsValue 
											+ "' and RHS '" + rhsValue 
											+ "' does not fit Date operation!");
		}
		
		return result;
	}
	
	protected Date doDateOp(Date lfsValue, Object rhsValue) {
		Date result = null;

		if (rhsValue instanceof Number){
			result = addDays(lfsValue, (Number)rhsValue);
		}
		else if (rhsValue instanceof String){
			result = doDateOp(lfsValue, (String)rhsValue);
		}
		else {
			throw new InvalidStatementException("LFS '" 	+ lfsValue 
											+ "' and RHS '" + rhsValue 
											+ "' does not fit Date operation!");
		}
		
		return result;
	}
	
	protected Date doDateOp(Object lfsValue, Date rhsValue) {
		Date result = null;

		if (lfsValue instanceof Number){
			result = addDays(rhsValue, (Number)lfsValue);
		}
		else if (lfsValue instanceof String){
			result = doDateOp((String)lfsValue, rhsValue);
		}
		else {
			throw new InvalidStatementException("LFS '" 	+ lfsValue 
											+ "' and RHS '" + rhsValue 
											+ "' does not fit Date operation!");
		}
		
		return result;
	}
	
	protected Date doDateOp(Date lfsValue, String rhsValue) {
		Date result = null;
		
		String[] rhsValueArray = rhsValue.split("-");
		
		if (rhsValueArray.length != 2){
			throw new InvalidStatementException("LFS '" 	+ lfsValue 
											+ "' and RHS '" + rhsValue 
											+ "' does not fit Date operation!");
		}
		
		String shiftType = rhsValueArray[1];
		String value     = rhsValueArray[0];
		boolean isValueNumber = NumberUtils.isNumber(value);

		if (shiftType.equals(_SHIFTTYPE_DAY) && isValueNumber){
			result = addDays(lfsValue, NumberUtils.createNumber(value));
		}		
		else if (shiftType.equals(_SHIFTTYPE_HOUR) && isValueNumber){
			result = addHours(lfsValue, NumberUtils.createNumber(value));
		}		
		else if (shiftType.equals(_SHIFTTYPE_MINUTE) && isValueNumber){
			result = addMinutes(lfsValue, NumberUtils.createNumber(value));
		}
		else {
			throw new InvalidStatementException("LFS '" 	+ lfsValue 
											+ "' and RHS '" + rhsValue 
											+ "' does not fit Date operation!");
		}
		
		return result;
	}
	
	protected Date doDateOp(String lfsValue, Date rhsValue) {
		Date result = null;

		String[] lfsValueArray = lfsValue.split("-");
		
		if (lfsValueArray.length != 2){
			throw new InvalidStatementException("LFS '" 	+ lfsValue 
											+ "' and RHS '" + rhsValue 
											+ "' does not fit Date operation!");
		}
		
		String shiftType = lfsValueArray[1];
		String value     = lfsValueArray[0];
		boolean isValueNumber = NumberUtils.isNumber(value);

		if (shiftType.equals(_SHIFTTYPE_DAY) && isValueNumber){
			result = addDays(rhsValue, NumberUtils.createNumber(value));
		}		
		else if (shiftType.equals(_SHIFTTYPE_HOUR) && isValueNumber){
			result = addHours(rhsValue, NumberUtils.createNumber(value));
		}		
		else if (shiftType.equals(_SHIFTTYPE_MINUTE) && isValueNumber){
			result = addMinutes(rhsValue, NumberUtils.createNumber(value));
		}
		else {
			throw new InvalidStatementException("LFS '" 	+ lfsValue 
											+ "' and RHS '" + rhsValue 
											+ "' does not fit Date operation!");
		}
		
		return result;
	}

	/**
	 * @param date
	 * @param number
	 * @return
	 */
	private Date addDays(Date date, Number number) {
		Date result = DateUtils.addDays(date, number.intValue());
		return result;
	}
	/**
	 * @param date
	 * @param number
	 * @return
	 */
	private Date addHours(Date date, Number number) {
		Date result = DateUtils.addHours(date, number.intValue());
		return result;
	}
	
	/**
	 * @param date
	 * @param number
	 * @return
	 */
	private Date addMinutes(Date date, Number number) {
		Date result = DateUtils.addMinutes(date, number.intValue());
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

	/**
	 * At least one of the parameters should be string. <br>
	 * If the other one is not, it will converted into string by .toString()
	 * method
	 * 
	 * @param lhsValue
	 * @param rhsValue
	 * @return
	 */
	@Override
	protected boolean isString(Object lhsValue, Object rhsValue) {
		return (lhsValue instanceof String) || (rhsValue instanceof String);
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
		boolean isValueDate    = lhsValue instanceof Date;
		boolean isRhsDate      = rhsValue instanceof Date;
		boolean isValueNumeric = lhsValue instanceof Number;
		boolean isRhsNumeric   = rhsValue instanceof Number;
		boolean isValueString  = lhsValue instanceof String;
		boolean isRhsString    = rhsValue instanceof String;
		
		return ( isValueDate && (isRhsNumeric   || isRhsString ) ) 
			|| ( isRhsDate   && (isValueNumeric || isValueString) );
	}

	/**
	 * @param obj
	 * @return
	 */
	private String castToString(Object obj) {
		if (obj instanceof Date) {
			return SimpleStatementParser._DEFAULT_DATE_FORMAT.format(obj);
		} else {
			return obj.toString();
		}
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

}
