package assembler.pass.parsers;

import java.util.List;

import assembler.instruction.Instruction;

/**
 * Class responsible of the first pass.
 * @author Amr
 *
 */
public interface IFirstPassParser {
	/**
	 * Runs the first pass on the source code.
	 * Generates the suitable symbol table and the list of
	 * instructions for the second pass.
	 * @param sourceCode
	 * The list of string of the source program.
	 * @return
	 * A list of instruction of the source program.
	 */
	List<Instruction> firstPass(List<String> sourceCode);
	/**
	 * @return
	 * Return the index of the end instruction in the source program read.
	 */
	Integer getEndIndex();
}
