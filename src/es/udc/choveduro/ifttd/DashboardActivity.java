package es.udc.choveduro.ifttd;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
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
import es.udc.choveduro.ifttd.types.Accion;
import es.udc.choveduro.ifttd.types.CallbackIF;
import es.udc.choveduro.ifttd.types.Condition;
import es.udc.choveduro.ifttd.types.ConfigurablesListActivity;
import es.udc.choveduro.ifttd.types.Consequence;

public class DashboardActivity extends EasyActivity {

	private ProgressDialog loading;
	private OwlService mService;
	public static final String LOG_NAME = DashboardActivity.class.getName();

	List<Accion> loadedActions = new ArrayList<Accion>();
	Accion.Adapter loadedActionsAdapter = null;

	@Override
	public void onServiceConnected(OwlService mService) {
		this.mService = mService;
		loading.dismiss();
		try {
			loadedActions.addAll(mService.getAccionDao().queryForAll());
			loadedActionsAdapter.notifyDataSetChanged();
		} catch (SQLException e) {
		}
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
		List<Accion> values = new ArrayList<Accion>();

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
							Accion a = loadedActions.get(position);
							a.setStatus(Accion.STATUS.DISABLED);
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
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_actionbar, menu);
		return true;
	}

	final public static class Callback_Condition implements CallbackIF {
		private DashboardActivity ctx;

		public Callback_Condition(DashboardActivity ctx) {
			this.ctx = ctx;
		}

		@Override
		public void resultOK(String resultString, Bundle resultMap) {
			int currentCondition = resultMap.getInt("position");
			ctx.launchActivity(Consequence.Activity.class,
					ctx.new Callback_Consequence(ctx, currentCondition));
		}

		@Override
		public void resultCancel(String resultString, Bundle resultMap) {
			ctx.mService.unwind();
		}

	}

	final public class Callback_Consequence implements CallbackIF {
		private EasyActivity ctx;
		private int cond;

		public Callback_Consequence(EasyActivity ctx, int cond) {
			this.ctx = ctx;
			this.cond = cond;
		}

		@Override
		public void resultOK(String resultString, Bundle resultMap) {
			// Hoorray, refresh my array
			try {
				loadedActions.add(mService.getAccionDao().queryForId(
						resultMap.getInt("id")));
			} catch (SQLException e) {
				Log.e(LOG_NAME, "Err, could not get recently created action.",
						e);
			}
		}

		@Override
		public void resultCancel(String resultString, Bundle resultMap) {
			mService.unwind();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.add_icon) {
			Toast.makeText(this, "Añadir pulsado", Toast.LENGTH_SHORT).show();

			launchActivity(Condition.Activity.class,
					new Callback_Condition(this));

			return true;
		}
		return false;
	}

}
