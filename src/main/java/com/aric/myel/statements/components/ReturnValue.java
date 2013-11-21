package com.aric.myel.statements.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.math.NumberUtils;

import com.aric.myel.Config;
import com.aric.myel.exceptions.IllegalObjectTypeForReturnTypeValue;
import com.aric.myel.statements.Statement;
import com.aric.myel.statements.operators.EOperator;
import com.aric.myel.statements.operators.IOperator;
import com.aric.myel.utils.TypeUtils;

/**
 * Facilitates messaging between nodes of a flow.
 * <p>
 * Has seven TYPEs ;
 * <p>
 * <ul>
 * <li>STRING</li>
 * <li>NUMERIC</li>
 * <li>DATE</li>
 * <li>ARRAY_OF_STRING</li>
 * <li>ARRAY_OF_NUMERIC</li>
 * <li>ARRAY_OF_DATE</li>
 * <li>BOOLEAN</li>
 * <li>ARRAY_OF_BOOLEAN</li>
 * <li>DOUBLE</li>
 * <li>ARRAY_OF_DOUBLE</li>
 * </ul>
 * 
 * @author TCDUKOC
 * 
 */
public final class ReturnValue implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8213828869431167625L;
	private static final Character _MAJORDELIMITER = Statement._MAJORDELIMITER;
	private static final Character HASH_CONST = 31;

	private List<String> value;
	private TYPE type;
	private String format;
	public static final ReturnValue BOOL_TRUE = new ReturnValue(true,
			TYPE.BOOLEAN);
	public static final ReturnValue BOOL_FALSE = new ReturnValue(false,
			TYPE.BOOLEAN);

	/**
	 * 
	 * @param value
	 * @param type
	 * @param format
	 * @throws IllegalObjectTypeForReturnTypeValue
	 */
	public ReturnValue(String value, TYPE type, String format) {
		setFormat(format);
		setType(type);
		setValue(value);
	}

	/**
	 * 
	 * @param value
	 * @param type
	 * @param format
	 * @throws IllegalObjectTypeForReturnTypeValue
	 */
	public ReturnValue(Object value, TYPE type, String format) {
		setFormat(format);
		setType(type);
		setValue(value);
	}

	/**
	 * 
	 * @param value
	 * @param type
	 * @param format
	 * @throws IllegalObjectTypeForReturnTypeValue
	 */
	public ReturnValue(List<Object> value, TYPE type, String format) {
		setFormat(format);
		setType(type);
		setValue(value);
	}

	/**
	 * 
	 * @param value
	 * @param type
	 * @throws IllegalObjectTypeForReturnTypeValue
	 */
	public ReturnValue(String value, TYPE type) {
		this(value, type, null);
	}

	/**
	 * 
	 * @param value
	 * @param type
	 * @throws IllegalObjectTypeForReturnTypeValue
	 */
	public ReturnValue(Object value, TYPE type) {
		this(value, type, null);
	}

	/**
	 * 
	 * @param value
	 * @param type
	 * @throws IllegalObjectTypeForReturnTypeValue
	 */
	public ReturnValue(List<Object> value, TYPE type) {
		this(value, type, null);
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getListOfValues() {
		return value;
	}

	/**
	 * 
	 * @return
	 */
	protected Object getValue() {
		if (value != null && value.size() != 0) {
			return value.get(0);
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public Object getCastedValue() {
		if (value != null && value.size() != 0) {
			return this.type.castValue(value.get(0), this.format);
		}
		return null;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	protected Object getValue(int id) {
		if (value != null && value.size() != 0) {
			return value.get(id);
		}
		return null;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Object getCastedValue(int id) {
		if (value != null && value.size() != 0) {
			return this.type.castValue(value.get(id), this.format);
		}
		return null;
	}

	/**
	 * 
	 * @param value
	 * @throws IllegalObjectTypeForReturnTypeValue
	 */
	private void setDateValue(List<Object> value)
			throws IllegalObjectTypeForReturnTypeValue {
		this.value = new ArrayList<String>();
		if (format == null) {
			format = Config.SYSTEM_DATE_FORMAT_LONG;
		}
		Date date;
		for (Object object : value) {
			if (object instanceof Date) {
				date = (Date) object;
			} else if (object instanceof String) {
				date = TypeUtils.formatDate((String) object, this.format);
			} else if (object instanceof Long) {
				long l = (Long) object;
				date = new Date(l);
			} else {
				throw new IllegalObjectTypeForReturnTypeValue(
						"DATE returnTYPE requires either a Date or a String.",
						object.getClass().getName(), this.getType().toString(),
						(String) this.getValue());
			}
			this.value.add(TypeUtils.dateToString(date, format));
		}

	}

	private void setNumericOrStringValue(List<Object> value) {
		this.value = new ArrayList<String>();
		for (Object object : value) {
			this.value.add(object.toString());
		}
	}

	/**
	 * 
	 * @param value
	 * @throws IllegalObjectTypeForReturnTypeValue
	 */
	public void setValue(List<Object> value)
			throws IllegalObjectTypeForReturnTypeValue {
		if (value == null) {
			this.value = null;
		} else if (value.size() == 0) {
			this.value = new ArrayList<String>();
		} else if (value.get(0) == null) {
			this.value = null;
		} else if ((this.getType().equals(TYPE.ARRAY_OF_DATE) || this.getType()
				.equals(TYPE.DATE))) {
			setDateValue(value);
		} else if ((NumberUtils.isNumber(value.get(0) == null ? "" : value.get(
				0).toString()))
				&& (isTypeNumeric())) {
			setNumericOrStringValue(value);
		} else if ((this.getType().equals(TYPE.ARRAY_OF_STRING) || this
				.getType().equals(TYPE.STRING))) {
			setNumericOrStringValue(value);
		} else if (this.getType().equals(TYPE.BOOLEAN)) {
			setBooleanValue(value);
		} else {
			throw new IllegalObjectTypeForReturnTypeValue(
					"Could not match objectType and returnType!",
					value.get(0) == null ? "" : value.get(0).getClass()
							.getName(), this.getType().toString(),
					(String) this.getValue());
		}
	}

	/**
	 * @return
	 */
	private boolean isTypeNumeric() {
		boolean isNumeric = this.getType().equals(TYPE.ARRAY_OF_NUMERIC)
				|| this.getType().equals(TYPE.NUMERIC);
		boolean isDouble = this.getType().equals(TYPE.ARRAY_OF_DOUBLE)
				|| this.getType().equals(TYPE.DOUBLE);
		return isNumeric || isDouble;
	}

	private void setBooleanValue(List<Object> value) {
		this.value = new ArrayList<String>();
		for (Object object : value) {
			this.value.add(object.toString());
		}
	}

	/**
	 * 
	 * @param value
	 * @throws IllegalObjectTypeForReturnTypeValue
	 */
	private void setValue(String value)
			throws IllegalObjectTypeForReturnTypeValue {
		if (value == null) {
			value = "";
		}
		/*
		 * value is "end-of-line" delimited string.
		 */
		boolean hasLineSep = false;
		StringTokenizer stringTokenizer = new StringTokenizer(value,
				Character.toString(_MAJORDELIMITER));
		List<Object> listOfReturns = new ArrayList<Object>();

		hasLineSep = stringTokenizer.countTokens() > 1;
		while (stringTokenizer.hasMoreElements()) {
			listOfReturns.add(stringTokenizer.nextToken());
		}

		if (!hasLineSep) {
			StringTokenizer stringTokenizerEplise = new StringTokenizer(value,
					Character.toString((char) 168));
			listOfReturns = new ArrayList<Object>();
			while (stringTokenizerEplise.hasMoreElements()) {
				listOfReturns.add(stringTokenizerEplise.nextToken());
			}
		}
		setValue(listOfReturns);
	}

	/**
	 * 
	 * @param value
	 * @throws IllegalObjectTypeForReturnTypeValue
	 */
	@SuppressWarnings("unchecked")
	public void setValue(Object value)
			throws IllegalObjectTypeForReturnTypeValue {
		List<Object> listOfReturns = new ArrayList<Object>();
		if (value instanceof List) {
			listOfReturns = (List<Object>) value;
		} else if (value instanceof String) {
			setValue((String) value);
			return;
		} else {
			listOfReturns.add(value);
		}
		setValue(listOfReturns);
	}

	/**
	 * 
	 * @return
	 */
	public TYPE getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 */
	public void setType(TYPE type) {
		this.type = type;
	}

	/**
	 * 
	 * @return
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * 
	 * @param format
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	public String toString() {
		return this.getStrVal();
	}

	/**
	 * @return
	 */
	public String getStrVal() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		if (this.value == null || this.value.isEmpty()) {
			buffer.append("{null}");
		} else {
			for (Object v : this.value) {
				buffer.append(v == null ? "{null}" : v).append("; ");
			}
			buffer.deleteCharAt(buffer.length() - 1);
			buffer.deleteCharAt(buffer.length() - 1);
		}
		buffer.append("->TYPE:" + this.type.toString() + "<-");
		buffer.append("]");
		return buffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ReturnValue) {
			ReturnValue other = (ReturnValue) obj;
			if (other.getType() == this.getType()) {
				IOperator equal = EOperator.EQUAL.getiOperator();
				return equal.compare(this, other);
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * HASH_CONST + hashCodeForValue();
		hash = hash * HASH_CONST + (this.type == null ? 0 : type.hashCode());
		hash = hash * HASH_CONST
				+ (this.format == null ? 0 : format.hashCode());
		return hash;
	}

	private int hashCodeForValue() {
		if (value == null)
			return 0;
		int hash = 1;
		for (Object obj : value) {
			hash = HASH_CONST * hash + (obj == null ? 0 : obj.hashCode());
		}
		return hash;
	}

}
