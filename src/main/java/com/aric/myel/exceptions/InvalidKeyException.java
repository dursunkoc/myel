package com.aric.myel.exceptions;


/**
 * 
 * @author TCDUKOC
 * 
 */
public class InvalidKeyException extends RuntimeException {
	private static final long serialVersionUID = 4567694698232121480L;

	public InvalidKeyException(String message, String key) {
		super(message + " (key=" + key+")");
	}
}
