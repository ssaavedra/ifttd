package es.udc.choveduro.ifttd.types;

import es.udc.choveduro.ifttd.EasyActivity;

public abstract class CondOrCons {

	public abstract void configure(EasyActivity ctx, CallbackIF callback);
	public abstract String getName();
	public abstract int getImageResource();

}
