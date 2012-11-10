/**
 * 
 */
package es.udc.choveduro.ifttd.consequences;

import java.util.HashMap;

import android.app.Activity;
import android.app.ListActivity;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import es.udc.choveduro.ifttd.types.Consequence;
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
		return "Se muestra " + "\"" + configuration.get("text") + "\".";
	}

	public void saveConfig(){
		 HashMap<String, String> configuration = this.getConfig();
		 configuration.put("texto", "valor");
		 configuration.put("ledcolor", "valor");
	}
	
	public class PrefsActivity extends PreferenceActivity {

		@SuppressWarnings("deprecation")
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences_notification);
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
		
		mNotificationManager.notify(2, notification);
	
	}

}
