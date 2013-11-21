package com.aric.myel.exceptions;


/**
 * @author TTDKOC
 * 
 */
public class InvalidTypeForOperatorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7416730314634150979L;

	/**
	 * 
	 */
	public InvalidTypeForOperatorException() {
		super();
	}

	/**
	 * @param message
	 */
	public InvalidTypeForOperatorException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public InvalidTypeForOperatorException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidTypeForOperatorException(String message, Throwable cause) {
		super(message, cause);
	}

}
