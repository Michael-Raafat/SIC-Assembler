package assembler.tables;

import java.util.List;

import assembler.instruction.factory.IInstructionFactory;
import assembler.literal.ILiteral;

/**
 * Literal table interface.
 * @author Amr
 *
 */
public interface ILiteralTable {
	/**
	 * Adds a new literal to the table and calculates its value and size.
	 * @param literal
	 * The string of the literal to add.
	 * @return true if literal is added successfully, false otherwise.
	 */
	Boolean addLiteral(String literal);
	/**
	 * Checks if the literal is already contained in the literal table
	 * (By value not string comparison).
	 * @param literal
	 * The literal to check its existence.
	 * @return
	 * True if the literal exists already in table.
	 */
	boolean contains(String literal);
	/**
	 * Returns the address of a literal.
	 * @param literal
	 * The literal to return its address.
	 * @return
	 * Integer of the address of the literal in the memory.
	 */
	int getStartingAddress(String literal);
	/**
	 * Returns the size of the literal in memory.
	 * @param literal
	 * The literal to get its size.
	 * @return
	 * its size in memory.
	 */
	int getSize(String literal);
	/**
	 * Takes a literal and return its op code representation.
	 * @param literal
	 * The literal to transform to op code.
	 * @return
	 * null if it is isn't contained in the literal table,
	 * otherwise converts it to the op code and returns a string of the op code
	 */
	String getOpCode(String literal);
	/**
	 * @return
	 * A list of all literals contained in the table.
	 */
	List<ILiteral> getAllLiterals();
	/**
	 * @return
	 * A list of all literals contained in the table that aren't assigned.
	 */
	List<ILiteral> getAllUnsignedLiterals();
	/**
	 * Assigns an address to a literal.
	 * @param literal
	 * The literal to assign its address to.
	 * @param address
	 * The address of the literal.
	 */
	void assignAddress(String literal, int address);
	/**
	 * Clears the literal table.
	 */
	void clear();
	/**
	 * Sets the instruction factory used in the literal table.
	 * @param factory
	 * The factory to use.
	 */
	void setInstructionFactory(IInstructionFactory factory);
	/**
	 * Return string representation of the table.
	 * @return
	 * List of strings.
	 */
	List<String> getTableRows();
}
