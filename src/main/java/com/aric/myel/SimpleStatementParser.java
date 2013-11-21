/**
 * 
 */
package com.aric.myel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.aric.myel.exceptions.InvalidStatementException;
import com.aric.myel.symbols.Symbol;
import com.aric.myel.symbols.SymbolFactory;
import com.aric.myel.utils.StringUtils;

/**
 * @author TTDKOC
 *
 */
public class SimpleStatementParser extends StatementParser {

	private static final String _STRING_PRIM = "\"";
	private static final String _STRING_TERM = "\"";
	private static final String _STRING_PRIM_LONG = "\\\"";
	private static final String _STRING_TERM_LONG = "\\\"";
	private static final String _DATE_PRIM = "<";
	private static final String _DATE_FORMAT_DELIM = ",";
	private static final String _DATE_TERM = ">";
	private static final String _NUMBER_PRIM = "#";
	private static final String _NUMBER_TERM = "#";
	private static final String _VAR_PRIM = "${";
	private static final String _VAR_TERM = "}";

	private static final String _COMPLEX_PRIM = "(";
	private static final String _COMPLEX_TERM = ")";

	public static final String _TOKEN_DELIM = " ";
	public static final SimpleDateFormat _DEFAULT_DATE_FORMAT = new SimpleDateFormat(
			Config.SYSTEM_DATE_FORMAT_LONG);

	private static final SimpleDateFormat _DATE_FORMAT = new SimpleDateFormat();

	@SuppressWarnings("rawtypes")
	private Map varSet;

	/**
	 * @param varSet
	 */
	@SuppressWarnings("rawtypes")
	public SimpleStatementParser(Map varSet) {
		this.varSet = varSet;
	}

	/**
	 * 
	 * @param command
	 * @param varSet
	 * @return
	 * @throws InvalidStatementException
	 */
	public Executable resolve(String command) {
		if (command == null || command.isEmpty()) {
			return new SimpleExecutable(null);
		}
		List<String> tokens = simplyParseSource(command);
		List<String> leftNotationTokens = buildLeftNotation(tokens);
		Executable finalStatement = buildExecutionStack(leftNotationTokens);
		return finalStatement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.turkcelltech.comet.commonutil.utils.el.StatementParser#
	 * createEmptyStatement()
	 */
	@Override
	protected Executable createEmptyStatement() {
		return new SimpleExecutable(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.turkcelltech.comet.commonutil.utils.el.StatementParser#
	 * createConstantStatement(java.lang.String)
	 */
	@Override
	protected Executable createConstantStatement(String token) {
		Object value;
		if (isString(token)) {
			value = formatString(token);
		} else if (isDate(token)) {
			value = formatDate(token);
		} else if (isNumeric(token)) {
			value = formatNumber(token);
		} else {
			throw new InvalidStatementException("Constant token '" + token
					+ "' could be converted to an executable object.");
		}
		return new SimpleExecutable(value);
	}

	/**
	 * @param token
	 * @return
	 */
	private boolean isString(String token) {
		return isStringWithShortQuotes(token) || isStringWithLongQuotes(token);
	}

	private boolean isStringWithShortQuotes(String token) {
		return token.startsWith(_STRING_PRIM) && token.endsWith(_STRING_TERM);
	}

	private boolean isStringWithLongQuotes(String token) {
		return token.startsWith(_STRING_PRIM_LONG)
				&& token.endsWith(_STRING_TERM_LONG);
	}

	/**
	 * @param token
	 * @return
	 */
	private boolean isDate(String token) {
		return token.startsWith(_DATE_PRIM) && token.endsWith(_DATE_TERM);
	}

	/**
	 * @param token
	 * @return
	 */
	private boolean isNumeric(String token) {
		return token.startsWith(_NUMBER_PRIM)
				&& token.endsWith(_NUMBER_TERM)
				&& NumberUtils.isNumber(StringUtils
						.getSubStringBetweenTokens(token, _NUMBER_PRIM,
								_NUMBER_TERM));
	}

	/**
	 * @param token
	 * @return
	 */
	private Object formatNumber(String token) {
		String numberString = StringUtils.getSubStringBetweenTokens(token,
				_NUMBER_PRIM, _NUMBER_TERM).trim();
		if (!NumberUtils.isNumber(numberString)) {
			throw new InvalidStatementException("'" + token
					+ "' is not a numeric value");
		}
		return NumberUtils.createNumber(numberString);
	}

	/**
	 * @param token
	 * @param value
	 * @return
	 */
	private Date formatDate(String token) {
		String dateString = StringUtils.getSubStringBetweenTokens(token,
				_DATE_PRIM, _DATE_FORMAT_DELIM).trim();
		// empty string means Current Time...
		if (dateString == null || dateString.isEmpty()) {
			return new Date();
		}
		// extract formatString.
		String formatString = StringUtils.getSubStringBetweenTokens(token,
				_DATE_FORMAT_DELIM, _DATE_TERM).trim();

		try {
			// if formatString is null then use default date format
			// else use formatString to format dateString
			if (formatString == null || formatString.isEmpty()) {
				return _DEFAULT_DATE_FORMAT.parse(dateString);
			} else {
				_DATE_FORMAT.applyPattern(formatString);
				return _DATE_FORMAT.parse(dateString);
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param token
	 * @return
	 */
	private Object formatString(String token) {
		Object value;
		if (isStringWithLongQuotes(token)) {
			value = StringUtils.getSubStringBetweenTokens(token,
					_STRING_PRIM_LONG, _STRING_TERM_LONG);
		} else {
			value = StringUtils.getSubStringBetweenTokens(token,
					_STRING_PRIM, _STRING_TERM);
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.turkcelltech.comet.commonutil.utils.el.StatementParser#
	 * createVariableStatement(java.lang.String)
	 */
	@Override
	protected Executable createVariableStatement(String token) {
		String variableName = StringUtils.getSubStringBetweenTokens(token,
				_VAR_PRIM, _VAR_TERM);
		if (varSet.containsKey(variableName)) {
			return new SimpleExecutable(varSet.get(variableName));
		}

		if (variableName.contains(_TOKEN_DELIM)) {
			SimpleStatementParser newParser = new SimpleStatementParser(varSet);
			Executable resolved = newParser.resolve(variableName);
			return resolved;
		} else {
			throw new InvalidStatementException("Token '" + variableName
					+ "' is not valid!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.turkcelltech.comet.commonutil.utils.el.StatementParser#
	 * combineExecutablesUsingSymbol
	 * (com.turkcelltech.comet.commonutil.utils.el.Executable,
	 * com.turkcelltech.comet.commonutil.utils.el.Executable, java.lang.String)
	 */
	@Override
	protected Executable combineExecutablesUsingSymbol(Executable rhsStatement,
			Executable lhsStatement, String token) {
		Symbol symbol = SymbolFactory.getSymbol(token);
		Executable result = symbol.operate(lhsStatement, rhsStatement);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.turkcelltech.comet.commonutil.utils.el.StatementParser#
	 * isComplexBeginOrComplexEnd(char, java.lang.String, int)
	 */
	@Override
	protected boolean isComplexBeginOrComplexEnd(char c, String command,
			int index) {
		return isComplexBegin(c, command, index)
				|| isComplexEnd(c, command, index);
	}

	/**
	 * @param c
	 * @return
	 */
	private static boolean isComplexEnd(char c, String command, int index) {
		boolean isComplexEnd = (c == _COMPLEX_TERM.charAt(0) && checkConformity(
				command, index, _COMPLEX_TERM));
		return isComplexEnd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.StatementParser#isComplexEnd
	 * (java.lang.String)
	 */
	@Override
	protected boolean isComplexEnd(String command) {
		return command.equals(_COMPLEX_TERM);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.StatementParser#isComplexBegin
	 * (char, java.lang.String, int)
	 */
	@Override
	protected boolean isComplexBegin(char c, String command, int index) {
		boolean isComplexBegin = (c == _COMPLEX_PRIM.charAt(0) && checkConformity(
				command, index, _COMPLEX_PRIM));
		return isComplexBegin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.StatementParser#isComplexBegin
	 * (java.lang.String)
	 */
	@Override
	protected boolean isComplexBegin(String command) {
		return command.equals(_COMPLEX_PRIM);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.StatementParser#isVariableEnd
	 * (java.lang.String, int)
	 */
	@Override
	protected boolean isVariableEnd(String command, int index) {
		boolean isVariableEnd = (command.charAt(index) == _VAR_TERM.charAt(0) && checkConformity(
				command, index, _VAR_TERM));
		return isVariableEnd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.StatementParser#isVariableBegin
	 * (char, java.lang.String, int)
	 */
	@Override
	protected boolean isVariableBegin(char c, String command, int index) {
		boolean isVariableBegin = (c == _VAR_PRIM.charAt(0) && checkConformity(
				command, index, _VAR_PRIM));
		return isVariableBegin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.StatementParser#isVariable
	 * (java.lang.String)
	 */
	@Override
	protected boolean isVariable(String command) {
		return command.startsWith(_VAR_PRIM) && command.endsWith(_VAR_TERM);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.StatementParser#isConstantEnd
	 * (java.lang.String, int)
	 */
	@Override
	protected boolean isConstantEnd(String command, int index) {
		boolean isStringEndWithShortQuotes = (command.charAt(index) == _STRING_TERM
				.charAt(0) && checkConformity(command, index, _STRING_TERM));
		boolean isStringEndWithLongQuotes = (command.charAt(index) == _STRING_TERM_LONG
				.charAt(0) && checkConformity(command, index, _STRING_TERM_LONG));
		boolean isNumberEnd = (command.charAt(index) == _NUMBER_TERM.charAt(0) && checkConformity(
				command, index, _NUMBER_TERM));
		boolean isDateEnd = (command.charAt(index) == _DATE_TERM.charAt(0) && checkConformity(
				command, index, _DATE_TERM));
		return isStringEndWithShortQuotes || isStringEndWithLongQuotes
				|| isNumberEnd || isDateEnd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.StatementParser#isConstantBegin
	 * (char, java.lang.String, int)
	 */
	@Override
	protected boolean isConstantBegin(char c, String command, int index) {
		boolean isStringBeginWithShortQuotes = (command.charAt(index) == _STRING_PRIM
				.charAt(0) && checkConformity(command, index, _STRING_PRIM));
		boolean isStringBeginWithLongQuotes = (command.charAt(index) == _STRING_PRIM_LONG
				.charAt(0) && checkConformity(command, index, _STRING_PRIM_LONG));
		boolean isNumberBegin = (command.charAt(index) == _NUMBER_PRIM
				.charAt(0) && checkConformity(command, index, _NUMBER_PRIM));
		boolean isDateBegin = (command.charAt(index) == _DATE_PRIM.charAt(0) && checkConformity(
				command, index, _DATE_PRIM));
		return isStringBeginWithShortQuotes || isStringBeginWithLongQuotes
				|| isNumberBegin || isDateBegin;
	}

	protected static boolean checkConformity(String command, int index,
			String conformityStr) {
		return command.subSequence(index, index + conformityStr.length())
				.equals(conformityStr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.StatementParser#isConstant
	 * (java.lang.String)
	 */
	@Override
	protected boolean isConstant(String command) {
		boolean isString = isString(command);
		boolean isNumeric = isNumeric(command);
		boolean isDate = isDate(command);
		return isString || isNumeric || isDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.StatementParser#isDelim(char,
	 * java.lang.String, int)
	 */
	@Override
	protected boolean isDelim(char c, String command, int index) {
		boolean isDelim = (c == _TOKEN_DELIM.charAt(0) && checkConformity(
				command, index, _TOKEN_DELIM));
		return isDelim;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.StatementParser#isSymbol(java
	 * .lang.String)
	 */
	@Override
	protected boolean isSymbol(String token) {
		return SymbolFactory.isSymbol(token);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.StatementParser#isDecisionOperator
	 * (java.lang.String)
	 */
	@Override
	protected boolean isDecisionOperator(String token) {
		Symbol symbol = SymbolFactory.getSymbol(token);
		return symbol.isDecisionOperator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.commonutil.utils.el.StatementParser#isEmpty(java
	 * .lang.String)
	 */
	@Override
	protected boolean isEmpty(String token) {
		return false;
	}

}
