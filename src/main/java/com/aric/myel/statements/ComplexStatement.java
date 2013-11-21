/**
 *
 */
package com.aric.myel.statements;

import static com.aric.myel.statements.StatementUtils.getNewStatementSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aric.myel.exceptions.CorrectionIsNotPossibleException;
import com.aric.myel.statements.components.Environment;
import com.aric.myel.statements.components.ReturnValue;
import com.aric.myel.statements.components.TYPE;
import com.aric.myel.statements.operators.EOperator;
import com.aric.myel.utils.StringUtils;

/**
 * @author Dursun KOC
 * 
 */
public class ComplexStatement implements Statement {
	private static final long serialVersionUID = -8629178877062765391L;
	private Statement lhsStatement;
	private Statement rhsStatement;
	private EOperator operator;

	// private static ExecutorService EXEC_SERVICE =
	// Executors.newFixedThreadPool(500);
	// private static final class FMS_Task implements Callable<Set<Statement>>{
	// private final Statement s;
	// private FMS_Task(Statement s) {
	// this.s=s;
	// }
	// public static final FMS_Task create(Statement s){
	// return new FMS_Task(s);
	// }
	// @Override
	// public Set<Statement> call() throws Exception {
	// return s.findMinimumSatisfaction();
	// }
	//
	// }

	/**
	 * @param lhsStatement
	 * @param rhsStatement
	 * @param operator
	 */
	private ComplexStatement(Statement lhsStatement, Statement rhsStatement,
			EOperator operator) {
		this.lhsStatement = lhsStatement;
		this.rhsStatement = rhsStatement;
		this.operator = operator;
	}

	/**
	 * @param lhsStatement
	 * @param rhsStatement
	 * @param operator
	 * @return
	 */
	public static Statement makeComplex(Statement lhsStatement,
			Statement rhsStatement, EOperator operator) {
		if (operator.isBoolOperator()) {
			if (lhsStatement.equals(rhsStatement)) {
				return lhsStatement;
			} else if (lhsStatement.includes(rhsStatement)) {
				return lhsStatement;
			} else if (rhsStatement.includes(lhsStatement)) {
				return rhsStatement;
			} else {
				return new ComplexStatement(lhsStatement, rhsStatement,
						operator);
			}
		} else {
			return new ComplexStatement(lhsStatement, rhsStatement, operator);
		}
	}

	/**
	 * @return
	 */
	public boolean containsNestedComplexStatement() {
		return lhsStatement instanceof ComplexStatement
				|| rhsStatement instanceof ComplexStatement;
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
		long lhsComplexity = this.lhsStatement != null ? this.lhsStatement
				.getComplexityLevel() * COMPLEXITY_MULTIPLIER : COMPLEXITY_NULL;
		long rhsComplexity = this.rhsStatement != null ? this.rhsStatement
				.getComplexityLevel() * COMPLEXITY_MULTIPLIER : COMPLEXITY_NULL;
		return 1 + lhsComplexity + rhsComplexity;
	}

	public Statement getLhsStatement() {
		return lhsStatement;
	}

	public Statement getRhsStatement() {
		return rhsStatement;
	}

	public EOperator getOperator() {
		return operator;
	}

	public void setOperator(EOperator operator) {
		this.operator = operator;
	}

	/**
	 * @param environment
	 * @return
	 */
	private StatementResult simpleExecute(Environment environment) {
		if (operator.isBoolOperator()) {
			return optimizedBoolOperator(environment);
		}
		StatementResult lhsResult = lhsStatement.execute(environment, false);
		StatementResult rhsResult = rhsStatement.execute(environment, false);
		StatementResult retVal = doOperation(environment, lhsResult, rhsResult);
		return retVal;
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
		if (!withReason) {
			return simpleExecute(environment);
		}
		Set<Statement> reasons = executeForReason(environment);
		if (reasons.isEmpty()) {
			return StatementResult.resultForComplexStatements(
					ReturnValue.BOOL_TRUE, StatementReason.EMPTY_REASON);
		} else {
			Map<String, Object> environmentEntries = extractUsedEnvironment(
					reasons, environment);
			return StatementResult.resultForComplexStatements(
					ReturnValue.BOOL_FALSE, new StatementReason(reasons,
							environmentEntries));
		}
	}

	/**
	 * @param reasons
	 * @param environment
	 * @return
	 */
	private Map<String, Object> extractUsedEnvironment(Set<Statement> reasons,
			Environment environment) {
		Map<String, Object> entries = new HashMap<String, Object>();
		List<String> keys = new ArrayList<String>();
		for (Statement stmt : reasons) {
			StatementUtils.listOutVariablesIterative(keys, stmt);
		}
		for (String key : keys) {
			Object variable = environment.getUncheckedVariable(key);
			entries.put(key, variable);
		}
		return entries;
	}

	/**
	 * @param environment
	 * @return
	 */
	private Set<Statement> executeForReason(Environment environment) {
		Set<Statement> findMinimumSatisfaction = findMinimumSatisfaction(environment);
		return findMinimumSatisfaction;
	}

	/**
	 * @param environment
	 * @param isDebug
	 * @return
	 */
	private StatementResult optimizedBoolOperator(Environment environment) {
		StatementResult lhsResult = lhsStatement.execute(environment, false);
		ReturnValue lhsRetVal = lhsResult.getResult();
		Boolean lhsValue = (Boolean) lhsRetVal.getCastedValue();
		if (lhsValue && operator == EOperator.OR) {
			return StatementResult.resultForSimpleStatements(lhsRetVal);
		} else if (!lhsValue && operator == EOperator.AND) {
			return StatementResult.resultForSimpleStatements(lhsRetVal);
		}
		StatementResult rhsResult = rhsStatement.execute(environment, false);
		StatementResult retVal = doOperation(environment, lhsResult, rhsResult);
		return retVal;
	}

	/**
	 * @param environment
	 * @param isDebug
	 * @param lhsResult
	 * @param rhsResult
	 * @return
	 */
	private StatementResult doOperation(Environment environment,
			StatementResult lhsResult, StatementResult rhsResult) {
		boolean result;
		result = operator.compare(lhsResult.getResult(), rhsResult.getResult());
		StatementResult retVal = StatementResult
				.resultForSimpleStatements(new ReturnValue(result, TYPE.BOOLEAN));
		return retVal;
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
		buffer.append(Statement.BEGIN).append(Statement.DELIM);
		buffer.append(lhsStatement.statement2String()).append(Statement.DELIM);
		buffer.append(operator.getStrVal()).append(Statement.DELIM);
		buffer.append(rhsStatement.statement2String()).append(Statement.DELIM);
		buffer.append(Statement.END);
		return buffer.toString();
	}

	@Override
	public String statement2StringSimple() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(Statement.BEGIN).append(Statement.DELIM);
		buffer.append(lhsStatement.statement2StringSimple()).append(
				Statement.DELIM);
		buffer.append(operator.getStrVal()).append(Statement.DELIM);
		buffer.append(rhsStatement.statement2StringSimple()).append(
				Statement.DELIM);
		buffer.append(Statement.END);
		return buffer.toString();
	}

	@Override
	public String statement2StringSimpleFormated(int indent) {
		StringBuffer buffer = new StringBuffer();
		if(!this.operator.isBoolOperator()){
			buffer.append(this.statement2StringSimple());
		}else{
			buffer.append(Statement.BEGIN);
			newLine(indent, buffer);
			buffer.append(this.lhsStatement.statement2StringSimpleFormated(indent+1));
			newLine(indent, buffer);
			buffer.append(this.operator);
			newLine(indent, buffer);
			buffer.append(this.rhsStatement.statement2StringSimpleFormated(indent+1));
			newLine(indent-1, buffer);
			buffer.append(Statement.END);
		}
		return buffer.toString();
	}

	private void newLine(int indent, StringBuffer buffer) {
		buffer.append("\n");
		buffer.append(StringUtils.lpad(" ", indent * 4));
	}

	@Override
	public String toString() {
		return this.statement2StringSimple();
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
		return true;
	}

	/**
	 * @param environment
	 * @return
	 */
	@Override
	public Set<Statement> findMinimumSatisfaction(Environment environment) {
		if (!operator.isBoolOperator()) {
			return findMinimumSatisfaction00(this, environment);
		}
		Set<Statement> _lhs = findMinimumSatisfaction0(lhsStatement,
				environment);
		Set<Statement> _rhs = findMinimumSatisfaction0(rhsStatement,
				environment);
		Set<Statement> result = makeConjuction(_lhs, _rhs);
		return result;

	}

	private Set<Statement> findMinimumSatisfaction00(ComplexStatement s,
			Environment environment) {
		StatementResult r = s.execute(environment);
		if (!r.isSuccessful()) {
			Set<Statement> set = getNewStatementSet(1);
			set.add(this);
			return set;
		} else {
			return Collections.emptySet();
		}
	}

	/**
	 * @param s
	 * @param environment
	 * @return
	 */
	private Set<Statement> findMinimumSatisfaction0(Statement s,
			Environment environment) {
		StatementResult r = s.execute(environment);
		if (!r.isSuccessful()) {
			return s.findMinimumSatisfaction(environment);
		} else {
			return Collections.emptySet();
		}
	}

	private Set<Statement> makeConjuction(Set<Statement> _lhs,
			Set<Statement> _rhs) {
		Set<Statement> result = null;
		if (operator == EOperator.AND) {
			result = doAndConjuction(_lhs, _rhs);
		} else if (operator == EOperator.OR) {
			result = doOrConjuction(_lhs, _rhs);
		} else {
			throw new CorrectionIsNotPossibleException(
					"bool operator should either be \"AND\" or \"OR\"!");
		}
		result = sanityCheck(result);
		return result;
	}

	// private Set<Statement> getFMS(Statement s) {
	// if(lhsStatement.getComplexityLevel()>COMPLEXITY_MULTIPLIER*COMPLEXITY_MULTIPLIER){
	// return fmsViaMultiThread(s);
	// }else{
	// return s.findMinimumSatisfaction();
	// }
	// }
	//
	//
	// private Set<Statement> fmsViaMultiThread(Statement s){
	// Future<Set<Statement>> fs = EXEC_SERVICE.submit(FMS_Task.create(s));
	// try {
	// return fs.get();
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw new
	// RuntimeException("Exception while executing ["+s.statement2StringSimple()+"]",e);
	// }
	// }

	private Set<Statement> sanityCheck(Set<Statement> result) {
		Set<Set<Statement>> sanity = new HashSet<Set<Statement>>(
				result.size() * 3);

		for (Statement statement : result) {
			if (statement.isComplex()) {
				Set<Statement> resolveIntoSetStatement = StatementUtils
						.resolveIntoSetStatement((ComplexStatement) statement);
				checkSanityAndAdd(sanity, resolveIntoSetStatement);
			}
		}
		result = getNewStatementSet(result.size());
		for (Set<Statement> ss : sanity) {
			result.add(StatementUtils.compound(ss, EOperator.AND));
		}
		return result;
	}

	private void checkSanityAndAdd(Set<Set<Statement>> sanity,
			Set<Statement> newStatements) {
		if (sanity.isEmpty()) {
			sanity.add(newStatements);
			return;
		}
		boolean newStatementShouldBeIncluded = true;
		boolean newStatemetnLocked = false;
		Set<Set<Statement>> removalSet = new HashSet<Set<Statement>>(
				sanity.size());
		for (Set<Statement> ss : sanity) {
			if (includes(ss, newStatements)) {
				removalSet.add(ss);
				newStatementShouldBeIncluded = true;
			} else if (includes(newStatements, ss)) {
				newStatementShouldBeIncluded = false;
				newStatemetnLocked = true;
			}
		}
		sanity.removeAll(removalSet);
		if (!newStatemetnLocked && newStatementShouldBeIncluded) {
			sanity.add(newStatements);
		}
	}

	private boolean includes(Set<Statement> master, Set<Statement> newbie) {
		Set<Statement> intersections = getNewStatementSet(master);
		intersections.retainAll(newbie);
		return intersections.size() == newbie.size();
	}

	/**
	 * @param _lhs
	 * @param _rhs
	 * @return
	 */
	private Set<Statement> doOrConjuction(Set<Statement> _lhs,
			Set<Statement> _rhs) {
		if (_lhs.isEmpty() || _rhs.isEmpty()) {
			return Collections.emptySet();
		}
		Set<Statement> result = getNewStatementSet(_lhs);
		result.addAll(_rhs);
		return result;
	}

	/**
	 * @param _lhs
	 * @param _rhs
	 * @return
	 */
	private Set<Statement> doAndConjuction(Set<Statement> _lhs,
			Set<Statement> _rhs) {
		if (_lhs.isEmpty()) {
			return _rhs;
		}
		if (_rhs.isEmpty()) {
			return _lhs;
		}
		Set<Statement> intersections = getNewStatementSet(_lhs);
		intersections.retainAll(_rhs);

		Set<Statement> _lhsOnly = getNewStatementSet(_lhs);
		_lhsOnly.removeAll(_rhs);

		Set<Statement> _rhsOnly = getNewStatementSet(_rhs);
		_rhsOnly.removeAll(_lhs);

		Set<Statement> result = getNewStatementSet(_lhsOnly.size()
				* _rhsOnly.size() * 2);
		for (Statement _iRhs : _lhsOnly) {
			for (Statement _iLhs : _rhsOnly) {
				result.add(makeComplex(_iLhs, _iRhs, EOperator.AND));
			}
		}
		result.addAll(intersections);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ComplexStatement)) {
			return false;
		}
		ComplexStatement that = (ComplexStatement) obj;
		return this.rhsStatement.equals(that.rhsStatement)
				&& this.lhsStatement.equals(that.lhsStatement)
				&& this.operator.equals(that.operator);
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * HASH_CONST
				+ (lhsStatement == null ? 0 : lhsStatement.hashCode());
		hash = hash * HASH_CONST
				+ (rhsStatement == null ? 0 : rhsStatement.hashCode());
		hash = hash * HASH_CONST + (operator == null ? 0 : operator.hashCode());
		return hash;
	}

	@Override
	public boolean includes(Statement statement) {
		return this.equals(statement) || this.lhsStatement.includes(statement)
				|| this.rhsStatement.includes(statement);
	}
}
