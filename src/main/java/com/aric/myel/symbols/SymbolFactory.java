/**
 * 
 */
package com.aric.myel.symbols;

import com.aric.myel.exceptions.InvalidStatementException;


/**
 * @author TTDKOC
 * 
 */
public class SymbolFactory {
	private static final Symbol PLUS = PlusSymbol.getInstance();
	private static final Symbol MINUS = MinusSymbol.getInstance();
	private static final Symbol MULTIPLY = MultiplicationSymbol.getInstance();
	private static final Symbol DIVIDE = DivisionSymbol.getInstance();
	private static final Symbol FORMATTO = FormatToSymbol.getInstance();
	private static final Symbol[] _registry = new Symbol[] { PLUS, MINUS,
			MULTIPLY, DIVIDE, FORMATTO };

	public static Symbol getSymbol(String symbol) {
		for (Symbol sym : _registry) {
			if (sym.isMe(symbol)) {
				return sym;
			}
		}
		throw new InvalidStatementException("Symbol '" + symbol
				+ "' is not registered.");
	}

	/**
	 * @param symbol
	 * @return
	 */
	public static boolean isSymbol(String symbol) {
		for (Symbol sym : _registry) {
			if (sym.isMe(symbol)) {
				return true;
			}
		}
		return false;
	}

}
