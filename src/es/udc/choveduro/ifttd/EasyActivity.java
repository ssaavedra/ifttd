package es.udc.choveduro.ifttd;

import java.util.Random;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.util.SparseArray;

import com.actionbarsherlock.app.SherlockActivity;

import es.udc.choveduro.ifttd.service.OwlService;
import es.udc.choveduro.ifttd.service.OwlService.OwlBinder;
import es.udc.choveduro.ifttd.types.CallbackIF;

public class EasyActivity extends SherlockActivity {

	/** holds the map of callbacks */
	protected SparseArray<CallbackIF> _callbackMap = new SparseArray<CallbackIF>();

	OwlService mService;
	protected boolean mBound;

	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			OwlBinder binder = (OwlBinder) service;
			mService = binder.getService();
			mBound = true;
			EasyActivity.this.onServiceConnected(mService);
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = new Intent(this, OwlService.class);
		this.startService(intent);

		// Bind to service
		bindService(intent, mConnection, Context.BIND_ADJUST_WITH_ACTIVITY
				| Context.BIND_AUTO_CREATE);

	}

	/**
	 * Overloadable method to talk to server when inited connection.
	 * 
	 * @param OwlService
	 */
	protected void onServiceConnected(OwlService service) {

	}

	/**
	 * Overloadable method to apply when connection stopped.
	 */
	protected void onServiceDisconnected() {

	}

	protected void onDestroy() {
		super.onDestroy();
		if (mBound) {
			unbindService(mConnection);
		}
	}

	final public void launchActivity(
			Class<? extends Activity> subActivityClass, CallbackIF callback) {

		Intent i = new Intent(this, subActivityClass);

		Random rand = new Random();
		int correlationId = rand.nextInt();

		_callbackMap.put(correlationId, callback);

		Log.w(this.getClass().getName(),
				"Launching activity with correlationID="
						+ Integer.toString(correlationId));
		startActivityForResult(i, correlationId);

	}

	/**
	 * this is the underlying implementation of the onActivityResult method that
	 * handles auto generation of correlationIds and adding/removing callback
	 * functors to handle the result
	 */
	@Override
	final protected void onActivityResult(int correlationId, int resultCode,
			Intent i) {

		Log.w(this.getClass().getName(), "Getting activity result");
		try {
			CallbackIF callback = _callbackMap.get(correlationId);

			switch (resultCode) {
			case Activity.RESULT_CANCELED:
				if (i != null)
					callback.resultCancel(i.getBundleExtra("result"));
				else
					callback.resultCancel(null);
				_callbackMap.remove(correlationId);
				break;
			case Activity.RESULT_OK:
				callback.resultOK(i.getBundleExtra("result"));
				_callbackMap.remove(correlationId);
				break;
			default:
				Log.e("OOPS",
						"Couldn't find callback handler for correlationId");
			}
		} catch (Exception e) {
			Log.e("OOPS", "Problem processing result from sub-activity", e);
		}
	}

}