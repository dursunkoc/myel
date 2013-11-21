/**
 * 
 */
package com.aric.myel.statements.components;

/**
 * @author TTDKOC
 *
 */
public class InvalidTypeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5236005441304978166L;

	/**
	 * 
	 */
	public InvalidTypeException() {
		super();
	}

	/**
	 * @param message
	 */
	public InvalidTypeException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public InvalidTypeException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidTypeException(String message, Throwable cause) {
		super(message, cause);
	}

}
