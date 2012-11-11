package es.udc.choveduro.ifttd.conditions;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;
import es.udc.choveduro.ifttd.EasyActivity;
import es.udc.choveduro.ifttd.R;
import es.udc.choveduro.ifttd.types.CallbackIF;
import es.udc.choveduro.ifttd.types.Condition;
import es.udc.choveduro.ifttd.types.ConfigurationActivity;

final public class OnTimeCondition extends Condition {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2086273518215948802L;

	/**
	 * Beware: Activity is not static, so it can refer to Condition.this
	 * 
	 * @author ssaavedra
	 * 
	 */
	public static class Activity extends ConfigurationActivity {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState, R.layout.condition_on_time_config);

			((DatePicker) findViewById(R.id.ontime_date_picker)).init(
					configCalendar.get(Calendar.DATE),
					configCalendar.get(Calendar.MONTH),
					configCalendar.get(Calendar.YEAR),
					new DatePicker.OnDateChangedListener() {

						@Override
						public void onDateChanged(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							configCalendar.set(year, monthOfYear, dayOfMonth);
						}
					});
			((TimePicker) findViewById(R.id.ontime_time_picker))
					.setCurrentHour(configCalendar.get(Calendar.HOUR_OF_DAY));
			((TimePicker) findViewById(R.id.ontime_time_picker))
					.setCurrentMinute(configCalendar.get(Calendar.MINUTE));
		}

		/**
		 * Grab things that are not automatically persisted with listeners. Gets
		 * called automatically.
		 */
		public void onAccept() {
			configCalendar.set(Calendar.HOUR_OF_DAY,
					((TimePicker) findViewById(R.id.ontime_time_picker))
							.getCurrentHour());
			configCalendar.set(Calendar.MINUTE,
					((TimePicker) findViewById(R.id.ontime_time_picker))
							.getCurrentMinute());
			this.setResult(RESULT_OK, this.getIntent().putExtra("date", configCalendar));
		}
	}

	public static final String NAME = "On Time...";
	public static final int ImageResource = R.drawable.ic_launcher;
	public static final String shortDesc = "Fires at specified time";
	private Calendar dateCalendar = new GregorianCalendar();
	private static Calendar configCalendar = new GregorianCalendar();

	public OnTimeCondition() {
		super();
		if (getConfig().containsKey("date")) {
				dateCalendar = (Calendar) getConfig().get("date");
			
		}
	}

	@Override
	final public String getName() {
		return NAME;
	}

	@Override
	public int getImageResource() {
		return ImageResource;
	}

	@Override
	public void configure(EasyActivity ctx, final CallbackIF callback) {
		ctx.launchActivity(new Activity().getClass(), new CallbackIF() {

			@Override
			public void resultOK(String resultString, Bundle resultMap) {
				dateCalendar = (Calendar) resultMap.get("date");
				setConfig("date", (Calendar) dateCalendar);
				callback.resultOK(resultString, resultMap);
			}

			@Override
			public void resultCancel(String resultString, Bundle resultMap) {
				callback.resultCancel(resultString, resultMap);

			}

		});
	}

	@Override
	public String getShortDesc() {
		return shortDesc;
	}

}
