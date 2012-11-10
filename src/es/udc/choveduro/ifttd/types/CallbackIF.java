package es.udc.choveduro.ifttd.types;

import android.os.Bundle;

abstract public class CallbackIF {
	public abstract void resultOK(String resultString, Bundle resultMap);
	public abstract void resultCancel(String resultString, Bundle resultMap);
}
