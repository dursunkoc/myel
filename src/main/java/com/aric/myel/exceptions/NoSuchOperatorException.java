package com.aric.myel.exceptions;

public class NoSuchOperatorException extends RuntimeException {
	private static final long serialVersionUID = 3742562254199726101L;
	private String operatorName;

	/**
	 * 
	 */
	public NoSuchOperatorException() {

	}

	/**
	 * 
	 * @param message
	 */
	public NoSuchOperatorException(String message) {
		super(message);
	}

	/**
	 * 
	 * @return
	 */
	public String getOperatorName() {
		return this.operatorName;
	}

	public NoSuchOperatorException(String message, String operatorName) {
		super(message);
		this.operatorName = operatorName;
	}

}
