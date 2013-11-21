/**
 * 
 */
package com.aric.myel.exceptions;

/**
 * @author TTDKOC
 *
 */
public class IllegalObjectTypeForReturnTypeValue extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6633345294841366297L;

	/**
	 * 
	 */
	public IllegalObjectTypeForReturnTypeValue() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public IllegalObjectTypeForReturnTypeValue(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public IllegalObjectTypeForReturnTypeValue(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public IllegalObjectTypeForReturnTypeValue(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param objectType
	 * @param returnType
	 * @param returnValue
	 */
	public IllegalObjectTypeForReturnTypeValue(String message,
			String objectType, String returnType, String returnValue) {
		super(message + " (objectType=" + objectType + ") and (returnType="
				+ returnType + ") and (returnValue=" + returnValue + ").");
	}

}
