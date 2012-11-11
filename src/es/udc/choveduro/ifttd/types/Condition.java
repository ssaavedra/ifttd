package es.udc.choveduro.ifttd.types;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;

import es.udc.choveduro.ifttd.EasyActivity;

abstract public class Condition implements Configurable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8599129405886266978L;
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

	protected void setConfig(String string, String value) {
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

	public static class DAO extends BaseDaoImpl<Condition, Accion> implements
			Dao<Condition, Accion> {

		protected DAO(Class<Condition> dataClass) throws SQLException {
			super(dataClass);
			// TODO Auto-generated constructor stub
		}

	}

	public static class Activity extends ConfigurablesListActivity<Condition> {
		protected ArrayList<Condition> fetchFromService() {
			return mService.getConditions();
		}

		protected void tellService(int position) {
			mService.setTransactionCondition(position);
		}
	}

}
