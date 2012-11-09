package es.udc.choveduro.ifttd.types;

import java.util.HashMap;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable( tableName = "conditions" )
abstract public class Condition {

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
	 * Condition name (populated by subclass)
	 */
	abstract public String getName();

	/**
	 * Condition image resource
	 */
	abstract public int getImageResource();

}