package assembler.tables;

import assembler.operation.OperationLength;
import assembler.operation.OperationOperandType;

/**
 * Interface of the operation table.
 * @author Amr
 *
 */
public interface IOperationTable {
	/**
	 * Checks if the input is a valid assembly operation.
	 * @param operation
	 * The input operation.
	 * @return
	 * True if it is a real operation.
	 */
	boolean isOperation(String operation);
	/**
	 * Checks if the input is a valid assembler directive.
	 * @param directive
	 * The input operation.
	 * @return
	 * True if it is a real directive.
	 */
	boolean isDirective(String directive);
	/**
	 * Checks if the type of operands of an operation.
	 * @param operation
	 * The input operation.
	 * @return
	 * Enum representing the operands.
	 */
	OperationOperandType operationType(String operation);
	/**
	 * Returns the size taken by the operation in bytes.
	 * @param operation
	 * The input operation.
	 * @return
	 * The number of bytes taken by it.
	 */
	OperationLength getSize(String operation);
	/**
	 * Checks if the command is SIC.
	 * @param operation
	 * The operation to check.
	 * @return
	 * True if the command is SIC.
	 */
	boolean isSIC(String operation);
	/**
	 * Gets the input operation opcode.
	 * @param operation
	 * Operation to get its opCode.
	 * @return
	 * The operation code if it is a valid operation, -1 otherwise.
	 */
	int getOpCode(String operation);
}
