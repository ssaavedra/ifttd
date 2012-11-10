package es.udc.choveduro.ifttd.types;

import java.util.HashMap;

import android.content.Context;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import es.udc.choveduro.ifttd.EasyActivity;

@DatabaseTable(tableName = "consequences")
public abstract class Consequence extends CondOrCons {

	@DatabaseField(id = true, foreign = true)
	private Accion belong_to;

	@DatabaseField
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
}
