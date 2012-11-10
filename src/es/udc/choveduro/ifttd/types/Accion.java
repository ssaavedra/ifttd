package es.udc.choveduro.ifttd.types;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import es.udc.choveduro.ifttd.R;

@DatabaseTable()
public class Accion {

	/**
	 * An adapter for ListViews.
	 * 
	 * @author ssaavedra
	 * 
	 */
	final public static class Adapter extends ArrayAdapter<Accion> {

		final static class ViewHolder {
			TextView cond_name, cons_name;
			TextView cond_text, cons_text;
			ImageView cond_img, cons_img;
		}

		public Adapter(Context ctx, int textViewResID) {
			super(ctx, textViewResID);
		}

		public Adapter(Context ctx, int textViewResID, List<Accion> items) {
			super(ctx, textViewResID, items);
		}

		public Adapter(Context ctx, int textViewResID, Accion items[]) {
			super(ctx, textViewResID, items);
		}

		// TODO: Implement all the ArrayAdapter<T> constructors. :)

		/**
		 * getView redesigned to get what we want
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			View item = convertView;
			ViewHolder holder;

			if (item == null) {
				LayoutInflater inflater = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				item = inflater.inflate(R.layout.this_and_then_item, null);

				holder = new ViewHolder();
				holder.cond_name = (TextView) item
						.findViewById(R.id.textView_condition_title);
				holder.cons_name = (TextView) item
						.findViewById(R.id.textView_consequence_title);
				holder.cond_text = (TextView) item
						.findViewById(R.id.textView_condition_subtitle);
				holder.cons_text = (TextView) item
						.findViewById(R.id.textView_consequence_subtitle);
				holder.cond_img = (ImageView) item
						.findViewById(R.id.imageButton_condition);
				holder.cons_img = (ImageView) item
						.findViewById(R.id.imageButton_consequence);

				item.setTag(holder);
			} else {
				holder = (ViewHolder) item.getTag();
			}

			Condition cond = getItem(position).getCond();
			Consequence cons = getItem(position).getConsec();

			holder.cond_name.setText(cond.getName());
			holder.cons_name.setText(cons.getName());
			holder.cond_text.setText(cond.getShortDesc());
			holder.cons_text.setText(cons.getShortDesc());
			holder.cond_img.setImageResource(cond.getImageResource());
			holder.cons_img.setImageResource(cons.getImageResource());

			return (item);
		}
	}

	/**
	 * The status which the action can have
	 */
	public enum STATUS {
		ENABLED, DISABLED
	};

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = true, foreign = true)
	private Condition cond;

	@DatabaseField(canBeNull = true, foreign = true)
	private Consequence consec;

	@DatabaseField()
	private STATUS status = STATUS.ENABLED;

	/**
	 * This exists so the DAO can act correctly.
	 */
	public Accion() {

	}

	public Accion(Condition cond, Consequence consec) {
		this.cond = cond;
		this.consec = consec;
	}

	/**
	 * @return the cond
	 */
	public Condition getCond() {
		return cond;
	}

	/**
	 * @return the consec
	 */
	public Consequence getConsec() {
		return consec;
	}

	/**
	 * @return the status
	 */
	public STATUS getStatus() {
		return status;
	}

}
