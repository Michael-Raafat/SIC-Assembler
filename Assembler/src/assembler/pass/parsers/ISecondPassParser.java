package assembler.pass.parsers;

import java.util.List;

import assembler.instruction.Instruction;

/**
 * Interface of the second pass parser.
 * @author Amr
 *
 */
public interface ISecondPassParser {
	/**
	 * Performs the second pass on the list of instructions.
	 * @param sourceProgram
	 * The list of instructions.
	 * @param outPath
	 * The output path for the listing file and object code.
	 * @return
	 * True if it contained no errors, false otherwise.
	 * @param endIndex
	 * The index of the end instruction.
	 */
	boolean secondPass(String outPath, List<Instruction> sourceProgram,
			Integer endIndex);
}
