package es.udc.choveduro.ifttd.types;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;



@DatabaseTable()
public class Accion {
	public enum STATUS { ENABLED, DISABLED };

	@DatabaseField( generatedId = true )
	private int id;
	
	@DatabaseField( canBeNull = true, foreign = true )
	private Condition cond;
	
	@DatabaseField( canBeNull = true, foreign = true )
	private Consequence consec;
	
	@DatabaseField()
	private STATUS status = STATUS.ENABLED;
	
	public Accion(Condition cond, Consequence consec) {
		this.cond = cond;
		this.consec = consec;
	}

	/**
	 * @return the cond
	 */
	public Condition getCond() {
		return cond;
	}

	/**
	 * @return the consec
	 */
	public Consequence getConsec() {
		return consec;
	}

	/**
	 * @return the status
	 */
	public STATUS getStatus() {
		return status;
	}
	
	
}
