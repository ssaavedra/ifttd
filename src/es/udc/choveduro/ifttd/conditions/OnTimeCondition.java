package es.udc.choveduro.ifttd.conditions;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import es.udc.choveduro.ifttd.EasyActivity;
import es.udc.choveduro.ifttd.R;
import es.udc.choveduro.ifttd.types.CallbackIF;
import es.udc.choveduro.ifttd.types.Condition;
import es.udc.choveduro.ifttd.types.ConfigurationActivity;

final public class OnTimeCondition extends Condition {

	/**
	 * Beware: Activity is not static, so it can refer to Condition.this
	 * 
	 * @author ssaavedra
	 * 
	 */
	public class Activity extends ConfigurationActivity {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.condition_on_time_config);
			
			((DatePicker) findViewById(R.id.ontime_date_picker)).init(
					dateCalendar.get(Calendar.DATE),
					dateCalendar.get(Calendar.MONTH),
					dateCalendar.get(Calendar.YEAR),
					new DatePicker.OnDateChangedListener() {

						@Override
						public void onDateChanged(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							dateCalendar.set(year, monthOfYear, dayOfMonth);
						}
					});
			((TimePicker) findViewById(R.id.ontime_time_picker))
					.setCurrentHour(dateCalendar.get(Calendar.HOUR_OF_DAY));
			((TimePicker) findViewById(R.id.ontime_time_picker))
					.setCurrentMinute(dateCalendar.get(Calendar.MINUTE));
		}

		/**
		 * Grab things that are not automatically persisted with listeners. Gets
		 * called automatically.
		 */
		public void onAccept() {
			dateCalendar.set(Calendar.HOUR_OF_DAY,
					((TimePicker) findViewById(R.id.ontime_time_picker))
							.getCurrentHour());
			dateCalendar.set(Calendar.MINUTE,
					((TimePicker) findViewById(R.id.ontime_time_picker))
							.getCurrentMinute());
		}
	}

	public static final String NAME = "On Time...";
	public static final int ImageResource = R.drawable.ic_launcher;
	public static final String shortDesc = "Fires at specified time";
	private Date fireDate;
	private Calendar dateCalendar = new GregorianCalendar();

	public OnTimeCondition() {
		if (getConfig().containsKey("date")) {
			try {
				fireDate = DateFormat.getInstance().parse(
						getConfig().get("date"));
			} catch (ParseException e) {
				// Exception parsing the date
				// TODO: Send toast?
				fireDate = null;
			}
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
		ctx.launchActivity(Activity.class, new CallbackIF() {

			@Override
			public void resultOK(String resultString, Bundle resultMap) {
				setConfig("date", (String)resultMap.get("date"));
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
