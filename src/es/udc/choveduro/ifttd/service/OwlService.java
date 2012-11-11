package es.udc.choveduro.ifttd.service;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseService;
import com.j256.ormlite.dao.Dao;

import es.udc.choveduro.ifttd.conditions.OnLocationCondition;
import es.udc.choveduro.ifttd.conditions.OnTimeCondition;
import es.udc.choveduro.ifttd.consequences.ShowNotification;
import es.udc.choveduro.ifttd.types.Accion;
import es.udc.choveduro.ifttd.types.Condition;
import es.udc.choveduro.ifttd.types.Consequence;

public class OwlService extends OrmLiteBaseService<DatabaseHelper> {
	private static final String TAG = "IFTTD-OwlService";

	IBinder mBinder = new OwlBinder();

	DatabaseHelper dbh;

	ArrayList<Condition> conditionCache = new ArrayList<Condition>();
	ArrayList<Consequence> consequenceCache = new ArrayList<Consequence>();
	Condition transactionCond;
	Consequence transactionConsequence;
	Accion transactionAction;
	boolean transactionInProcess = false;

	public class OwlBinder extends Binder {
		public OwlService getService() {
			return OwlService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		dbh = new DatabaseHelper(this);
		Log.i(TAG, "Creating service");
		conditionCache.add(new OnTimeCondition());
		conditionCache.add(new OnLocationCondition());
		consequenceCache.add(new ShowNotification());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		dbh.close();
		Log.i(TAG, "Destroying service");
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "Service started.", Toast.LENGTH_SHORT).show();
		return START_STICKY;
	}

	public Dao<Accion, Integer> getAccionDao() throws SQLException {
		return dbh.getAccionDao();
	}

	public ArrayList<Condition> getConditions() {
		return conditionCache;
	}

	public ArrayList<Consequence> getConsequences() {
		return consequenceCache;
	}

	public void startTransaction() {
		transactionAction = new Accion();
		transactionInProcess = true;
	}

	public void setTransactionCondition(int c) {
		transactionCond = conditionCache.get(c);
	}

	public void setTransactionConsequence(int c) {
		transactionConsequence = consequenceCache.get(c);
	}

	public void unwind() {
		transactionCond = null;
		transactionConsequence = null;
		transactionAction = null;
		transactionInProcess = false;
	}

	public void commitTransaction() {
		transactionAction = new Accion();
		transactionAction.setConsec(transactionConsequence);
		transactionAction.setCond(transactionCond);
		try {
			dbh.getAccionDao().create(transactionAction);
			// dbh.getConditionDao().create(transactionCond);
			// dbh.getConsequenceDao().create(transactionConsequence);
		} catch (SQLException e) {
			Log.e(TAG, "SQL Exception when creating things...", e);

			try {
				dbh.getAccionDao().delete(transactionAction);
				// dbh.getConditionDao().delete(transactionCond);
				// dbh.getConsequenceDao().delete(transactionConsequence);
			} catch (SQLException e1) {
			} finally {
				Log.e(TAG, "SQL Exception again on deletion.");
				consequenceCache.remove(transactionConsequence);
				conditionCache.remove(transactionCond);
				try {
					// Create new instances on the cache of the same type ;-)
					consequenceCache.add(transactionConsequence.getClass()
							.newInstance());
					conditionCache
							.add(transactionCond.getClass().newInstance());
				} catch (Exception e1) {
				}
			}
		}
	}
}