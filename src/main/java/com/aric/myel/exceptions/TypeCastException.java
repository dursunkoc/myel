/**
 * 
 */
package com.aric.myel.exceptions;

/**
 * @author TTDKOC
 *
 */
public class TypeCastException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 89449667347259264L;

	/**
	 * 
	 */
	public TypeCastException() {
		super();
	}

	/**
	 * @param message
	 */
	public TypeCastException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public TypeCastException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public TypeCastException(String message, Throwable cause) {
		super(message, cause);
	}

}
