package assembler.imp;

import java.util.List;

import assembler.IAssembler;
import assembler.file.managment.IReader;
import assembler.file.managment.SourceReader;
import assembler.instruction.Instruction;
import assembler.instruction.factory.IInstructionFactory;
import assembler.instruction.factory.imp.InstructionFactory;
import assembler.pass.parsers.IFirstPassParser;
import assembler.pass.parsers.ISecondPassParser;
import assembler.pass.parsers.imp.FirstPassParser;
import assembler.pass.parsers.imp.SecondPassParser;
import assembler.tables.ILiteralTable;
import assembler.tables.IOperationTable;
import assembler.tables.ISymbolTable;
import assembler.tables.LiteralTable;
import assembler.tables.OperationTable;
import assembler.tables.SymbolTable;
/**
 * Assembler implementation.
 * @author Amr
 *
 */
public class Assembler implements IAssembler {
	/**
	 * First pass manager.
	 */
	private IFirstPassParser firstPass;
	/**
	 * Second pass manager.
	 */
	private ISecondPassParser secondPass;
	/**
	 * operation table.
	 */
	private IOperationTable operationTable;
	/**
	 * Instruction factory.
	 */
	private IInstructionFactory instructionFactory;
	/**
	 * Literal table.
	 */
	private ILiteralTable literalTable;
	/**
	 * The symbol table.
	 */
	private ISymbolTable symboleTable;
	/**
	 * Reader for the source code.
	 */
	private IReader sourceReader;
	/**
	 * Constructor.
	 */
	public Assembler() {
		operationTable = new OperationTable();
		symboleTable = new SymbolTable();
		literalTable = new LiteralTable();
		instructionFactory = new InstructionFactory(symboleTable,
				operationTable, literalTable);
		literalTable.setInstructionFactory(instructionFactory);
		firstPass = new FirstPassParser(
				symboleTable, operationTable,
				instructionFactory, literalTable);
		secondPass = new SecondPassParser(symboleTable, literalTable);
		sourceReader = new SourceReader();
	}
	@Override
	public final boolean assemble(
			final String inFile, final String outFileLocation,
			final boolean sicXE) {
		try {
			List<String> codeStrings = sourceReader.readFile(inFile);
			instructionFactory.setFactoryMode(sicXE);
			symboleTable.clearTable();
			literalTable.clear();
			List<Instruction> codeInstructions
				= firstPass.firstPass(codeStrings);
			return secondPass.secondPass(outFileLocation, codeInstructions,
					firstPass.getEndIndex());
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}
