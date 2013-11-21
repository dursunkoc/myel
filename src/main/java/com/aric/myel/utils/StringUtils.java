/**
 * 
 */
package com.aric.myel.utils;

/**
 * @author TTDKOC
 *
 */
public class StringUtils {

	/**
	 * retrieves a portion of String between first occurance of startToken, and
	 * lastOccurance of endToken
	 * 
	 * @param fullText
	 * @param startToken
	 * @param endToken
	 * @return
	 */
	public static String getSubStringBetweenTokens(String fullText,
			String startToken, String endToken) {
		int beginIndex = fullText.indexOf(startToken) + startToken.length();
		int endIndex = fullText.lastIndexOf(endToken);
		String retVal = fullText.substring(beginIndex, endIndex);
		return retVal;
	}

	public static boolean isStringNull(String s) {
		return s == null || s.trim().equals("");
	}

	public static <T> T nvl(T obj, T nullValue) {
		if (obj == null) {
			return nullValue;
		}
		return obj;
	}

	public static String lpad(String deger, int uzunluk) {
		StringBuffer str = new StringBuffer();

		if (deger.length() < uzunluk) {
			int len = uzunluk;
			str.append(deger);
			for (int index = 0; index < len - deger.length(); index++) {
				str.append(" ");
			}
		}
		return str.toString();
	}

	public static String lpad(String deger, int uzunluk, String ekdeger) {
		StringBuffer str = new StringBuffer();
		StringBuffer xStr = new StringBuffer();
		str.append(deger);
		if (deger.length() < uzunluk) {
			int len = uzunluk;
			for (int index = 0; index < len - deger.length(); index++) {
				xStr.append(ekdeger);
			}
		}
		xStr.append(str.toString());
		return xStr.toString();
	}

}
