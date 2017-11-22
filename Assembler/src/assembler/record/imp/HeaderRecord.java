package assembler.record.imp;

import assembler.record.Record;
/**
 * 
 * @author Michael.
 *
 */
public class HeaderRecord implements Record {
	/**
	 * program name.
	 */
	private String programName;
	/**
	 * Starting address of object program.
	 */
	private int address;
	/**
	 * Length of object program.
	 */
	private int length;
	/**
	 * Fixed string locations.
	 */
	private static final int PROGRAM_NAME_END = 7,
			STARTING_ADDRESS_END = 13,
			LENGTH_END = 19;
	public HeaderRecord(int address, String programName, int length) {
		this.address = address;
		this.length = length;
		this.programName = programName;
	}
	/**
	 * Returns string representation of the record.
	 * @return
	 * The string representation.
	 */
	public final String toString() {
		StringBuilder build = new StringBuilder("H^");
		build.append(programName);
		while (build.length() - 1 < PROGRAM_NAME_END) {
			build.append(" ");
		}
		build.append("^");
		String temp = Integer.toHexString(address); 
		while ((build.length() + temp.length() - 2) < STARTING_ADDRESS_END) {
			build.append("0");
		}
		build.append(temp + "^");
		int lenDigits = Integer.toHexString(length).length(); 
		//TODO if there is a length of object
		//program greater than 5 digits.
		while ((build.length() + lenDigits) - 3 < LENGTH_END) {
			build.append("0");
		}
		build.append(Integer.toHexString(length));
		return build.toString().toUpperCase();
	}
}
