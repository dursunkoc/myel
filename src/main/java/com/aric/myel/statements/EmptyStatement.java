/**
 *
 */
package com.aric.myel.statements;

import java.util.HashSet;
import java.util.Set;

import com.aric.myel.statements.components.Environment;
import com.aric.myel.statements.components.ReturnValue;

/**
 * @author Dursun KOC
 * 
 */
public class EmptyStatement implements Statement {
	private static final long serialVersionUID = -2851420364734048608L;
	private static final EmptyStatement instance = new EmptyStatement();

	/**
	 *
	 */
	private EmptyStatement() {
	}

	public static EmptyStatement getInstance() {
		return instance;
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
		return StatementResult.resultForSimpleStatements(ReturnValue.BOOL_TRUE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.turkcelltech.comet.flow.util.statement.v2.Statement#statement2String
	 * ()
	 */
	@Override
	public String statement2String() {
		return Statement.EMPTY_STATEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.turkcelltech.comet.flow.util.statement.v2.Statement#
	 * statement2StringSimple()
	 */
	@Override
	public String statement2StringSimple() {
		return Statement.EMPTY_STATEMENT;
	}
	
	@Override
	public String statement2StringSimpleFormated(int indent) {
		return Statement.EMPTY_STATEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.turkcelltech.comet.flow.util.statement.v2.Statement#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return true;
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
	public boolean includes(Statement statement) {
		return this.equals(statement);
	}


}
