/**
 *
 */
package com.aric.myel.statements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import com.aric.myel.statements.components.Environment;
import com.aric.myel.statements.components.ReturnValue;
import com.aric.myel.statements.components.TYPE;
import com.aric.myel.utils.StringUtils;

/**
 * @author Dursun KOC
 *
 */
public class ConstantStatement implements Statement {
	private static final long serialVersionUID = 3220324825097470187L;
	private ReturnValue value;

	/**
	 * @param value
	 */
	public ConstantStatement(ReturnValue value) {
		this.value = value;
	}

	public ReturnValue getValue() {
		return value;
	}

	public void setValue(ReturnValue value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seecom.turkcelltech.comet.flow.util.statement.v2.Statement#execute(com.
	 * turkcelltech.comet.flow.engine.components.Environment)
	 */
	@Override
	public StatementResult execute(Environment environment) {
		return this.execute(environment, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seecom.turkcelltech.comet.flow.util.statement.v2.Statement#execute(com.
	 * turkcelltech.comet.flow.engine.components.Environment, boolean)
	 */
	@Override
	public StatementResult execute(Environment environment, boolean withReason) {
		return StatementResult.resultForSimpleStatements(this.value);
	}

	@Override
	public String statement2String() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(Statement.CONST_BEGIN).append(Statement.DELIM);
		this.value2String(buffer);
		buffer.append(Statement.CONST_END);
		return buffer.toString();
	}
	
	@Override
	public String statement2StringSimple() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.value);
		return buffer.toString();
	}
	
	@Override
	public String statement2StringSimpleFormated(int indent) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.value);
		return buffer.toString();
	}

	/**
	 * @param buffer
	 */
	private void value2String(StringBuffer buffer) {
		List<String> listOfValues = this.value.getListOfValues();
		for (String strVal : listOfValues) {
			buffer.append(strVal).append(Statement.CONST_VALSEP);
		}
		buffer.append(Statement.DELIM);
		buffer.append(Statement.CONST_FORMATINDICATOR);
		buffer.append(StringUtils.nvl(value.getFormat(), ""));
		buffer.append(Statement.CONST_FORMATINDICATOR);
		buffer.append(Statement.DELIM);
		buffer.append(Statement.CONST_TYPEINDICATOR);
		buffer.append(value.getType().getTypeId());
		buffer.append(Statement.CONST_TYPEINDICATOR);
		buffer.append(Statement.DELIM);
	}

	public static ConstantStatement buildFromString(String str) {
		ReturnValue value = null;
		StringTokenizer coreValueTokenizer = new StringTokenizer(str,
				Statement.CONST_VALSEP);
		int countTokens = coreValueTokenizer.countTokens();
		List<String> coreVal = new ArrayList<String>();
		for (int valIndex = 0; valIndex < countTokens - 1; valIndex++) {
			coreVal.add(coreValueTokenizer.nextToken());
		}
		String supplimentaryDiv = coreValueTokenizer.nextToken();
		String formatDiv = StringUtils.getSubStringBetweenTokens(
				supplimentaryDiv, Statement.CONST_FORMATINDICATOR,
				Statement.CONST_FORMATINDICATOR);
		String typeDiv = StringUtils.getSubStringBetweenTokens(
				supplimentaryDiv, Statement.CONST_TYPEINDICATOR,
				Statement.CONST_TYPEINDICATOR);
		value = new ReturnValue(coreVal, TYPE
				.getType(Integer.parseInt(typeDiv)), formatDiv);
		return new ConstantStatement(value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.turkcelltech.comet.flow.util.statement.v2.Statement#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.turkcelltech.comet.flow.util.statement.v2.Statement#isComplex()
	 */
	@Override
	public boolean isComplex() {
		return false;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @seecom.turkcelltech.comet.flow.util.statement.v2.Statement#
	 * findMinimumSatisfaction()
	 */
	@Override
	public Set<Statement> findMinimumSatisfaction(Environment environment) {
		HashSet<Statement> set = new HashSet<Statement>();
		set.add(this);
		return set;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.turkcelltech.comet.flow.util.statement.v2.Statement#getComplexityLevel
	 * ()
	 */
	@Override
	public long getComplexityLevel() {
		return COMPLEXITY_BASE;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ConstantStatement)){
			return false;
		}
		ConstantStatement that = (ConstantStatement) obj;
		return this.value.equals(that.value);
	}
	
	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * HASH_CONST + (this.value==null?0:value.hashCode()); 
		return hash;
	}

	@Override
	public boolean includes(Statement statement) {
		return this.equals(statement);
	}

}
