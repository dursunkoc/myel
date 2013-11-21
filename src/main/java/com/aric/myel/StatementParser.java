/**
 * 
 */
package com.aric.myel;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.aric.myel.exceptions.InvalidStatementException;

/**
 * @author TTDKOC
 *
 */
public abstract class StatementParser {
	/**
	 * @param leftNotationTokens
	 * @return
	 * @throws InvalidStatementException
	 */
	public Executable buildExecutionStack(List<String> leftNotationTokens)
			throws InvalidStatementException {
		Executable stmt;
		Stack<Executable> stmtStack = new Stack<Executable>();
		for (String token : leftNotationTokens) {
			if (isSymbol(token)) {
				Executable rhsStatement = stmtStack.pop();
				Executable lhsStatement = stmtStack.pop();
				stmt = combineExecutablesUsingSymbol(rhsStatement,
						lhsStatement, token);
			} else if (isVariable(token)) {
				stmt = createVariableStatement(token);
			} else if (isConstant(token)) {
				stmt = createConstantStatement(token);
			} else if (isEmpty(token)) {
				stmt = createEmptyStatement();
			} else {
				throw new InvalidStatementException(
						"Broken statement. token :'" + token + "'");
			}
			stmtStack.push(stmt);
		}
		return stmtStack.pop();
	}

	/**
	 * @return
	 */
	protected abstract Executable createEmptyStatement();

	/**
	 * @param token
	 * @return
	 */
	protected abstract Executable createConstantStatement(String token);

	/**
	 * @param token
	 * @return
	 */
	protected abstract Executable createVariableStatement(String token);

	/**
	 * @param rhsStatement
	 * @param lhsStatement
	 * @param token
	 * @return
	 */
	protected abstract Executable combineExecutablesUsingSymbol(
			Executable rhsStatement, Executable lhsStatement, String token);

	/**
	 * @param command
	 * @return
	 */
	public List<String> simplyParseSource(String command) {
		StringBuffer buffer = new StringBuffer();
		List<String> tokens = new ArrayList<String>();

		for (int i = 0; i < command.length(); i++) {
			char c = command.charAt(i);
			if (isDelim(c, command, i)) {
				simplyAddToken(buffer, tokens);
				buffer = new StringBuffer();
			} else if (isConstantBegin(c, command, i)) {
				while (!isConstantEnd(command, i)) {
					buffer.append(command.charAt(i));
					i = i + 1;
				}
				buffer.append(command.charAt(i));
			} else if (isVariableBegin(c, command, i)) {
				while (!isVariableEnd(command, i)) {
					buffer.append(command.charAt(i));
					i = i + 1;
				}
				buffer.append(command.charAt(i));
			} else if (isComplexBeginOrComplexEnd(c, command, i)) {
				bracketAddToken(buffer, c, tokens);
				buffer = new StringBuffer();
			} else {
				buffer.append(c);
			}
		}
		if (buffer.length() > 0) {
			simplyAddToken(buffer, tokens);
		}
		return tokens;
	}

	protected abstract boolean isComplexBeginOrComplexEnd(char c,
			String command, int index);

	/**
	 * @param command
	 * @return
	 */
	private boolean isComplexBeginOrComplexEnd(String command) {
		return isComplexBegin(command) || isComplexEnd(command);
	}

	/**
	 * @param command
	 * @return
	 */
	protected abstract boolean isComplexEnd(String command);

	/**
	 * @param c
	 * @return
	 */
	protected abstract boolean isComplexBegin(char c, String command, int index);

	/**
	 * @param command
	 * @return
	 */
	protected abstract boolean isComplexBegin(String command);

	/**
	 * @param command
	 * @param i
	 * @return
	 */
	protected abstract boolean isVariableEnd(String command, int index);

	/**
	 * @param c
	 * @return
	 */
	protected abstract boolean isVariableBegin(char c, String command, int index);

	/**
	 * @param command
	 * @return
	 */
	protected abstract boolean isVariable(String command);

	/**
	 * @param command
	 * @param index
	 * @return
	 */
	protected abstract boolean isConstantEnd(String command, int index);

	/**
	 * @param c
	 * @return
	 */
	protected abstract boolean isConstantBegin(char c, String command, int index);

	/**
	 * @param command
	 * @return
	 */
	protected abstract boolean isConstant(String command);

	/**
	 * @param c
	 * @return
	 */
	protected abstract boolean isDelim(char c, String command, int index);

	/**
	 * @param command
	 * @param index
	 * @param conformityStr
	 * @return
	 */
	protected static boolean checkConformity(String command, int index,
			String conformityStr) {
		return command.subSequence(index, index + conformityStr.length())
				.equals(conformityStr);
	}

	/**
	 * any straightForward Token
	 * 
	 * @param buffer
	 * @param tokens
	 */
	private static void simplyAddToken(StringBuffer buffer, List<String> tokens) {
		if (buffer.length() > 0) {
			tokens.add(buffer.toString());
		}
	}

	/**
	 * Tokenize brackets
	 * 
	 * @param buffer
	 * @param c
	 * @param tokens
	 */
	private static void bracketAddToken(StringBuffer buffer, char c,
			List<String> tokens) {
		simplyAddToken(buffer, tokens);
		tokens.add(Character.toString(c));
	}

	public List<String> buildLeftNotation(List<String> tokens) {
		List<String> leftNotationTokens = new ArrayList<String>();
		// nothing to build
		if (tokens.size() == 0) {
			return leftNotationTokens;
		}

		Stack<String> stack = new Stack<String>();
		stack.add("");
		for (String token : tokens) {
			if (isComplexEnd(token)) {
				while (!isComplexBegin(token)) {
					token = stack.pop();
					if (!isComplexBegin(token)) {
						leftNotationTokens.add(token);
					}
				}
			} else if (isSymbol(token)) {
				if (isDecisionOperator(token)) {
					while (isStackPopAbleForExecution(stack)) {
						leftNotationTokens.add(stack.pop());
					}
				}
				stack.push(token);
			} else if (isComplexBegin(token)) {
				stack.push(token);
			} else {
				leftNotationTokens.add(token);
			}
		}
		while (isStackPopAbleForExecution(stack)) {
			String stackToken = stack.pop();
			leftNotationTokens.add(stackToken);
		}

		return leftNotationTokens;
	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	protected abstract boolean isSymbol(String token);

	/**
	 * 
	 * @param token
	 * @return
	 */
	protected abstract boolean isDecisionOperator(String token);

	/**
	 * 
	 * @param stack
	 * @return
	 */
	private boolean isStackPopAbleForExecution(Stack<String> stack) {
		return !(stack.isEmpty()) && !(stack.lastElement() == null)
				&& !(isComplexBeginOrComplexEnd(stack.lastElement()))
				&& !(stack.lastElement().trim().equals(""));
	}

	/**
	 * @param token
	 * @return
	 */
	protected abstract boolean isEmpty(String token);
}
