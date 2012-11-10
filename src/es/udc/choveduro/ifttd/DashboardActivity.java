package es.udc.choveduro.ifttd;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import es.udc.choveduro.ifttd.service.OwlService;
import es.udc.choveduro.ifttd.service.OwlService.OwlBinder;
import es.udc.choveduro.ifttd.types.Accion;
import es.udc.choveduro.ifttd.types.CallbackIF;
import es.udc.choveduro.ifttd.types.Condition;
import es.udc.choveduro.ifttd.types.ConditionSelectorActivity;
import es.udc.choveduro.ifttd.types.Consequence;

public class DashboardActivity extends EasyActivity {

	protected OwlService mService;
	protected boolean mBound;
	private ProgressDialog loading;
	public static final String LOG_NAME = DashboardActivity.class.getName();

	List<Accion> loadedActions = new ArrayList<Accion>();
	Accion.Adapter loadedActionsAdapter = null;

	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			OwlBinder binder = (OwlBinder) service;
			mService = binder.getService();
			mBound = true;
			loading.dismiss();
			try {
				loadedActions.addAll(mService.getAccionDao().queryForAll());
				loadedActionsAdapter.notifyDataSetChanged();
			} catch (SQLException e) {
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);

		// We should start the service and bind to it.
		Intent intent = new Intent(this, OwlService.class);
		this.startService(intent);

		// Bind to service
		bindService(intent, mConnection, Context.BIND_ADJUST_WITH_ACTIVITY
				| Context.BIND_AUTO_CREATE);

		// Put a progressbar until we are actually connected to the service.
		loading = ProgressDialog.show(this, "Initializing",
				"Loading activities...");
		loading.setCancelable(false);

		loadedActionsAdapter = new Accion.Adapter(this,
				R.layout.this_and_then_item, loadedActions);

		ListView list = (ListView) findViewById(R.id.actionListView);
		list.setAdapter((ListAdapter) loadedActionsAdapter);
		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View parent,
					final int position, long id) {
				// TODO: Display dialog
				AlertDialog.Builder db = new AlertDialog.Builder(
						DashboardActivity.this);
				db.setCancelable(false);
				db.setNegativeButton("Cancel", new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				});

				db.setPositiveButton("Delete", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							mService.getAccionDao().delete(
									loadedActions.get(position));
						} catch (SQLException e) {
							Log.e(LOG_NAME, "Exception while deleting action.",
									e);
						}
						loadedActions.remove(position);
						dialog.dismiss();
					}
				});
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_actionbar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.add_icon) {
			Toast.makeText(this, "Añadir pulsado", Toast.LENGTH_SHORT).show();
			final Condition cond;
			final Consequence cons;

			this.launchActivity(ConditionSelectorActivity.class,
					new CallbackIF() {

						@Override
						public void resultOK(String resultString,
								Bundle resultMap) {
							cond = null;
							DashboardActivity.this.addActionStep1(cond);
						}

						@Override
						public void resultCancel(String resultString,
								Bundle resultMap) {
							DashboardActivity.this.addCancel();
						}

					});
		}
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unbindService(mConnection);
	}

	protected void addCancel() {
		// TODO Auto-generated method stub

	}

	protected void addActionStep1(Condition cond) {
		// TODO Auto-generated method stub

	}

}
