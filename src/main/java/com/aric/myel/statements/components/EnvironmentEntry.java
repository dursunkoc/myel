/**
 *
 */
package com.aric.myel.statements.components;

import java.util.Map.Entry;

/**
 * @author Dursun KOC
 *
 */
public class EnvironmentEntry implements Entry<String, Object> {
	private String key;
	private Object value;

	/* (non-Javadoc)
	 * @see java.util.Map.Entry#getKey()
	 */
	@Override
	public String getKey() {
		return key;
	}

	/* (non-Javadoc)
	 * @see java.util.Map.Entry#getValue()
	 */
	@Override
	public Object getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see java.util.Map.Entry#setValue(java.lang.Object)
	 */
	@Override
	public Object setValue(Object value) {
		this.value = value;
		return this.value;
	}

}
