package assembler.file.managment;

import java.util.List;

import assembler.instruction.Instruction;
import assembler.record.Record;

/**
 * Interface of the writer.
 * @author Amr
 *
 */
public interface IWriter {
	/**
	 * Writes the object code.
	 * @param outFile
	 * The output file to write the object code in.
	 * @param records
	 * The records to write the object code from.
	 */
	void writeRecord(String outFile, List<Record> records);
	/**
	 * Writes the listing file.
	 * @param outFile
	 * The output file to write the listing file.
	 * @param instructions
	 * The list of instructions to print the listing file from.
	 * @param intermediate
	 * Flag to show whether to print intermediate file or full listing
	 * file.
	 */
	void writeInstructions(String outFile, List<Instruction> instructions,
			boolean intermediate);
	/**
	 * Writes the symbol table.
	 * @param outFile
	 * The output file.
	 * @param tableRows
	 * The table rows to print.
	 */
	void writeTables(String outFile, List<String> tableRows);
}
