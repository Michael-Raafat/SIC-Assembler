package assembler.instruction.factory;

import assembler.instruction.Instruction;

/**
 * Interface of the instruction.
 * @author Amr
 *
 */
public interface IInstructionFactory {
	/**
	 * Creates an instruction.
	 * @return
	 * Returns an instruction from a suitable type.
	 */
	Instruction generateInstruction();
	/**
	 * Sets the factory mode.
	 * @param sicxe
	 * If true, factory generates SIC/XE instructions.
	 * Else factory generates SIC instructions.
	 */
	void setFactoryMode(boolean sicxe);
}
