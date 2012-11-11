package es.udc.choveduro.ifttd.types;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import es.udc.choveduro.ifttd.EasyActivity;

public abstract class Consequence implements Configurable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4520633075659609740L;
	private HashMap<String, String> config = new HashMap<String, String>();

	/**
	 * @return the config
	 */
	protected final HashMap<String, String> getConfig() {
		return config;
	}

	/**
	 * @param config
	 *            the config to set
	 */
	protected final void setConfig(HashMap<String, String> config) {
		this.config = config;
	}

	/**
	 * Consequence name (populated by subclass)
	 */
	abstract public String getName();

	/**
	 * Consequence image resource
	 */
	abstract public int getImageResource();

	/**
	 * Short description.
	 */
	abstract public String getShortDesc();

	/**
	 * Executes the consequence action.
	 */
	abstract public void run(Context c);

	/**
	 * Configure the condition through an activity
	 */
	public abstract void configure(EasyActivity ctx, CallbackIF callback);

	public static class Activity extends ConfigurablesListActivity<Consequence> {
		protected ArrayList<Consequence> fetchFromService() {
			return mService.getConsequences();
		}

		protected void tellService(int position) {
			mService.setTransactionConsequence(position);
		}
	}
}
