package com.aric.myel.statements.components;

import java.io.Serializable;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import com.aric.myel.exceptions.TypeCastException;
import com.aric.myel.exceptions.TypeInitializationException;
import com.aric.myel.utils.StringUtils;

/**
 * 
 * @author TCDUKOC
 * 
 */
public class TYPE implements Cloneable, Serializable {

	private static final long serialVersionUID = 5383428103808246575L;

	private final static int _nill = 0;
	private final static int _numeric = 1;
	private final static int _string = 2;
	private final static int _date = 3;
	private final static int _array_of_numeric = 4;
	private final static int _array_of_string = 5;
	private final static int _array_of_date = 6;
	private final static int _boolean = 7;
	private final static int _array_of_boolean = 8;
	private final static int _double = 9;
	private final static int _array_of_double = 10;
	private final static int _AVALIABLETYPEID[] = new int[] { _nill, _numeric,
			_string, _date, _array_of_numeric, _array_of_string,
			_array_of_date, _boolean, _array_of_boolean, _double,
			_array_of_double };
	/*
	 * Following constants are stick to code so should not be in the
	 * FlowConstants
	 */

	public final static TYPE NILL = new TYPE(_nill);
	public final static TYPE NUMERIC = new TYPE(_numeric);
	public final static TYPE STRING = new TYPE(_string);
	public final static TYPE DATE = new TYPE(_date);
	public final static TYPE ARRAY_OF_NUMERIC = new TYPE(_array_of_numeric);
	public final static TYPE ARRAY_OF_STRING = new TYPE(_array_of_string);
	public final static TYPE ARRAY_OF_DATE = new TYPE(_array_of_date);
	public final static TYPE BOOLEAN = new TYPE(_boolean);
	public final static TYPE ARRAY_OF_BOOLEAN = new TYPE(_array_of_boolean);
	public final static TYPE DOUBLE = new TYPE(_double);
	public final static TYPE ARRAY_OF_DOUBLE = new TYPE(_array_of_double);
	public final static String LONGTODATEFORMAT = "9...";

	private int type;

	/**
	 * 
	 * @return
	 */
	public TYPE getBaseType() {
		if (this.equals(ARRAY_OF_NUMERIC)) {
			return NUMERIC;
		} else if (this.equals(ARRAY_OF_DATE)) {
			return DATE;
		} else if (this.equals(ARRAY_OF_DOUBLE)) {
			return DOUBLE;
		} else if (this.equals(ARRAY_OF_BOOLEAN)) {
			return BOOLEAN;
		} else if (this.equals(ARRAY_OF_STRING)) {
			return STRING;
		} else {
			return NILL;
		}
	}

	/**
	 * 
	 * @param type
	 */
	public TYPE(int type) {
		this.type = type;
		if (Arrays.binarySearch(_AVALIABLETYPEID, type) < 0) {
			throw new TypeInitializationException("Unknown TYPE value:" + type);
		}
	}

	/**
	 * 
	 * @return
	 */
	public int getTypeId() {
		return this.type;
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public static TYPE getType(int type) {
		switch (type) {
		case _nill:
			return NILL;
		case _numeric:
			return NUMERIC;
		case _string:
			return STRING;
		case _date:
			return DATE;
		case _array_of_numeric:
			return ARRAY_OF_NUMERIC;
		case _array_of_string:
			return ARRAY_OF_STRING;
		case _array_of_date:
			return ARRAY_OF_DATE;
		case _boolean:
			return BOOLEAN;
		case _array_of_boolean:
			return ARRAY_OF_BOOLEAN;
		case _double:
			return DOUBLE;
		case _array_of_double:
			return ARRAY_OF_DOUBLE;
		default:
			throw new InvalidTypeException("Unknown TYPE value:" + type);
		}
	}

	/**
	 * 
	 * @return
	 */
	public TYPE getType() {
		return TYPE.getType(this.type);
	}

	/**
	 * 
	 * @return
	 */
	private int getTypeValue() {
		return this.type;
	}

	@Override
	public String toString() {
		switch (getTypeValue()) {
		case _nill:
			return "NILL";
		case _numeric:
			return "NUMERIC";
		case _string:
			return "STRING";
		case _date:
			return "DATE";
		case _array_of_numeric:
			return "ARRAY_OF_NUMERIC";
		case _array_of_string:
			return "ARRAY_OF_STRING";
		case _array_of_date:
			return "ARRAY_OF_DATE";
		case _boolean:
			return "BOOLEAN";
		case _array_of_boolean:
			return "ARRAY_OF_BOOLEAN";
		case _double:
			return "DOUBLE";
		case _array_of_double:
			return "ARRAY_OF_DOUBLE";
		default:
			throw new InvalidTypeException("Unknown TYPE value:" + type);
		}
	}

	@Override
	public boolean equals(Object obj) {
		TYPE _type = (TYPE) obj;
		return _type.getTypeValue() == this.getTypeValue();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isArray() {
		switch (getTypeValue()) {
		case _nill:
		case _numeric:
		case _string:
		case _date:
		case _boolean:
		case _double:
			return false;
		case _array_of_boolean:
		case _array_of_date:
		case _array_of_double:
		case _array_of_numeric:
		case _array_of_string:
			return true;
		default:
			throw new InvalidTypeException("Unknown TYPE value:" + type);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		TYPE clone = (TYPE) super.clone();
		return clone;
	}

	/**
	 * @param refValue
	 * @param refFormat
	 * @param type
	 * @return
	 */
	public static Object castValue(Object refValue, String refFormat, int type) {
		TYPE t = getType(type);
		return t.castValue(refValue, refFormat);
	}

	/**
	 * @param refValue
	 * @param refFormat
	 * @return
	 * @throws TypeCastException
	 * @throws InvalidTypeException
	 */
	public Object castValue(Object refValue, String refFormat)
			throws TypeCastException, InvalidTypeException {
		String stringValue = getStringValue(refValue);
		if (stringValue == null) {
			return null;
		}
		TYPE refType = getRefType();
		switch (refType.getTypeId()) {
		case _boolean:
			return getBooleanValue(stringValue);
		case _date:
			return getDateValue(stringValue, refFormat);
		case _numeric:
			return getNumericValue(stringValue);
		case _double:
			return getDoubleValue(stringValue);
		case _string:
			return stringValue;
		default:
			throw new InvalidTypeException("Unknown TYPE value:" + type);
		}
	}

	private String getStringValue(Object refValue) {
		if (refValue == null) {
			return null;
		}
		String stringValue = refValue.toString();
		if (StringUtils.isStringNull(stringValue)) {
			return null;
		}
		return stringValue;
	}

	/**
	 * @return
	 */
	private TYPE getRefType() {
		TYPE refType;
		if (isArray()) {
			refType = this.getBaseType();
		} else {
			refType = this.getType();
		}
		return refType;
	}

	/**
	 * @param refValue
	 * @return
	 */
	private Double getDoubleValue(String refValue) {
		try {
			return Double.parseDouble(refValue);
		} catch (Exception e) {
			throw new TypeCastException("Casting (value='" + refValue
					+ ") into double failed!", e);
		}
	}

	/**
	 * @param refValue
	 * @return
	 */
	private Object getNumericValue(String refValue) {
		try {
			return Long.parseLong(refValue);
		} catch (Exception e) {
			throw new TypeCastException("Casting (value='" + refValue
					+ ") into number failed!", e);
		}
	}

	/**
	 * @param refValue
	 * @param refFormat
	 * @return
	 * @throws ParseException
	 */
	private Object getDateValue(String refValue, String refFormat) {
		try {
			if (refFormat.equals(LONGTODATEFORMAT)) {
				return new Date(Long.parseLong(refValue));
			} else {
				SimpleDateFormat dateFormat = new SimpleDateFormat(refFormat);
				return dateFormat.parseObject(refValue);
			}
		} catch (Exception e) {
			throw new TypeCastException("Casting (value='" + refValue
					+ "' using format='" + refFormat + "') into date failed!",
					e);
		}
	}

	/**
	 * @param refValue
	 * @return
	 */
	private Object getBooleanValue(String refValue) {
		if (refValue.equalsIgnoreCase("true")) {
			return new Boolean(true);
		} else if (refValue.equalsIgnoreCase("false")) {
			return new Boolean(false);
		} else {
			throw new TypeCastException("Casting (value='" + refValue
					+ "') into Boolean failed!");
		}
	}

	/**
	 * @return
	 */
	public int toSqlType() {
		switch (getTypeValue()) {
		case _numeric:
		case _double:
			return Types.NUMERIC;
		case _string:
			return Types.VARCHAR;
		case _date:
			return Types.DATE;
		case _boolean:
			return Types.BOOLEAN;
		case _array_of_boolean:
		case _array_of_date:
		case _array_of_double:
		case _array_of_numeric:
		case _array_of_string:
		case _nill:
		default:
			throw new InvalidTypeException("TYPE (typeId:" + type
					+ ") cannot be mapped to sqlType!");
		}
	}
}