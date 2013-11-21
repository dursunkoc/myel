package com.aric.myel.statements.components;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.aric.myel.Config;
import com.aric.myel.exceptions.InvalidKeyException;
import com.aric.myel.exceptions.KeyDoesNotExistsException;

/**
 *
 * @author TCDUKOC
 *
 */
public class Environment implements Serializable {
	private static final long serialVersionUID = 723830546285398753L;
	private Map<String, Object> environment;

	/**
	 *
	 * @throws InvalidKeyException
	 */
	private void checkTHIS() {
		if (this.environment == null) {
			environment = new HashMap<String, Object>();
		}
		if (this.environment.containsKey(Config.ENVIRONMENT_SELF_REFENCE)) {
			if (this.environment.get(Config.ENVIRONMENT_SELF_REFENCE) != this)
				throw new InvalidKeyException(
						"Environment cannot contain a key named \"environment\" with a different value then this.",
						Config.ENVIRONMENT_SELF_REFENCE);
		} else {
			this.environment.put(Config.ENVIRONMENT_SELF_REFENCE, this);
		}

	}


	public Iterator<String> iterateEnvironment(){
		Iterator<String> it = environment.keySet().iterator();
		return it;
	}

	/**
	 *
	 * @throws InvalidKeyException
	 */
	public Environment() throws InvalidKeyException {
		checkTHIS();
	};

	/**
	 *
	 * @param environment
	 * @throws InvalidKeyException
	 */
	public Environment(Map<String, Object> environment)
			throws InvalidKeyException {
		setEnvironment(environment);
	}

	/**
	 *
	 * @return
	 */
	public Map<String, Object> getEnvironment() {
		return environment;
	}

	/**
	 *
	 * @param environment
	 * @throws InvalidKeyException
	 */
	public final synchronized void setEnvironment(
			Map<String, Object> environment) throws InvalidKeyException {
		this.environment = environment;
		checkTHIS();
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @throws InvalidKeyException
	 * @throws InvalidKeyException
	 */
	public synchronized void addVariable(String key, Object value)
			throws InvalidKeyException {
		if (key == null) {
			throw new InvalidKeyException(
					"Key cannot be null while adding a new variable", key);
		}
		if (isVariableExists(key)) {
			throw new InvalidKeyException("Key Already exists!", key);
		}
		this.environment.put(key, value);
		checkTHIS();
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @throws InvalidKeyException
	 */
	public synchronized void addOrUpdateVariable(String key, Object value)
			throws InvalidKeyException {
		if (isVariableExists(key)) {
			updateVariable(key, value);
		} else {
			addVariable(key, value);
		}
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @throws InvalidKeyException
	 * @throws InvalidKeyException
	 */
	public synchronized void updateVariable(String key, Object value)
			throws InvalidKeyException, InvalidKeyException {
		if (isVariableExists(key)) {
			removeVariable(key);
		}
		addVariable(key, value);
	}

	/**
	 *
	 * @param key
	 * @throws InvalidKeyException
	 */
	public synchronized void removeVariable(String key)
			throws InvalidKeyException {
		environment.remove(key);
		checkTHIS();
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public boolean isVariableExists(String key) {
		return environment.containsKey(key);
	}

	/**
	 *
	 * @param key
	 * @return
	 * @throws KeyDoesNotExistsException
	 */
	public Object getVariable(String key) {
		if (key == null) {
			return null;
		}
		if (!isVariableExists(key)) {
			throw new KeyDoesNotExistsException(
					"Key does not exist in the environment!", key);
		}
		return this.getUncheckedVariable(key);
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public Object getUncheckedVariable(String key) {
		return environment.get(key);
	}

	/**
	 *
	 * @throws InvalidKeyException
	 */
	public synchronized void resetEnvironment() throws InvalidKeyException {
		environment.clear();
		checkTHIS();
	}

	/**
	 *
	 * @param keysToPreserve
	 * @throws InvalidKeyException
	 */
	public synchronized void resetEnvironment(String keysToPreserve)
			throws InvalidKeyException {
		Iterator<String> iterator = environment.keySet().iterator();
		String currentKey;
		while (iterator.hasNext()) {
			currentKey = iterator.next().toString();
			if (!currentKey.startsWith(keysToPreserve)) {
				environment.remove(currentKey);
			}
		}
		checkTHIS();
	}
}
