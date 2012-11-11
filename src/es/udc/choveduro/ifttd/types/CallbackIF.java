package es.udc.choveduro.ifttd.types;

import android.os.Bundle;

public interface CallbackIF {
	public void resultOK(Bundle resultMap);
	public void resultCancel(Bundle resultMap);
}
