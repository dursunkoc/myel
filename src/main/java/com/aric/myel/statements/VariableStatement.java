/**
 *
 */
package com.aric.myel.statements;

import java.util.HashSet;
import java.util.Set;

import com.aric.myel.exceptions.InvalidStatementException;
import com.aric.myel.statements.components.Environment;
import com.aric.myel.statements.components.ReturnValue;

/**
 * @author Dursun KOC
 * 
 */
public class VariableStatement implements Statement {
	private static final long serialVersionUID = 8760976423646471617L;
	private String variableName;

	/**
	 * @param variableName
	 */
	public VariableStatement(String variableName) {
		this.variableName = variableName;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
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
		Object varObj = environment.getUncheckedVariable(variableName);
		if (varObj instanceof ReturnValue) {
			ReturnValue result = (ReturnValue) varObj;
			environment.addOrUpdateVariable(variableName, result);
			return StatementResult.resultForSimpleStatements(result);
		}
		throw new InvalidStatementException(
				"Variable should be in ReturnValue Type.(variableName='"
						+ variableName + "')");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.flow.util.statement.v2.Statement#statment2String()
	 */
	@Override
	public String statement2String() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(Statement.VARDEF_BEGIN);
		buffer.append(this.variableName);
		buffer.append(Statement.VARDEF_END);
		return buffer.toString();
	}

	@Override
	public String statement2StringSimple() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(Statement.VARDEF_BEGIN);
		buffer.append(this.variableName);
		buffer.append(Statement.VARDEF_END);
		return buffer.toString();
	}

	@Override
	public String statement2StringSimpleFormated(int indent) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(Statement.VARDEF_BEGIN);
		buffer.append(this.variableName);
		buffer.append(Statement.VARDEF_END);
		return buffer.toString();
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

	/*
	 * (non-Javadoc)
	 * 
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
		if (!(obj instanceof VariableStatement)) {
			return false;
		}
		VariableStatement that = (VariableStatement) obj;
		return this.variableName.equals(that.variableName);
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * HASH_CONST
				+ (this.variableName == null ? 0 : variableName.hashCode());
		return hash;
	}

	@Override
	public boolean includes(Statement statement) {
		return this.equals(statement);
	}
}
