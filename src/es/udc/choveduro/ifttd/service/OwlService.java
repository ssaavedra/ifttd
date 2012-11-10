package es.udc.choveduro.ifttd.service;

import java.sql.SQLException;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseService;
import com.j256.ormlite.dao.Dao;

import es.udc.choveduro.ifttd.types.Accion;
import es.udc.choveduro.ifttd.types.Condition;
import es.udc.choveduro.ifttd.types.Consequence;

public class OwlService extends OrmLiteBaseService<DatabaseHelper> {
	private static final String TAG = "IFTTD-OwlService";

	IBinder mBinder = new OwlBinder();
	
	DatabaseHelper dbh;

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
	
	public Dao<Condition, Accion> getConditionDao() throws SQLException {
		return dbh.getConditionDao();
	}
	
	public Dao<Consequence, Accion> getConsequenceDao() throws SQLException {
		return dbh.getConsequenceDao();
	}

}