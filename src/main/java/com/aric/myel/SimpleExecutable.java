/**
 * 
 */
package com.aric.myel;

/**
 * @author TTDKOC
 * 
 */
public class SimpleExecutable implements Executable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6478162677448702874L;

	private Object value;

	/**
	 * @param value
	 */
	public SimpleExecutable(Object value) {
		this.value = value;
	}

	/**
	 * @return
	 */
	public Object getValue() {
		return value;
	}
}
