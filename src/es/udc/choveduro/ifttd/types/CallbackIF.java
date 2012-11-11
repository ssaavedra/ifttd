package es.udc.choveduro.ifttd.types;

import android.os.Bundle;

public interface CallbackIF {
	public void resultOK(String resultString, Bundle resultMap);
	public void resultCancel(String resultString, Bundle resultMap);
}
