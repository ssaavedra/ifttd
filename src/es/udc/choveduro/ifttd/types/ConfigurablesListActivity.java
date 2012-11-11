package es.udc.choveduro.ifttd.types;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.MethodNotSupportedException;

import android.content.Context;
import android.os.Bundle;
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

import es.udc.choveduro.ifttd.EasyActivity;
import es.udc.choveduro.ifttd.R;
import es.udc.choveduro.ifttd.R.id;
import es.udc.choveduro.ifttd.R.layout;
import es.udc.choveduro.ifttd.R.menu;

public abstract class ConfigurablesListActivity extends EasyActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cond_or_cons_list);
		ListView l = (ListView) findViewById(R.id.configurable_list);
		
		
		
		l.setAdapter(new ItemAdapter(this, R.layout.cond_or_cons_item, getItems()));
		l.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View child, int position,
					long id) {
				onClickedAction(position);
			}
		});
	}
	
	/**
	 * @return the name to call for to the server
	 */
	abstract protected String whichItems();

	/**
	 * @return the items got by the server previously
	 */
	private ArrayList<Configurable> getItems() {
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.a, menu);
		return true;
	}

	public static class ItemAdapter extends ArrayAdapter<Configurable> {

		final static class ViewHolder {
			TextView name;
			ImageView img;
			ImageButton arrow;
		}

		private ConfigurablesListActivity ctx;

		public ItemAdapter(ConfigurablesListActivity context,
				int textViewResourceId, List<Configurable> objects) {
			super(context, textViewResourceId, objects);
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
					Toast.makeText(ctx, "Pressed arrow", Toast.LENGTH_SHORT).show();
				}

			});
			holder.name.setText(ctx.getItems().get(position).getName());
			holder.img.setImageResource(ctx.getItems().get(position)
					.getImageResource());

			return (item);
		}
	}
	
	public static class Callback implements CallbackIF {
		private ConfigurablesListActivity ctx;
		private int position;

		public Callback(ConfigurablesListActivity ctx, int position) {
			this.ctx = ctx;
			this.position = position;
		}

		@Override
		public void resultOK(String resultString, Bundle resultMap) {
			ctx.setResult(RESULT_OK, ctx.getIntent().putExtra("position", position));
			
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
