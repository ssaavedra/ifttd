package es.udc.choveduro.ifttd.types;

import java.io.Serializable;

import es.udc.choveduro.ifttd.EasyActivity;

public interface Configurable extends Serializable {

	public void configure(EasyActivity ctx, CallbackIF callback);
	public String getName();
	public int getImageResource();

}
