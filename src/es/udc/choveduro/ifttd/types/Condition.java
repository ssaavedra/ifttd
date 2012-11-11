package es.udc.choveduro.ifttd.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import es.udc.choveduro.ifttd.EasyActivity;

abstract public class Condition implements Configurable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8599129405886266978L;
	private HashMap<String, Serializable> config = new HashMap<String, Serializable>();

	/**
	 * @return the config
	 */
	protected final HashMap<String, Serializable> getConfig() {
		return config;
	}

	/**
	 * @param config
	 *            the config to set
	 */
	protected final void setConfig(HashMap<String, Serializable> config) {
		this.config = config;
	}

	protected void setConfig(String string, Serializable value) {
		this.config.put(string, value);

	}

	/**
	 * Condition name (populated by subclass)
	 */
	abstract public String getName();

	/**
	 * Condition image resource
	 */
	abstract public int getImageResource();

	/**
	 * Short description.
	 */
	abstract public String getShortDesc();

	/**
	 * Configure the condition through an activity
	 */
	public abstract void configure(EasyActivity ctx, CallbackIF callback);

	public static class Activity extends ConfigurablesListActivity<Condition> {
		protected ArrayList<Condition> fetchFromService() {
			return mService.getConditions();
		}

		protected void tellService(int position) {
			mService.setTransactionCondition(position);
		}
	}

}
