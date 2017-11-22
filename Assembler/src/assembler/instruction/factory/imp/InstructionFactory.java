package assembler.instruction.factory.imp;

import assembler.instruction.Instruction;
import assembler.instruction.factory.IInstructionFactory;
import assembler.instruction.imp.SicInstruction;
import assembler.instruction.imp.SicXeInstruction;
import assembler.tables.ILiteralTable;
import assembler.tables.IOperationTable;
import assembler.tables.ISymbolTable;
/**
 * Factory for the instructions.
 * @author Amr
 *
 */
public class InstructionFactory implements IInstructionFactory {
	/**
	 * The mode of the factory.
	 */
	private boolean sicXe;
	/**
	 * Symbol table used in the instructions.
	 */
	private ISymbolTable sTable;
	/**
	 * The operation table used in the instruction.
	 */
	private IOperationTable oTable;
	/**
	 * Literal table.
	 */
	private ILiteralTable lTable;
	/**
	 * Constructor of the factory.
	 * @param symTable
	 * The symbol table to be used in the instructions.
	 * @param opTable
	 * The operation table to be used in the instructions.
	 * @param ltTable
	 * The literal table.
	 */
	public InstructionFactory(final ISymbolTable symTable,
			final IOperationTable opTable,
			final ILiteralTable ltTable) {
		this.sTable = symTable;
		this.oTable = opTable;
		this.lTable = ltTable;
	}
	@Override
	public final Instruction generateInstruction() {
		if (sicXe) {
			return new SicXeInstruction(sTable, oTable, lTable);
		} else {
			return new SicInstruction(sTable, oTable, lTable);
		}
	}

	@Override
	public final void setFactoryMode(final boolean sicxe) {
		sicXe = sicxe;
	}

}
