/**
 * 
 */
package com.aric.myel.exceptions;


/**
 * @author TTDKOC
 *
 */
public class InvalidStatementException extends RuntimeException {

	public InvalidStatementException(String message) {
		super(message);
	}

	public InvalidStatementException(Throwable t) {
		super(t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4908677456293483795L;

}
