/**
 *
 */
package com.aric.myel.statements;

import static com.aric.myel.statements.ComplexStatement.makeComplex;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.aric.myel.Executable;
import com.aric.myel.StatementParser;
import com.aric.myel.exceptions.InvalidStatementException;
import com.aric.myel.statements.components.Environment;
import com.aric.myel.statements.operators.EOperator;
import com.aric.myel.statements.operators.OperatorFactory;

/**
 * @author Dursun KOC
 * 
 */
public class StatementUtils {
	/**
	 * 
	 */
	private static InternalStatementParser parser = new InternalStatementParser();

	/**
	 * @param statement
	 * @param environment
	 */
	public static Statement focusOnFalse(Statement statement,
			Environment environment) {
		if (statement instanceof ComplexStatement) {
			ComplexStatement complexStmt = (ComplexStatement) statement;
			if (complexStmt.containsNestedComplexStatement()) {
				return resolveNestedComplexStatement(environment, complexStmt);
			} else {
				StatementResult reasonResult = statement.execute(environment);
				return reasonResult.isSuccessful() ? null : statement;
			}
		}
		return statement;
	}

	/**
	 * @param environment
	 * @param complexStmt
	 * @return
	 */
	private static Statement resolveNestedComplexStatement(
			Environment environment, ComplexStatement complexStmt) {
		Statement finalReason = null;
		Statement _lhsReason = focusOnFalse(complexStmt.getLhsStatement(),
				environment);
		Statement _rhsReason = focusOnFalse(complexStmt.getRhsStatement(),
				environment);
		if (_lhsReason != null && _rhsReason != null) {
			finalReason = makeComplex(_lhsReason, _rhsReason,
					complexStmt.getOperator());
		} else if (_lhsReason != null && _rhsReason == null) {
			finalReason = _lhsReason;
		} else if (_lhsReason == null && _rhsReason != null) {
			finalReason = _rhsReason;
		}
		return finalReason;
	}

	/**
	 * @param outList
	 * @param stmt
	 */
	public static void listOutVariablesIterative(List<String> outList,
			Statement stmt) {
		if (stmt instanceof ComplexStatement) {
			ComplexStatement cmplx = (ComplexStatement) stmt;
			listOutVariablesIterative(outList, cmplx.getLhsStatement());
			listOutVariablesIterative(outList, cmplx.getRhsStatement());
		} else if (stmt instanceof VariableStatement) {
			outList.add(((VariableStatement) stmt).getVariableName());
		}
	}

	/**
	 * @param complexStatement
	 * @return
	 */
	public static String statement2String(Statement statement) {
		return statement.statement2String();
	}

	/**
	 * @param s
	 * @return
	 */
	public static Set<Statement> getNewStatementSet(Set<Statement> s) {
		return s == null ? new HashSet<Statement>() : new HashSet<Statement>(s);
	}

	/**
	 * @param capacity
	 * @return
	 */
	public static Set<Statement> getNewStatementSet(int capacity) {
		return new HashSet<Statement>(capacity);
	}

	/**
	 * @param command
	 * @return
	 * @throws InvalidStatementException
	 */
	public static Statement string2Statement(String command)
			throws InvalidStatementException {
		if (command == null || command.isEmpty()) {
			return EmptyStatement.getInstance();
		}
		List<String> tokens = parser.simplyParseSource(command);
		List<String> leftNotationTokens = parser.buildLeftNotation(tokens);
		Statement finalStatement = (Statement) parser
				.buildExecutionStack(leftNotationTokens);
		return finalStatement;
	}

	public static Statement compound(Collection<Statement> s, EOperator o) {
		Statement f = null;
		Iterator<Statement> siter = s.iterator();
		if (s == null || s.size() == 0) {
			f = EmptyStatement.getInstance();
		}
		if (s.size() == 1) {
			f = siter.next();
		}
		if (s.size() >= 2) {
			f = makeComplex(siter.next(), siter.next(), o);
		}
		while(siter.hasNext()) {
			f = makeComplex(f, siter.next(), o);
		}
		return f;
	}
	
	public static Set<Statement> resolveIntoSetStatement(ComplexStatement c){
		Set<Statement> statements = getNewStatementSet(null);
		Statement lhs = c.getLhsStatement();
		Statement rhs = c.getRhsStatement();
		EOperator op = c.getOperator();
		if(op.isBoolOperator()){
			statements.addAll(resolveIntoSetStatements0(lhs));
			statements.addAll(resolveIntoSetStatements0(rhs));
		}else{
			statements.add(c);
		}
		
		return statements;
	}

	private static Set<Statement> resolveIntoSetStatements0(Statement s) {
		if(s.isComplex()){
			Set<Statement> ss = resolveIntoSetStatement((ComplexStatement)s);
			return ss;
		} else{
			Set<Statement> newStatementSet = getNewStatementSet(2);
			newStatementSet.add(s);
			return newStatementSet;
		}
		
	}

	/**
	 * @author Dursun KOC
	 * 
	 */
	static class InternalStatementParser extends StatementParser {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.turkcelltech.comet.commonutil.utils.StatementParser#
		 * isComplexBeginOrComplexEnd(char, java.lang.String, int)
		 */
		@Override
		protected boolean isComplexBeginOrComplexEnd(char c, String command,
				int index) {
			return isComplexBegin(c, command, index)
					|| isComplexEnd(c, command, index);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.turkcelltech.comet.commonutil.utils.StatementParser#isComplexEnd
		 * (java.lang.String)
		 */
		@Override
		protected boolean isComplexEnd(String command) {
			return command.equals(Statement.END);
		}

		/**
		 * @param c
		 * @return
		 */
		private static boolean isComplexEnd(char c, String command, int index) {
			boolean isComplexEnd = (c == Statement.cEND && checkConformity(
					command, index, Statement.END));
			return isComplexEnd;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.turkcelltech.comet.commonutil.utils.StatementParser#isComplexBegin
		 * (char, java.lang.String, int)
		 */
		@Override
		protected boolean isComplexBegin(char c, String command, int index) {
			boolean isComplexBegin = (c == Statement.cBEGIN && checkConformity(
					command, index, Statement.BEGIN));
			return isComplexBegin;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.turkcelltech.comet.commonutil.utils.StatementParser#isComplexBegin
		 * (java.lang.String)
		 */
		@Override
		protected boolean isComplexBegin(String command) {
			return command.equals(Statement.BEGIN);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.turkcelltech.comet.commonutil.utils.StatementParser#isVariableEnd
		 * (java.lang.String, int)
		 */
		@Override
		protected boolean isVariableEnd(String command, int index) {
			boolean isVariableEnd = (command.charAt(index) == Statement.cVARDEF_END && checkConformity(
					command, index, Statement.VARDEF_END));
			return isVariableEnd;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.turkcelltech.comet.commonutil.utils.StatementParser#isVariableBegin
		 * (char, java.lang.String, int)
		 */
		@Override
		protected boolean isVariableBegin(char c, String command, int index) {
			boolean isVariableBegin = (c == Statement.cVARDEF_BEGIN && checkConformity(
					command, index, Statement.VARDEF_BEGIN));
			return isVariableBegin;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.turkcelltech.comet.commonutil.utils.StatementParser#isVariable
		 * (java.lang.String)
		 */
		@Override
		protected boolean isVariable(String command) {
			return command.startsWith(Statement.VARDEF_BEGIN)
					&& command.endsWith(Statement.VARDEF_END);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.turkcelltech.comet.commonutil.utils.StatementParser#isConstantEnd
		 * (java.lang.String, int)
		 */
		@Override
		protected boolean isConstantEnd(String command, int index) {
			boolean isConstantEnd = (command.charAt(index) == Statement.cCONST_END && checkConformity(
					command, index, Statement.CONST_END));
			return isConstantEnd;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.turkcelltech.comet.commonutil.utils.StatementParser#isConstantBegin
		 * (char, java.lang.String, int)
		 */
		@Override
		protected boolean isConstantBegin(char c, String command, int index) {
			boolean isConstantBegin = (c == Statement.cCONST_BEGIN && checkConformity(
					command, index, Statement.CONST_BEGIN));
			return isConstantBegin;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.turkcelltech.comet.commonutil.utils.StatementParser#isConstant
		 * (java.lang.String)
		 */
		@Override
		protected boolean isConstant(String command) {
			return command.startsWith(Statement.CONST_BEGIN)
					&& command.endsWith(Statement.CONST_END);

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.turkcelltech.comet.commonutil.utils.StatementParser#isDelim(char,
		 * java.lang.String, int)
		 */
		@Override
		protected boolean isDelim(char c, String command, int index) {
			boolean isDelim = (c == Statement.cDELIM && checkConformity(
					command, index, Statement.DELIM));
			return isDelim;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.turkcelltech.comet.commonutil.utils.StatementParser#isSymbol(
		 * java.lang.String)
		 */
		@Override
		protected boolean isSymbol(String token) {
			for (EOperator eOperator : EOperator.values()) {
				if (eOperator.isMe(token)) {
					return true;
				}
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.turkcelltech.comet.commonutil.utils.StatementParser#
		 * isDecisionOperator(java.lang.String)
		 */
		@Override
		protected boolean isDecisionOperator(String token) {
			return EOperator.AND.isMe(token) || EOperator.OR.isMe(token);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.turkcelltech.comet.commonutil.utils.StatementParser#isEmpty(java
		 * .lang.String)
		 */
		@Override
		protected boolean isEmpty(String token) {
			return token.equals(Statement.EMPTY_STATEMENT);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.turkcelltech.comet.commonutil.utils.StatementParser#
		 * combineExecutablesUsingSymbol
		 * (com.turkcelltech.comet.commonutil.utils.Executable,
		 * com.turkcelltech.comet.commonutil.utils.Executable, java.lang.String)
		 */
		@Override
		protected Executable combineExecutablesUsingSymbol(
				Executable rhsStatement, Executable lhsStatement, String token) {
			EOperator operator = OperatorFactory.getEOperator(token);
			return makeComplex((Statement) lhsStatement,
					(Statement) rhsStatement, operator);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.turkcelltech.comet.commonutil.utils.StatementParser#
		 * createVariableStatement(java.lang.String)
		 */
		@Override
		protected Executable createVariableStatement(String token) {
			Executable stmt;
			String variableName = token.substring(
					Statement.VARDEF_BEGIN.length(), token.length()
							- Statement.VARDEF_END.length());
			stmt = new VariableStatement(variableName);
			return stmt;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.turkcelltech.comet.commonutil.utils.StatementParser#
		 * createEmptyStatement()
		 */
		@Override
		protected Executable createEmptyStatement() {
			Executable stmt;
			stmt = EmptyStatement.getInstance();
			return stmt;
		}

		@Override
		protected Executable createConstantStatement(String token) {
			Executable stmt;
			stmt = ConstantStatement.buildFromString(token.substring(
					Statement.CONST_BEGIN.length(),
					token.length() - Statement.CONST_END.length()).trim());
			return stmt;
		}

	}

}
