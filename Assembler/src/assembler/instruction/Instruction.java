package assembler.instruction;

import assembler.operation.OperationLength;
import assembler.tables.ILiteralTable;
import assembler.tables.IOperationTable;
import assembler.tables.ISymbolTable;

/**
 * Abstract class of the assembly instructions.
 * @author Amr
 *
 */
public abstract class Instruction {
	/**
	 * Enables the relative base addressing.
	 */
	protected static boolean baseEnable;
	/**
	 * The base address.
	 */
	private static int baseAddress;
	/**
	 * Sets the base relative addressing to either true or false.
	 * @param value
	 * The value to assign to base enable.
	 */
	public static void enableBase(final boolean value) {
		Instruction.baseEnable = value;
	}
	/**
	 * String containing the operation.
	 */
	private String operation;
	/**
	 * String containing the operands.
	 */
	private String operands;
	/**
	 * String containing the label.
	 */
	private String label;
	/**
	 * The starting location of the instruction.
	 */
	private int location;
	/**
	 * The error in an instruction.
	 */
	private boolean error;
	/**
	 * The error message in the instruction.
	 */
	private String errorMessage;
	/**
	 * The symbol table to use when generating opCode.
	 */
	protected ISymbolTable symbolTable;
	/**
	 * The operation table used.
	 */
	protected IOperationTable operationTable;
	/**
	 * The literal table used.
	 */
	protected ILiteralTable literalTable;
	/**
	 * Fixed string locations.
	 */
	private static final int LOCATION_END = 5,
			LABEL_END = 14,
			OPERATION_END = 21,
			OPERANDS_END = 41,
			OPCODE_END = 48;
	/**
	 * Constructor of the instruction.
	 * @param sTable
	 * The symbol table to use.
	 * @param oTable
	 * The operation table to use.
	 * @param lTable
	 * The literal table to use.
	 */
	public Instruction(final ISymbolTable sTable,
			final IOperationTable oTable,
			final ILiteralTable lTable) {
		symbolTable = sTable;
		operationTable = oTable;
		literalTable = lTable;
	}
	/**
	 * The operation code for the instruction.
	 * @return
	 * String of the opCode.
	 */
	public abstract String getOpCode();
	/**
	 * Returns string representation of the instruction.
	 * @return
	 * The string representation.
	 */
	public final String toString() {
		StringBuilder build = new StringBuilder(Integer.toHexString(location));
		while (build.length() < LOCATION_END) {
			build.append(" ");
		}
		build.append(" " + label);
		while (build.length() < LABEL_END) {
			build.append(" ");
		}
		build.append(" " + operation);
		while (build.length() < OPERATION_END) {
			build.append(" ");
		}
		build.append("  " + operands);
		while (build.length() < OPERANDS_END) {
			build.append(" ");
		}
		build.append(" " + this.getOpCode());
		while (build.length() < OPCODE_END) {
			build.append(" ");
		}
		if (error) {
			build.append(" " + errorMessage);
		}
		return build.toString();
	}
	/**
	 * Returns string representation of the instruction.
	 * @return
	 * The string representation.
	 */
	public final String toIntermediateString() {
		StringBuilder build = new StringBuilder(Integer.toHexString(location));
		while (build.length() < LOCATION_END) {
			build.append(" ");
		}
		build.append(" " + label);
		while (build.length() < LABEL_END) {
			build.append(" ");
		}
		build.append(" " + operation);
		while (build.length() < OPERATION_END) {
			build.append(" ");
		}
		build.append("  " + operands);
		while (build.length() < OPERANDS_END) {
			build.append(" ");
		}
		if (error) {
			build.append(" " + errorMessage);
		}
		return build.toString();
	}
	/**
	 * @return the operation
	 */
	public final String getOperation() {
		return operation;
	}
	/**
	 * @param newOperation the operation to set
	 */
	public final void setOperation(final String newOperation) {
		this.operation = newOperation;
	}
	/**
	 * @return the operands
	 */
	public final String getOperands() {
		return operands;
	}
	/**
	 * @param newOperands the operands to set
	 */
	public final void setOperands(final String newOperands) {
		this.operands = newOperands;
	}
	/**
	 * @return the label
	 */
	public final String getLabel() {
		return label;
	}
	/**
	 * @param newLabel the label to set
	 */
	public final void setLabel(final String newLabel) {
		this.label = newLabel;
	}
	/**
	 * @return the location
	 */
	public final int getLocation() {
		return location;
	}
	/**
	 * @param newLocation the location to set
	 */
	public final void setLocation(final int newLocation) {
		this.location = newLocation;
	}
	/**
	 * @return the error
	 */
	public final boolean isError() {
		return error;
	}
	/**
	 * @param newError the error to set
	 */
	public final void setError(final boolean newError) {
		this.error = newError;
	}
	/**
	 * @return the errorMessage
	 */
	public final String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param newErrorMessage the errorMessage to set
	 */
	public final void setErrorMessage(
			final String newErrorMessage) {
		this.errorMessage = newErrorMessage;
	}
	/**
	 * @return the baseAddress
	 */
	public static int getBaseAddress() {
		return baseAddress;
	}
	/**
	 * @param newBaseAddress the baseAddress to set
	 */
	public static void setBaseAddress(final int newBaseAddress) {
		Instruction.baseAddress = newBaseAddress;
	}
	/**
	 * Checks for errors while calculating the end location.
	 * @return
	 * True if it is suitable to the machine type.
	 */
	protected abstract boolean checkType();
	/**
	 * Returns the ending location of the instruction.
	 * @return
	 * Int of the location counter value after saving the instruction.
	 */
	public final int getEndLocation() {
		if (error || !checkType()) {
			return location;
		}
		boolean form4 = false;
		String modOp = this.getOperation();
		if (modOp.length() > 0 && modOp.charAt(0) == '+') {
			modOp = modOp.substring(1);
			form4 = true;
		}
		if (operationTable.isOperation(modOp)) {
			OperationLength length = operationTable.getSize(
					modOp);
			if (length == OperationLength.ONE_BYTE && !form4) {
				return location + 1;
			} else if (length == OperationLength.TWO_BYTE && !form4) {
				return location + 2;
			} else if (length == OperationLength.THREE_OR_FOUR_BYTE) {
				if (form4) {
					return location + 4;
				} else {
					return location + 3;
				}
			} else {
				return location;
			}
		} else if (operationTable.isDirective(this.getOperation())) {
			if (operation.compareToIgnoreCase("BYTE") == 0
					|| operation.compareToIgnoreCase("WORD") == 0) {
				return location + this.getOpCode().length() / 2;
			} else if (operation.compareToIgnoreCase("RESW") == 0) {
				try {
					int i = Integer.parseInt(this.getOperands());
					if (i >= 0) {
						return location + i * 3;
					} else {
						this.setError(true);
						this.setErrorMessage("Invalid operand for RESW");
						return location;
					}
				} catch (Exception e) {
					return location;
				}
			} else if (operation.compareToIgnoreCase("RESB") == 0) {
				try {
					int i = Integer.parseInt(this.getOperands());
					if (i >= 0) {
						return location + i;
					} else {
						this.setError(true);
						this.setErrorMessage("Invalid operand for RESW");
						return location;
					}
				} catch (Exception e) {
					return location;
				}
			} else {
				return location;
			}
		} else {
			return location;
		}
	}
}
