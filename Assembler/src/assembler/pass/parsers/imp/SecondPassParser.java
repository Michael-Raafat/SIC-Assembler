package assembler.pass.parsers.imp;

import java.util.ArrayList;
import java.util.List;
import assembler.file.managment.IWriter;
import assembler.file.managment.SourceWriter;
import assembler.instruction.Instruction;
import assembler.pass.parsers.ISecondPassParser;
import assembler.record.Record;
import assembler.record.imp.EndRecord;
import assembler.record.imp.HeaderRecord;
import assembler.record.imp.TextRecord;
import assembler.tables.ILiteralTable;
import assembler.tables.ISymbolTable;
/**
 * 
 * @author Michael.
 *
 */
public class SecondPassParser implements ISecondPassParser {
	/**
	 * Writer used to write the output files.
	 */
	private IWriter writer;
	/**
	 * The list of records to write.
	 */
	private List<Record> records;
	/**
	 * The symbol table used.
	 */
	private ISymbolTable symbolTable;
	/**
	 * The literal table.
	 */
	private ILiteralTable literalTable;
	/**
	 * Constants.
	 */
	private static final int RECORD_SIZE = 60,
			BASE = 16, MAX_MEMORY_ADDRESS = 32768;
	/**
	 * Constructor of the second parser.
	 * @param sTable
	 * The symbol table used in the parsing.
	 * @param lTable
	 * The literal Table.
	 */
	public SecondPassParser(final ISymbolTable sTable,
			final ILiteralTable lTable) {
		records = new ArrayList<Record>();
		symbolTable = sTable;
		literalTable = lTable;
		writer = new SourceWriter();
	}
	@Override
	public final boolean secondPass(final String outPath,
			final List<Instruction> sourceProgram,
			final Integer endIndex) {
		records.clear();
		writer.writeInstructions(
				outPath.concat(".intermediate.txt"),
				sourceProgram, true);
		List<String> table = symbolTable.getTableRows();
		table.add("");
		table.add("");
		List<String> tempTable = literalTable.getTableRows();
		for (int i = 0; i < tempTable.size(); i++) {
			table.add(tempTable.get(i));
		}
		writer.writeTables(outPath.concat(".tables.txt"),
				table);
		int i = 0;
		Boolean flag = false;
		Instruction ins = sourceProgram.get(i);
		if (ins.getOperation().equalsIgnoreCase("START")) {
			int len = sourceProgram.get(
					sourceProgram.size() - 1).getEndLocation()
					- ins.getLocation();
			Record rec = new HeaderRecord(sourceProgram.get(i).getLocation(),
					sourceProgram.get(i).getLabel(), Integer.valueOf(len));
			records.add(rec);
			if (ins.isError()) {
				flag = true;
			}
		} else {
			 flag = true;
			 sourceProgram.get(i).setError(true);
			 sourceProgram.get(i).setErrorMessage("First line should"
			 		+ " contains start operation");
			 
		}
		i++;
		while (i < sourceProgram.size()
				&& !sourceProgram.get(i).getOperation()
				.equalsIgnoreCase("END")) {
			ins = sourceProgram.get(i);
			ins.getOpCode();
			while (i < sourceProgram.size()
					&& ins.getOperation().equalsIgnoreCase("EQU")) {
				if (ins.isError()) {
					flag = true;
				}
				i++;
				ins = sourceProgram.get(i);
				ins.getOpCode();
			}
			int j = i;
			String temp = ins.getOpCode();
			List<String> opCodes = new ArrayList<String>();
			int counter = 0;
			while (!ins.isError() && (temp.length()
					+ counter) <= RECORD_SIZE && i < sourceProgram.size()
					&& !ins.getOperation().equalsIgnoreCase("END")
					&& !ins.getOperation().equalsIgnoreCase("RESB")
					&& !ins.getOperation().equalsIgnoreCase("RESW")
					&& !ins.getOperation().equalsIgnoreCase("ORG")) {
				opCodes.add(temp);
				counter += temp.length();
				i++;
				if (i < sourceProgram.size()) {
					ins = sourceProgram.get(i);
					temp = ins.getOpCode();
					while (i < sourceProgram.size()
							&& ins.getOperation().equalsIgnoreCase("EQU")) {
						if (ins.isError()) {
							flag = true;
						}
						i++;
						ins = sourceProgram.get(i);
						temp = ins.getOpCode();
					}
				}
			}
			if (ins.isError()) {
				flag = true;
				i++;
			} else if (i >= sourceProgram.size()) {
				sourceProgram.get(i - 1).setError(true);
				sourceProgram.get(i - 1).setErrorMessage("Last line should"
				 		+ " contains End operation");
				flag = true;
			} else {
				int sPEL;
				if (sourceProgram.get(i - 1)
						.getOperation().equalsIgnoreCase("EQU")) {
					sPEL = sourceProgram.get(i).getLocation();
				} else {
					sPEL = sourceProgram.get(i - 1).getEndLocation();
				}
				int lenOp = sPEL
						- sourceProgram.get(j).getLocation();
				while (i < sourceProgram.size()
						&& (sourceProgram.get(i).getOperation().
						equalsIgnoreCase("RESB")
						|| sourceProgram.get(i).getOperation().
						equalsIgnoreCase("RESW")
						|| sourceProgram.get(i).getOperation().
						equalsIgnoreCase("ORG")
						|| sourceProgram.get(i).getOperation().
						equalsIgnoreCase("EQU"))) {
					if (sourceProgram.get(i).isError()) {
						flag = true;
					}
					i++;
				}
				Record rec = new TextRecord(opCodes,
						sourceProgram.get(j).getLocation(),
						lenOp);
				records.add(rec);
				opCodes = new ArrayList<String>();
			}
		}
		if (i < sourceProgram.size()) {
			ins = sourceProgram.get(i);
		}
		if (ins.getOperation().equalsIgnoreCase("END")) {
			ins.getOpCode();
			Record rec;
			if (symbolTable.contains(ins.getOperands())) {
				rec = new EndRecord(symbolTable.getAddress(ins.getOperands()));
				records.add(rec);
			} else if (ins.getOperands().equals("*")) {
				rec = new EndRecord(ins.getEndLocation());
				records.add(rec);
			} else {
				try {
					int add = 0;
					if (ins.getOperands().isEmpty()) {
						add = sourceProgram.get(0).getLocation();
					} else if (ins.getOperands().length() > 2
							&& ins.getOperands().substring(
							0, 2).equalsIgnoreCase("0x")) {
						add = Integer.parseInt(
								ins.getOperands().substring(2), BASE);
					} else {
						add = Integer.parseInt(ins.getOperands(), BASE);
					}
					if (!ins.getLabel().trim().equals("")) {
						flag = true;
						ins.setError(true);
						ins.setErrorMessage("End directive must "
								+ "not have a label.");
					}
					if (add >= MAX_MEMORY_ADDRESS) {
						flag = true;
						ins.setError(true);
						ins.setErrorMessage("Out of sic machine memory !");
					}
 					rec = new EndRecord(add);
					records.add(rec);
				} catch (Exception e) {
					flag = true;
					ins.setError(true);
					ins.setErrorMessage("Invalid operand for end directive");
				}
			}
		} else {
			ins.getOpCode();
			flag = true;
			ins.setError(true);
			ins.setErrorMessage("Last line should conatin END operation");
		}
		if (endIndex != null) {
			Instruction endIns = sourceProgram.remove(sourceProgram.size() - 1);
			sourceProgram.add(endIndex, endIns);
		}
		if (flag) {
			writer.writeInstructions(outPath.concat(".txt"),
					sourceProgram, false);
		} else {
			writer.writeInstructions(outPath.concat(".txt"),
					sourceProgram, false);
			writer.writeRecord(outPath.concat(".obj"), records);
		}
		return !flag;
	}

}
