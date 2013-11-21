/**
 *
 */
package com.aric.myel.statements;

import java.util.Set;

import com.aric.myel.Executable;
import com.aric.myel.statements.components.Environment;

/**
 * @author TTDKOC
 * 
 */
public interface Statement extends Executable {
	public static final Character _MAJORDELIMITER = 30;
	public static final int COMPLEXITY_NULL = 0;
	public static final int COMPLEXITY_BASE = 1;
	public static final int COMPLEXITY_MULTIPLIER = 10;
	public static final String BEGIN = "(";
	public static final String END = ")";
	public static final String DELIM = " ";
	public static final String VARDEF_BEGIN = "${";
	public static final String VARDEF_END = "}";
	public static final String CONST_BEGIN = "[";
	public static final String CONST_END = "]";
	public static final String CONST_VALSEP = _MAJORDELIMITER.toString();
	public static final String CONST_FORMATINDICATOR = "|";
	public static final String CONST_TYPEINDICATOR = ">";
	public static final String EMPTY_STATEMENT = Character.toString((char) 238);

	public static final char cBEGIN = BEGIN.charAt(0);
	public static final char cEND = END.charAt(0);
	public static final char cDELIM = DELIM.charAt(0);
	public static final char cVARDEF_BEGIN = VARDEF_BEGIN.charAt(0);
	public static final char cVARDEF_END = VARDEF_END.charAt(0);
	public static final char cCONST_BEGIN = CONST_BEGIN.charAt(0);
	public static final char cCONST_END = CONST_END.charAt(0);
	public static final char cCONST_VALSEP = CONST_VALSEP.charAt(0);
	public static final char cCONST_FORMATINDICATOR = CONST_FORMATINDICATOR
			.charAt(0);
	public static final char cCONST_TYPEINDICATOR = CONST_TYPEINDICATOR
			.charAt(0);
	public static final int HASH_CONST = 31;

	/**
	 * @param environment
	 * @return
	 */
	public abstract StatementResult execute(Environment environment);

	/**
	 * @param environment
	 * @param withReason
	 * @return
	 */
	public abstract StatementResult execute(Environment environment,
			boolean withReason);

	/**
	 * @return
	 */
	public boolean isEmpty();
	
	/**
	 * @return
	 */
	public boolean isComplex();

	/**
	 * @return
	 */
	public String statement2String();

	/**
	 * @return
	 */
	public String statement2StringSimple();
	/**
	 * @return
	 */
	public String statement2StringSimpleFormated(int indent);

	/**
	 * @param environment
	 * @return
	 */
	public Set<Statement> findMinimumSatisfaction(Environment environment);

	/**
	 * @return
	 */
	public long getComplexityLevel();

	/**
	 * @param rhsStatement
	 * @return
	 */
	public abstract boolean includes(Statement statement);

}
