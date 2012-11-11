package es.udc.choveduro.ifttd.types;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.MethodNotSupportedException;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;

import es.udc.choveduro.ifttd.DashboardActivity;
import es.udc.choveduro.ifttd.EasyActivity;
import es.udc.choveduro.ifttd.R;
import es.udc.choveduro.ifttd.R.id;
import es.udc.choveduro.ifttd.R.layout;
import es.udc.choveduro.ifttd.R.menu;
import es.udc.choveduro.ifttd.service.OwlService;
import es.udc.choveduro.ifttd.service.OwlService.OwlBinder;

public abstract class ConfigurablesListActivity<T extends Configurable> extends
		EasyActivity {

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
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cond_or_cons_list);
		ListView l = (ListView) findViewById(R.id.configurable_list);

		l.setAdapter(new ItemAdapter<T>(this, R.layout.cond_or_cons_item,
				getItems()));
		l.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View child,
					int position, long id) {
				onClickedAction(position);
			}
		});
	}

	/**
	 * @return the items got from the server
	 */
	abstract protected ArrayList<T> fetchFromService();

	/**
	 * Make server aware of the selected position on graceful exit.
	 * 
	 * @param position
	 */
	abstract protected void tellService(int position);

	protected void tellUnwind() {
		mService.unwind();
	}

	/**
	 * @return the items got by the server previously
	 */
	private ArrayList<T> getItems() {
		return fetchFromService();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.a, menu);
		return true;
	}

	public static class ItemAdapter<T> extends ArrayAdapter<T> {

		final static class ViewHolder {
			TextView name;
			ImageView img;
			ImageButton arrow;
		}

		private ConfigurablesListActivity<? extends Configurable> ctx;

		public ItemAdapter(
				ConfigurablesListActivity<? extends Configurable> context,
				int textViewResourceId, ArrayList<T> arrayList) {
			super(context, textViewResourceId, arrayList);
			this.ctx = context;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View item = convertView;
			ViewHolder holder;

			if (item == null) {
				LayoutInflater inflater = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				item = inflater.inflate(R.layout.cond_or_cons_item, null);

				holder = new ViewHolder();
				holder.name = (TextView) item.findViewById(R.id.name_item);
				holder.img = (ImageView) item.findViewById(R.id.image_item);
				holder.arrow = (ImageButton) item.findViewById(R.id.arrow_item);

				item.setTag(holder);
			} else {
				holder = (ViewHolder) item.getTag();
			}

			holder.arrow.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(ctx, "Pressed arrow", Toast.LENGTH_SHORT)
							.show();
				}

			});
			holder.name.setText(ctx.getItems().get(position).getName());
			holder.img.setImageResource(ctx.getItems().get(position)
					.getImageResource());

			return (item);
		}
	}

	public static class Callback implements CallbackIF {
		private ConfigurablesListActivity<? extends Configurable> ctx;
		private int position;

		public Callback(ConfigurablesListActivity<? extends Configurable> ctx, int position) {
			this.ctx = ctx;
			this.position = position;
		}

		@Override
		public void resultOK(String resultString, Bundle resultMap) {
			ctx.tellService(position);
			ctx.setResult(RESULT_OK,
					ctx.getIntent().putExtra("position", position));

		}

		@Override
		public void resultCancel(String resultString, Bundle resultMap) {
			ctx.setResult(RESULT_CANCELED);
			ctx.finish();
		}
	}

	protected void onClickedAction(int position) {
		getItems().get(position).configure(this, new Callback(this, position));
	}

}
