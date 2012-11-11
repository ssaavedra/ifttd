package es.udc.choveduro.ifttd.types;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import es.udc.choveduro.ifttd.R;

public class ConfigurationActivity extends Activity {
	public void onCreate(Bundle savedInstanceState, int layout) {
		super.onCreate(savedInstanceState);
		setContentView(layout);
		setResult(RESULT_CANCELED);
		findViewById(R.id.configure_accept_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						onAcceptBtnClicked();
					}
				});
		findViewById(R.id.configure_cancel_button).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						setResult(RESULT_CANCELED);
						ConfigurationActivity.this.finish();
					}

				});
	}

	public void onAcceptBtnClicked() {
		setResult(RESULT_OK);
		onAccept();
		finish();
	}

	/**
	 * Stub class to persist objects not automatically persisted with listeners.
	 * Called automatically when AcceptBtnClicked.
	 */
	public void onAccept() {

	}

}
