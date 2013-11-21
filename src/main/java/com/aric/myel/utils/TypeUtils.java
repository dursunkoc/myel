/**
 * 
 */
package com.aric.myel.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.aric.myel.Config;
import com.aric.myel.exceptions.InvalidTypeForOperatorException;
import com.aric.myel.statements.components.ReturnValue;
import com.aric.myel.statements.components.TYPE;

/**
 * @author TTDKOC
 *
 */
public class TypeUtils {
	public static Date formatDate(String dateValue, String dateFormat) {
		String localDateFormat = new String(dateFormat);
		String localDateValue = new String(dateValue);
		if (dateFormat.length() - dateValue.length() >= 5) {
			localDateValue = new StringBuffer(dateValue).append(" 00:00:00")
					.toString();
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				localDateFormat);
		Date resultDate;
		try {
			resultDate = simpleDateFormat.parse(localDateValue);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return resultDate;
	}

	public static String dateToString(Date date, String dateFormat) {
		if (dateFormat == null || dateFormat.isEmpty()) {
			dateFormat = Config.SYSTEM_DATE_FORMAT_LONG;
		}
		return DateFormatUtils.format(date, dateFormat);
	}

	public static void validateTypes(ReturnValue valueLeft,
			ReturnValue valueRight) {
		if (IsEitherTypeNill(valueLeft.getType(), valueRight.getType())) {
			return;
		}
		if (valueLeft.getType().getType() != valueRight.getType().getType()) {
			throw new InvalidTypeForOperatorException(
					"Types does not match.Left:"
							+ valueLeft.getType().getType() + " and Rigth:"
							+ valueRight.getType().getType());
		}
	}

	public static void validateTypes(TYPE typeLeft, TYPE typeRight) {
		if (IsEitherTypeNull(typeLeft, typeRight)
				|| ((typeLeft.getType() != typeRight.getType()) && !IsEitherTypeNill(
						typeLeft.getType(), typeRight.getType()))) {
			throw new InvalidTypeForOperatorException(
					"Types does not match.Left:" + typeLeft + " and Rigth:"
							+ typeRight);
		}
	}

	public static boolean IsEitherTypeNill(TYPE typeLeft, TYPE typeRight) {
		return (typeLeft == TYPE.NILL || typeRight == TYPE.NILL);
	}

	public static boolean IsEitherTypeNull(TYPE typeLeft, TYPE typeRight) {
		return (typeLeft == null || typeRight == null);
	}

	public static boolean checkNullValues(ReturnValue valueLeft,
			ReturnValue valueRight) {
		return (valueLeft.getCastedValue() == null || valueRight
				.getCastedValue() == null);

	}
}
