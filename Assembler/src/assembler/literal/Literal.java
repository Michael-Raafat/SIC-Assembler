package assembler.literal;

import assembler.instruction.Instruction;
import assembler.instruction.factory.IInstructionFactory;

public class Literal implements ILiteral {
	private String literal;
	private Integer address;
	private Integer size;
	private String opCode;
	private Boolean valid;
	private Instruction instruction;

	public Literal(String literal, IInstructionFactory instructionFactory) {
		this.literal = literal;
		this.address = null;
		instruction = instructionFactory.generateInstruction();
		instruction.setLabel("");
		valid = false;
		validateLiteral();
	}

	public String getLiteral() {
		return literal;
	}

	public void setLiteral(String literal) {
		this.literal = literal;
	}

	public Integer getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
		instruction.setLocation(address);
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getOpCode() {
		return opCode;
	}

	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public Boolean isValid() {
		return valid;
	}

	private void setOpCode() {
		if (literal.startsWith("=C'") || literal.startsWith("=c'")) {
			StringBuilder oc = new StringBuilder();
			for (int i = 3; i < literal.length() - 1; i++) {
				int c = literal.charAt(i);
				String hex = Integer.toHexString(c);
				if (hex.length() == 1)
					oc.append('0');
				oc.append(hex);
			}
			opCode = oc.toString();
		} else if (literal.startsWith("=X'") || literal.startsWith("=x'")) {
			opCode = literal.substring(3, literal.length() - 1);
		} else if (literal.startsWith("=W'") || literal.startsWith("=w'")) {
			try {
			opCode = Integer.toHexString(
					Integer.parseInt(
							literal.substring(3, literal.length() - 1)));
			} catch (Exception e) {
				instruction.setError(true);
				instruction.setErrorMessage("Invalid operand");
			}
			if (opCode.length() > 6)
				instruction.setError(true);
				instruction.setErrorMessage("Invalid operand");
		} else if (literal.startsWith("='")) {
			try {
			opCode = Integer.toHexString(Integer.parseInt(literal.substring(2, literal.length() - 1)));
			} catch (Exception e) {
				instruction.setError(true);
				instruction.setErrorMessage("Invalid operand");
			}
			if (opCode.length() > 6)
				instruction.setError(true);
				instruction.setErrorMessage("Invalid operand");
		}
	}

	private void setSize() {
		if (literal.startsWith("=C'") || literal.startsWith("=c'")) {
			size = opCode.length() / 2;
		} else if (literal.startsWith("=X'") || literal.startsWith("=x'")) {
			size = opCode.length();
		} else if (literal.startsWith("=W'") || literal.startsWith("=w'")) {
			size = 3;
		} else if (literal.startsWith("='")) {
			size = 3;
		}
	}

	private Boolean validateLiteral() {
		if (literal.startsWith("=C'") || literal.startsWith("=c'")) {
			instruction.setOperands(literal.substring(1));
			instruction.setOperation("BYTE");
			if (literal.length() > 19) {
				instruction.setError(true);
				instruction.setErrorMessage("Invalid operand");
				return false;
			}
		} else if (literal.startsWith("=X'") || literal.startsWith("=x'")) {
			instruction.setOperands(literal.substring(1));
			instruction.setOperation("BYTE");
			if (literal.length() > 18) {
				instruction.setError(true);
				instruction.setErrorMessage("Invalid operand");
				return false;
			}
		} else if (literal.startsWith("=W'") || literal.startsWith("=w'")) {
			instruction.setOperation("WORD");
			instruction.setOperands(literal.substring(3, literal.length() - 1));
			// maximum possible int?
		} else if (literal.startsWith("='")) {
			instruction.setOperation("WORD");
			instruction.setOperands(literal.substring(2, literal.length() - 1));
		} else {
			return false;
		}
		if (literal.charAt(literal.length() - 1) != '\'') {
			instruction.setError(true);
			instruction.setErrorMessage("Invalid operand");
			return false;
		}
		valid = true;
		setOpCode();
		setSize();
		return true;
	}

	@Override
	public Instruction getInstruction() {
		return instruction;
	}

}
