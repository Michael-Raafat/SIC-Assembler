package assembler.instruction.imp;

import assembler.instruction.Instruction;
import assembler.operation.OperationOperandType;
import assembler.tables.ILiteralTable;
import assembler.tables.IOperationTable;
import assembler.tables.ISymbolTable;
/**
 * Implementation of the sic/xe instruction.
 * @author Amr
 *
 */
public class SicXeInstruction extends Instruction {
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
	 * Constructor of the sic/xe instruction.
	 * @param sTable
	 * The symbol table to use.
	 * @param oTable
	 * The operation table to use.
	 */
	public SicXeInstruction(final ISymbolTable sTable,
			final IOperationTable oTable,
			final ILiteralTable lTable) {
		super(sTable, oTable, lTable);
	}

	@Override
	public final String getOpCode() {
		StringBuilder opCode = new StringBuilder();
		String operation = this.getOperation();
		boolean form4 = false;
		if (operation.charAt(0) == '+') {
			form4 = true;
			operation = operation.substring(1);
		}
		if (operationTable.isOperation(operation)) {
			if (operationTable.operationType(this.getOperation())
					== OperationOperandType.REG_TO_REG
					&& !form4)  {
				String[] mainOperands = this.getOperands().split(",");
				if (mainOperands.length == 2
						&& symbolTable.isRegister(mainOperands[0])
						&& symbolTable.isRegister(mainOperands[1])) {
					opCode.append(Integer.toHexString(
							operationTable.getOpCode(
							this.getOperation())));
					normalizeString(opCode, 2);
					opCode.append(Integer.toHexString(
							symbolTable.getRegisterAddress(
									mainOperands[0])));
					opCode.append(Integer.toHexString(
							symbolTable.getRegisterAddress(
									mainOperands[1])));
				} else {
					this.setError(true);
					this.setErrorMessage("This operation needs"
							+ " two regiters as an operand");
					return "";
				}
			} else if (operationTable.operationType(this.getOperation())
			== OperationOperandType.REG && !form4) {
				if (symbolTable.isRegister(this.getOperands())) {
					opCode.append(Integer.toHexString(
							operationTable.getOpCode(
							this.getOperation())));
					normalizeString(opCode, 2);
					opCode.append(Integer.toHexString(
							symbolTable.getRegisterAddress(
									this.getOperands())));
					opCode.append("0");
				} else {
					this.setError(true);
					this.setErrorMessage("This operation needs"
							+ " a regiter as an operand");
					return "";
				}
			} else if (operationTable.operationType(this.getOperation())
					== OperationOperandType.NONE && !form4) {
				if (this.getOperands().trim().compareTo("") == 0) {
					opCode.append(Integer.toHexString(
							operationTable.getOpCode(
							this.getOperation())));
					normalizeString(opCode, 2);
				} else {
					this.setError(true);
					this.setErrorMessage("No operands are"
							+ " needed for this command.");
					return "";
				}
			} else if (operationTable.operationType(operation)
					== OperationOperandType.MEMORY) {
				int count = 0;
				String mainOperand = "";
				if (this.getOperands().charAt(0) == '#') {
					count = 1;
					mainOperand = this.getOperands().substring(1);
				} else if (this.getOperands().charAt(0) == '@') {
					count = 2;
					mainOperand = this.getOperands().substring(1);
				} else {
					count = 3;
					mainOperand = this.getOperands();
				}
				opCode.append(Integer.toHexString(
						operationTable.getOpCode(
								this.getOperation())
						+ count));
				String[] mainOperands = mainOperand.split(",");
				boolean indexed = false;
				if (mainOperands.length == 2
						&& mainOperands[1].trim().compareToIgnoreCase("X")
						== 0) {
					indexed = true;
				} else if (mainOperands.length > 2) {
					this.setError(true);
					this.setErrorMessage("Invalid operands.");
					return "";
				}
				if (!indexed && form4) {
					opCode.append("1");
					if (symbolTable.contains(mainOperand)) {
						StringBuilder temp = new StringBuilder();
						temp.append(Integer.toHexString(
								symbolTable.getAddress(mainOperand)));
						normalizeString(temp, 5);
						opCode.append(temp);
					} else {
						this.setError(true);
						this.setErrorMessage("Unreferenced label");
						return "";
					}
				} else if (indexed && form4 && count == 3 ) {
					opCode.append("9");
					if (symbolTable.contains(mainOperands[0])) {
						StringBuilder temp = new StringBuilder();
						temp.append(Integer.toHexString(
								symbolTable.getAddress(
									mainOperands[0])));
						normalizeString(temp, 5);
						opCode.append(temp);
					} else {
						this.setError(true);
						this.setErrorMessage("Unreferenced label");
						return "";
					}
				} else {
					int flag = 0;
					if (indexed) {
						if (count < 3) {
							this.setError(true);
							this.setErrorMessage("Not allowed to use index"
									+ " relative in immediate/indirect "
									+ "addressing.");
						}
						flag = 8;
					}
					if (mainOperands.length == 2) {
						mainOperand = mainOperands[0];
					}
					int hexAddressValue = 0;
					if (mainOperand.substring(0, 2).
							compareToIgnoreCase("0X") == 0) {
						hexAddressValue = Integer.parseInt(
								mainOperand.trim().substring(2),
								HEX_BASE);
					} else if (isNumber(mainOperand)) {
						hexAddressValue = Integer.parseInt(mainOperand);
					} else if (symbolTable.contains(mainOperand)
							&& !symbolTable.isRegister(mainOperand)) {
						hexAddressValue = symbolTable.getAddress(mainOperand);
					} else {
						this.setError(true);
						this.setErrorMessage("Not a valid operand.");
						return "";
					}
				}
			}
		} else if (operationTable.isDirective(this.getOperation())) {
			return directiveAction();
		} else {
			this.setError(true);
			this.setErrorMessage("Undefined Operation.");
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
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * The actions taken if it is a directive.
	 * @return
	 * The opCode if it exists for a directive.
	 */
	private String directiveAction() {
		StringBuilder opCode = new StringBuilder();
		if (this.getOperation().compareToIgnoreCase("BYTE") == 0) {
			String mainOperands = this.getOperands().trim();
			if (mainOperands.charAt(0) == 'c') {
				String realOperand = mainOperands.substring(2,
						mainOperands.length() - 1);
				if (realOperand.length() > MAX_CHAR_NUMBER) {
					this.setError(true);
					this.setErrorMessage("Byte operand "
							+ "is larger than allowed.");
					return "";
				}
				for (int j = 0; j < realOperand.length(); j++) {
					StringBuilder temp = new StringBuilder(String.valueOf(
							(int) realOperand.charAt(j)));
					normalizeString(temp, 2);
					opCode.append(temp);
				}
			} else if (mainOperands.charAt(0) == 'x') {
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
		return "";
	}

	@Override
	protected final boolean checkType() {
		return operationTable.isDirective(getOperation())
				|| operationTable.isOperation(getOperation());
	}
}
