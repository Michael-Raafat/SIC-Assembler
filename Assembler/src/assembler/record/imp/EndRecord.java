package assembler.record.imp;

import assembler.record.Record;
/**
 * 
 * @author Michael.
 *
 */
public class EndRecord implements Record {
	/**
	 * Starting address of object program.
	 */
	private int address;
	/**
	 * Fixed string locations.
	 */
	private static final int STARTING_ADDRESS_END = 7;
	public EndRecord(int address) {
		this.address = address;
	}
	/**
	 * Returns string representation of the record.
	 * @return
	 * The string representation.
	 */
	public final String toString() {
		StringBuilder build = new StringBuilder("E^");
		String temp = Integer.toHexString(address); 
		while ((build.length() + temp.length() - 1) < STARTING_ADDRESS_END) {
			build.append("0");
		}
		build.append(temp);
		return build.toString().toUpperCase();
	}
}
