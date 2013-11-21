package com.aric.myel.exceptions;

/**
 *
 * @author TCDUKOC
 *
 */
public class KeyDoesNotExistsException extends RuntimeException {
	private String keyName;
	private static final long serialVersionUID = -1703215282378546787L;

	/**
	 *
	 * @param message
	 * @param key
	 */
	public KeyDoesNotExistsException(String message, String key) {
		super(message + "(key=" + key + ")");
		this.keyName = key;
	}

	/**
	 *
	 * @return
	 */
	public String getKeyName() {
		return this.keyName;
	}

	/**
	 * @param e
	 */
	public KeyDoesNotExistsException(Throwable e) {
		super(e);
	}
}