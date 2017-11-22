package assembler.tables;

import java.util.List;

/**
 * Interface for the symbol table.
 * @author Amr
 *
 */
public interface ISymbolTable {
	/**
	 * Checks if this label is contained in the symbol label.
	 * @param label
	 * The label to check for.
	 * @return
	 * True if it is already added.
	 */
	boolean contains(String label);
	/**
	 * Adds the label with the value of its address to the symbol table.
	 * @param label
	 * The label to add.
	 * @param address
	 * The address of the label.
	 */
	void add(String label, int address);
	/**
	 * Returns the address of the given label.
	 * @param label
	 * The label to get its address.
	 * @return
	 * The address of the label.
	 */
	int getAddress(String label);
	/**
	 * Checks if the input is a symbol of a register.
	 * @param r
	 * The string symbol of the register.
	 * @return
	 * True if it is a register.
	 */
	boolean isRegister(String r);
	/**
	 * Gets the address of a register.
	 * @param reg
	 * The symbol of the register.
	 * @return
	 * Address of the register.
	 */
	int getRegisterAddress(String reg);
	/**
	 * Clears the symbol table.
	 */
	void clearTable();
	/**
	 * @return
	 * Return a list of strings containing representation of the symbol table.
	 */
	List<String> getTableRows();
}
