package es.udc.choveduro.ifttd;

import java.util.Random;

import es.udc.choveduro.ifttd.types.CallbackIF;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

public class EasyActivity extends Activity {

	/** holds the map of callbacks */
	protected SparseArray<CallbackIF> _callbackMap = new SparseArray<CallbackIF>();

	public EasyActivity() {
		super();
	}

	public void launchActivity(Class subActivityClass, CallbackIF callback) {
	
		Intent i = new Intent(this, subActivityClass);
	
		Random rand = new Random();
		int correlationId = rand.nextInt();
	
		_callbackMap.put(correlationId, callback);
	
		startActivityForResult(i, correlationId);
	
	}

	/**
	 * this is the underlying implementation of the onActivityResult method that
	 * handles auto generation of correlationIds and adding/removing callback
	 * functors to handle the result
	 */
	protected void onActivityResult(int correlationId, int resultCode, String paramStr,
			Bundle paramMap) {
			
				try {
					CallbackIF callback = _callbackMap.get(correlationId);
			
					switch (resultCode) {
					case Activity.RESULT_CANCELED:
						callback.resultCancel(paramStr, paramMap);
						_callbackMap.remove(correlationId);
						break;
					case Activity.RESULT_OK:
						callback.resultOK(paramStr, paramMap);
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