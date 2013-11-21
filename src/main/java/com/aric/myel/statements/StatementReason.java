/**
 *
 */
package com.aric.myel.statements;

import static com.aric.myel.statements.ComplexStatement.makeComplex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.aric.myel.statements.operators.EOperator;

/**
 * @author Dursun KOC
 * 
 */
public class StatementReason {
	private List<Statement> statements;
	private Map<String, Object> environmentEntries;
	public static final StatementReason EMPTY_REASON = new StatementReason(
			null, null);

	public StatementReason(Collection<Statement> statements,
			Map<String, Object> environmentEntries) {
		
		this.statements = statements == null ? new ArrayList<Statement>()
				: new ArrayList<Statement>(statements);
		this.environmentEntries = environmentEntries == null ? new HashMap<String, Object>()
				: environmentEntries;
	}

	/**
	 * @return the environmentEntries
	 */
	public Map<String, Object> getEnvironmentEntries() {
		return this.environmentEntries;
	}

	/**
	 * @return the statements
	 */
	public List<Statement> getStatements() {
		return this.statements;
	}

	public StatementReason and(StatementReason reason) {
		List<Statement> newStatements = mergeStatements(reason.statements);
		Map<String, Object> newEnvironmentEntries = mergeEnvironment(reason.environmentEntries);

		StatementReason newReason = new StatementReason(newStatements,
				newEnvironmentEntries);
		return newReason;
	}

	private Map<String, Object> mergeEnvironment(
			Map<String, Object> additionalEntries) {
		Map<String, Object> newEnvironmentEntries = new HashMap<String, Object>();
		for (Entry<String, Object> entry : this.environmentEntries.entrySet()) {
			newEnvironmentEntries.put(entry.getKey(), entry.getValue());
		}
		for (Entry<String, Object> entry : additionalEntries.entrySet()) {
			newEnvironmentEntries.put(entry.getKey(), entry.getValue());
		}
		return newEnvironmentEntries;
	}

	private List<Statement> mergeStatements(List<Statement> additionalStatements) {
		List<Statement> newStatements = new LinkedList<Statement>();
		for (Statement statement : statements) {
			for (Statement additionalStatement : additionalStatements) {
				newStatements.add(makeComplex(statement, additionalStatement, EOperator.AND));
			}			
		}
		return newStatements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n");
		for (Statement stmt : statements) {
			buffer.append(stmt.statement2String());
			buffer.append("\n");
		}
		return buffer.toString();
	}
}
