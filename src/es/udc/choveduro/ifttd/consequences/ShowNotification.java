/**
 * 
 */
package es.udc.choveduro.ifttd.consequences;

import java.util.HashMap;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import es.udc.choveduro.ifttd.conditions.OnLocationCondition.PrefsActivity;
import es.udc.choveduro.ifttd.types.CallbackIF;
import es.udc.choveduro.ifttd.types.Consequence;
import es.udc.choveduro.ifttd.EasyActivity;
import es.udc.choveduro.ifttd.R;

/**
 * @author ch01
 * 
 */
public class ShowNotification extends Consequence {
	
	/**
	 * 
	 * @see es.udc.choveduro.ifttd.types.Consequence#getName()
	 */
	@Override
	public String getName() {
		return "Notificacion";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.udc.choveduro.ifttd.types.Consequence#getImageResource()
	 */
	@Override
	public int getImageResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.udc.choveduro.ifttd.types.Consequence#getShortDesc()
	 */
	@Override
	public String getShortDesc() {
		HashMap<String, String> configuration = this.getConfig();
		return "Shows \"" + configuration.get("text") + "\".";
	}
	
	public static class PrefsActivity extends PreferenceActivity {
		static SharedPreferences sp;
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences_notification);
			setContentView(R.layout.preferences_general);
			if(sp == null){
				PrefsActivity.sp = getPreferences(MODE_WORLD_READABLE);

			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run(Context c) {
		HashMap<String, String> configuration = this.getConfig();
		int color = Color.parseColor(configuration.get("ledcolor"));
		
		Notification notification = new Notification(this.getImageResource(), configuration.get("text"), System.currentTimeMillis());

		notification.ledARGB = color;
	    notification.ledOnMS = 300;
	    notification.ledOffMS = 1000;
	    notification.flags = Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL;

	
		NotificationManager mNotificationManager =
			    (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
		
		mNotificationManager.notify(this.hashCode(), notification);
	}

	@Override
	public void configure(final EasyActivity ctx, final CallbackIF callback) {
		
		ctx.launchActivity(PrefsActivity.class, new CallbackIF(){

			@Override
			public void resultOK(String resultString, Bundle resultMap) {
				SharedPreferences prefs = ShowNotification.PrefsActivity.sp;

				HashMap<String, String> configuration = ShowNotification.this.getConfig();
				configuration.put("texto", prefs.getString("text", "Mensaje!!"));
				configuration.put("ledcolor", prefs.getString("ledcolor", "blanco"));
				callback.resultOK(resultString, resultMap);
			}

			@Override
			public void resultCancel(String resultString, Bundle resultMap) {
				callback.resultCancel(resultString, resultMap);			
			}
		});
		
	}

}
