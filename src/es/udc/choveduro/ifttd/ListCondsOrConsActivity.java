package es.udc.choveduro.ifttd;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import es.udc.choveduro.ifttd.types.CallbackIF;
import es.udc.choveduro.ifttd.types.CondOrCons;
import es.udc.choveduro.ifttd.types.Condition;
import es.udc.choveduro.ifttd.types.Consequence;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;

public abstract class ListCondsOrConsActivity extends EasyActivity {

	protected ArrayList<CondOrCons> items;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cond_or_cons_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.a, menu);
        return true;
    }
    
    
    public static class ItemAdapter extends ArrayAdapter<CondOrCons>{
    	
    	final static class ViewHolder {
			TextView name;
			ImageView img;
			ImageButton arrow;
		}

		private ListCondsOrConsActivity ctx;

		public ItemAdapter(Context context, int textViewResourceId,
				List<CondOrCons> objects, ListCondsOrConsActivity ctx) {
			super(context, textViewResourceId, objects);
			this.ctx = ctx;
		}
		
		public View getView(final int position, View convertView, ViewGroup parent) {
			View item = convertView;
			ViewHolder holder;

			if (item == null) {
				LayoutInflater inflater = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				item = inflater.inflate(R.layout.cond_of_cons_item, null);

				holder = new ViewHolder();
				holder.name = (TextView) item
						.findViewById(R.id.name_item);
				holder.img = (ImageView) item
						.findViewById(R.id.image_item);
				holder.arrow = (ImageButton) item.findViewById(R.id.arrow_item);

				item.setTag(holder);
			} else {
				holder = (ViewHolder) item.getTag();
			}

			holder.arrow.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
				
				}
				
			});
			holder.name.setText(ctx.items.get(position).getName());
			holder.img.setImageResource(ctx.items.get(position).getImageResource());
			
			return (item);
		}
	}
 
}
