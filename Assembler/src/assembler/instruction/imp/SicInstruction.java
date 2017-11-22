package assembler.instruction.imp;

import assembler.instruction.Instruction;
import assembler.operation.OperationOperandType;
import assembler.tables.ILiteralTable;
import assembler.tables.IOperationTable;
import assembler.tables.ISymbolTable;
/**
 * Implementation of the instruction class for the sic machine.
 * @author Amr
 *
 */
public class SicInstruction extends Instruction {
	/**
	 * The hexadecimal base.
	 */
	private static final int HEX_BASE = 16,
			INDEXED_FLAG = 32768,
			ADDRESS_LENGTH = 4,
			WORD_LENGTH = 6,
			MAX_CHAR_NUMBER = 15,
			MAX_HEXA_NUMBER = 14,
			MAX_DIGIT_NUMBER = 9999;
	/**
	 * Constructor.
	 * @param sTable
	 * The symbol table.
	 * @param oTable
	 * The operation table.
	 * @param lTable
	 * The literal table.
	 */
	public SicInstruction(final ISymbolTable sTable,
			final IOperationTable oTable,
			final ILiteralTable lTable) {
		super(sTable, oTable, lTable);
	}

	@Override
	public final String getOpCode() {
		StringBuilder opCode = new StringBuilder();
		if (operationTable.isOperation(
				this.getOperation())) {
			if (operationTable.isSIC(this.getOperation())) {
				if (operationTable.operationType(this.getOperation())
						== OperationOperandType.NONE) {
					opCode.append(
							Integer.toHexString(
								operationTable.getOpCode(
									this.getOperation())));
					normalizeString(opCode, 2);
					opCode.append("0000");
					return opCode.toString();
				} else {
					opCode.append(
							Integer.toHexString(
								operationTable.getOpCode(
									this.getOperation())));
					normalizeString(opCode, 2);
					String[] operands = this.getOperands().split(",");
					String mainOperand;
					int hexAddressValue = 0;
					if (operands.length == 2
							&& operands[1].trim().toLowerCase().charAt(0)
							== 'x') {
						mainOperand = operands[0].trim();
						hexAddressValue = INDEXED_FLAG;
					} else if (operands.length >= 2) {
						this.setError(true);
						this.setErrorMessage("Invalid operands.Not a valid"
								+ " index relative address");
						return "";
					} else {
						mainOperand = this.getOperands().trim();
					}
					if (mainOperand.length() > 2
							&& mainOperand.substring(0,
									2).compareToIgnoreCase("0x") == 0) {
						try {
							hexAddressValue += Integer.parseInt(
									mainOperand.substring(2), HEX_BASE);
						} catch (Exception ei) {
							this.setError(true);
							this.setErrorMessage("Not a valid operand.");
							return "";
						}
					} else if (symbolTable.contains(mainOperand)
							&& !symbolTable.isRegister(mainOperand)) {
						hexAddressValue += symbolTable.getAddress(mainOperand);
					} else if (literalTable.contains(mainOperand)) {
						hexAddressValue += literalTable.getStartingAddress(
								mainOperand);
					} else if (mainOperand.equals("*")) {
						hexAddressValue += this.getEndLocation();
					} else {
						if (mainOperand.equals("=*")) {
							String altOperand = "=W'"
									+ Integer.toString(this.getEndLocation())
									+ "'";
							if (literalTable.contains(altOperand)) {
								hexAddressValue
								+= literalTable.getStartingAddress(
										altOperand);
							} else {
								this.setError(true);
								this.setErrorMessage("Not a valid operand.");
								return "";
							}
						} else {
							this.setError(true);
							this.setErrorMessage("Not a valid operand.");
							return "";
						}
					}
					StringBuilder address = new StringBuilder(
							Integer.toHexString(hexAddressValue));
					if (address.length() > ADDRESS_LENGTH) {
						this.setError(true);
						this.setErrorMessage("Out of memory range"
								+ " of SIC machine.");
						return "";
					}
					normalizeString(address, ADDRESS_LENGTH);
					opCode.append(address);
				}
			} else {
				this.setError(true);
				this.setErrorMessage("Not a SIC machine instruction");
				return "";
			}
		} else if (operationTable.isDirective(this.getOperation())
				&& operationTable.isSIC(this.getOperation())) {
			if (this.getOperation().compareToIgnoreCase("BYTE") == 0) {
				String mainOperands = this.getOperands().trim();
				if (mainOperands.length() < 4
						|| mainOperands.charAt(1) != '\''
						|| mainOperands.charAt(
								mainOperands.length() - 1) != '\'') {
					this.setError(true);
					this.setErrorMessage("Invalid byte operand.");
					return "";
				} else if (mainOperands.toLowerCase().charAt(0) == 'c') {
					String realOperand = mainOperands.substring(2,
							mainOperands.length() - 1);
					if (realOperand.length() > MAX_CHAR_NUMBER) {
						this.setError(true);
						this.setErrorMessage("Byte operand "
								+ "is larger than allowed.");
						return "";
					}
					for (int j = 0; j < realOperand.length(); j++) {
						StringBuilder temp = new StringBuilder(
								Integer.toHexString(
								(int) realOperand.charAt(j)));
						normalizeString(temp, 2);
						opCode.append(temp);
					}
				} else if (mainOperands.toLowerCase().charAt(0) == 'x') {
					StringBuilder realOperand
					= new StringBuilder(mainOperands.substring(2,
							mainOperands.length() - 1));
					if (realOperand.length() > MAX_HEXA_NUMBER) {
						this.setError(true);
						this.setErrorMessage("Byte operand "
								+ "is larger than allowed.");
						return "";
					}
					if (realOperand.length() % 2 != 0) {
						realOperand.insert(0, "0");
					}
					opCode.append(realOperand);
				} else {
					this.setError(true);
					this.setErrorMessage("Invalid operands"
							+ " for the byte directive.");
					return "";
				}
			} else if (this.getOperation().compareToIgnoreCase("WORD") == 0) {
				String[] mainOperands = this.getOperands().split(",");
				for (int i = 0; i < mainOperands.length; i++) {
					mainOperands[i] = mainOperands[i].trim();
					StringBuilder temp = new StringBuilder();
					if (symbolTable.contains(mainOperands[i])
							&& !symbolTable.isRegister(mainOperands[i])) {
						temp.append(Integer.toHexString(
								symbolTable.getAddress(mainOperands[i])));
						normalizeString(temp, WORD_LENGTH);
						opCode.append(temp);
					} else if (isNumber(mainOperands[i])) {
						int t = Integer.parseInt(mainOperands[i]);
						if (Math.abs(t) > MAX_DIGIT_NUMBER) {
							this.setError(true);
							this.setErrorMessage("Operand out "
									+ "of range. Maximum is 4 digits");
							return "";
						}
						temp.append(Integer.toHexString(
							t));
						normalizeString(temp, WORD_LENGTH);
						opCode.append(temp);
					} else {
						this.setError(true);
						this.setErrorMessage("Invalid operands"
								+ " for the word directive.");
						return "";
					}
				}
			}
		} else {
			this.setError(true);
			this.setErrorMessage("Undefined Operation");
			return "";
		}
		return opCode.toString();
	}
	/**
	 * Normalizes a string to a specified length.
	 * @param str
	 * The string to normalize.
	 * @param length
	 * The desired length of the string.
	 */
	private void normalizeString(final StringBuilder str,
			final int length) {
		while (str.length() < length) {
			str.insert(0, "0");
		}
	}
	/**
	 * Checks if a string is in fact a number.
	 * @param str
	 * The string to check.
	 * @return
	 * True if it is a number.
	 */
	private boolean isNumber(final String str) {
		try {
			Integer.parseInt(str, HEX_BASE);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	protected final boolean checkType() {
		return operationTable.isDirective(getOperation())
				|| operationTable.isSIC(getOperation());
	}
}
