package assembler.record.imp;

import java.util.ArrayList;
import java.util.List;

import assembler.record.Record;
/**
 * 
 * @author Michael.
 *
 */
public class TextRecord implements Record {
	/**
	 * list of opcodes of instruction
	 * that one record can hold.
	 */
	private List<String> opCodes;
	/**
	 * Starting address of object program.
	 */
	private int address;
	/**
	 * Length of object code.
	 */
	private int length;
	/**
	 * Fixed string locations.
	 */
	private static final int ADDRESS_END = 7,
			LENGTH_END = 9,
			OPCODES_END = 69;
	/**
	 * 
	 * @param opCodes
	 * @param address
	 * @param length
	 */
	public TextRecord(final List<String> opCodes, final int address,
			final int length) {
		this.opCodes = opCodes;
		this.address = address;
		this.length = length;
	}
	/**
	 * Returns string representation of the record.
	 * @return
	 * The string representation.
	 */
	public final String toString() {
		StringBuilder build = new StringBuilder("T^");
		String temp = Integer.toHexString(address); 
		while ((build.length() + temp.length() - 1) < ADDRESS_END) {
			build.append("0");
		}
		build.append(temp + "^");
		int lenDigits = Integer.toHexString(length).length();
		//TODO if there is a length of object
		//program greater than 5 digits.
		while ((build.length() + lenDigits - 2) < LENGTH_END) {
			build.append("0");
		}
		build.append(Integer.toHexString(length) + "^");
		int count = 0;
		for (int i = 0; i < opCodes.size(); i++) {
			count += opCodes.get(i).length();
			if (i == opCodes.size()-1) {
				build.append(opCodes.get(i));
			} else {
				build.append(opCodes.get(i) + "^");
			}
		}
		if (count > OPCODES_END - 9) {
			throw new RuntimeException();
		}
		return build.toString().toUpperCase();
	}
}
