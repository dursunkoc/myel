/**
 *
 */
package com.aric.myel.statements.operators;

import com.aric.myel.statements.components.ReturnValue;


/**
 * @author Dursun KOC
 *
 */
public enum EOperator {
	EQUAL("equal", "==", new Equal(),false),
	NOT_EQUAL("notEqual", "!=", new NotEqual(),false),
	IN("in", "IN", new In(),false),
	NOT_IN("notIn", "NOT IN", new NotIn(),false),
	LIKE("like", "LIKE", new Like(),false),
	NOT_LIKE("notLike",	"NOT LIKE", new NotLike(),false),
	GREATER("GreaterThan", ">", new GreaterThan(),false),
	GREATER_OR_EQUAL("GreaterThanOrEqual", ">=", new GreaterThanOrEqual(),false),
	SMALLER("SmallerThan", "<", new SmallerThan(),false),
	SMALLER_OR_EQUAL("SmallerThanOrEqual", "<=", new SmallerThanOrEqual(),false),
	AND("And", "&&", new And(),true),
	OR("Or", "||", new Or(),true),
	CONTAINS("Contains", "contains", new Contains(),false),
	NOT_CONTAINS("notContains", "not contains", new NotContains(),false),
	BETWEEN("between", "between", new Between(),false),
	NOOP("noop","noop",new Dummy(),false);
	
	private final String operatorPrimoName;
	private final String operatorSecundoName;
	private final IOperator iOperator;
	private final boolean isBoolOperator;

	EOperator(String operatorPrimoName, String operatorSecundoName,
			IOperator iOperator, boolean isBoolOperator) {
		this.operatorPrimoName = operatorPrimoName;
		this.operatorSecundoName = operatorSecundoName;
		this.iOperator = iOperator;
		this.isBoolOperator = isBoolOperator;
	}

	public boolean isMe(String operator) {
		return operator.equalsIgnoreCase(this.operatorPrimoName)
				|| operator.equalsIgnoreCase(this.operatorSecundoName);
	}
	public boolean isBoolOperator(){
		return this.isBoolOperator;
	}

	/**
	 * @return the iOperator
	 */
	public IOperator getiOperator() {
		return this.iOperator;
	}

	/**
	 * @param valueLeft
	 * @param valueRight
	 * @return
	 */
	public boolean compare(ReturnValue valueLeft, ReturnValue valueRight) {
		return this.iOperator.compare(valueLeft, valueRight);
	}

	/**
	 * @param valueLeft
	 * @param valueRight
	 * @param centerValue
	 * @return
	 */
	public boolean compare(ReturnValue valueLeft, ReturnValue valueRight,
			ReturnValue centerValue) {
		return this.iOperator.compare(valueLeft, valueRight, centerValue);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return this.operatorPrimoName;
	}

	public String getStrVal() {
		return this.operatorPrimoName;
	}

	public String getSecundoName() {
		return this.operatorSecundoName;
	}
	
	private static class Dummy implements IOperator{
		@Override
		public boolean compare(ReturnValue valueLeft, ReturnValue valueRight) {
			throw new UnsupportedOperationException("Dummy Operator does not execute!");
		}

		@Override
		public boolean compare(ReturnValue valueLeft, ReturnValue valueRight,
				ReturnValue centerValue) {
			throw new UnsupportedOperationException("Dummy Operator does not execute!");
		}
		
	}
	
}
