package es.udc.choveduro.ifttd.types;

import es.udc.choveduro.ifttd.EasyActivity;

public interface Configurable {

	public void configure(EasyActivity ctx, CallbackIF callback);
	public String getName();
	public int getImageResource();

}
