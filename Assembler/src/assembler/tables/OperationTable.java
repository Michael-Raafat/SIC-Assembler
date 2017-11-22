package assembler.tables;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import assembler.operation.IOperation;
import assembler.operation.Operation;
import assembler.operation.OperationLength;
import assembler.operation.OperationOperandType;
/**
 * Implementation of the Operation table.
 * @author Amr
 *
 */
public class OperationTable implements IOperationTable {
	/**
	 * Hash map for all the operations.
	 */
	private HashMap<String, IOperation> operationTable;
	/**
	 * Constructor.
	 */
	public OperationTable() {
		operationTable = new HashMap<String, IOperation>();
		loadOperations();
	}
	
	@Override
	public final boolean isOperation(final String operation) {
		return operationTable.containsKey(operation.toLowerCase());
	}
	
	@Override
	public final boolean isDirective(final String directive) {
		if (directive.equalsIgnoreCase("START")
				|| directive.equalsIgnoreCase("END") 
				|| directive.equalsIgnoreCase("BYTE")
				|| directive.equalsIgnoreCase("WORD")
				|| directive.equalsIgnoreCase("RESW")
				|| directive.equalsIgnoreCase("RESB")
				|| directive.equalsIgnoreCase("EQU")
				|| directive.equalsIgnoreCase("ORG")
				|| directive.equalsIgnoreCase("LTORG")) {
			return true;
		}
		return false;
	}

	@Override
	public final OperationOperandType operationType(final String operation) {
		return operationTable.get(operation.toLowerCase()).getOperandType();
	}

	@Override
	public final OperationLength getSize(final String operation) {
		return operationTable.get(operation.toLowerCase()).getOperationLength();
	}

	@Override
	public final boolean isSIC(final String operation) {
		if (isDirective(operation)) {
			return true;
		} else if (operationTable.containsKey(operation.toLowerCase())) {
			return operationTable.get(operation.toLowerCase()).sic();
		} else {
			return false;
		}
	}

	@Override
	public final int getOpCode(final String operation) {
		return operationTable.get(operation.toLowerCase()).getOpCode();
	}
	/**
	 * Loads the operations into the operation table.
	 */
	@SuppressWarnings("unchecked")
	private void loadOperations() {
		try {
			XMLDecoder decoder =
		            new XMLDecoder(new BufferedInputStream(
		                new FileInputStream("operations.xml")));
			List<Operation> list = (List<Operation>) decoder.readObject();
			decoder.close();
			for (Operation op : list) {
				operationTable.put(op.getName().toLowerCase(), op);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
