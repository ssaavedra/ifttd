package es.udc.choveduro.ifttd.service;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import es.udc.choveduro.ifttd.types.Accion;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	
	private static final String DATABASE_NAME = "ifttd.db";
	private final static int DATABASE_VERSION = 1;
	private final String LOG_NAME = getClass().getName();

	private Dao<Accion, Integer> accionDao;

	/**
	 * @return the accionDao
	 * @throws java.sql.SQLException
	 */
	public final Dao<Accion, Integer> getAccionDao() throws java.sql.SQLException {
		if(accionDao == null)
			accionDao = getDao(Accion.class);
		return accionDao;
	}

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Accion.class);
		} catch (SQLException e) {
			Log.e(LOG_NAME, "OMRLite could not create new tables", e);
		} catch (java.sql.SQLException e) {
			Log.e(LOG_NAME, "Java exception: Could not create new tables", e);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource, int arg2, int arg3) {
		// TODO

	}

}
