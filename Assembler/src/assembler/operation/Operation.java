package assembler.operation;

public class Operation implements IOperation {
	private String name;
	private Integer opCode;
	private OperationLength opLength;
	private OperationOperandType operandType;
	/**
	 * @return the sic
	 */
	public boolean isSic() {
		return sic;
	}

	/**
	 * @param sic the sic to set
	 */
	public void setSic(boolean sic) {
		this.sic = sic;
	}

	/**
	 * @return the opLength
	 */
	public OperationLength getOpLength() {
		return opLength;
	}

	/**
	 * @param operandType the operandType to set
	 */
	public void setOperandType(OperationOperandType operandType) {
		this.operandType = operandType;
	}

	private boolean sic;
	
	public Operation() {
		name = null;
		opCode = null;
		opLength = null;
		operandType = null;
		sic = false;
	}
	
	public Operation(String name,Integer opCode,OperationLength opLength, 
			OperationOperandType operandType,boolean sic) {
		this.name = name;
		this.opCode = opCode;
		this.opLength = opLength;
		this.operandType = operandType;
		this.sic = sic;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setOpCode(Integer opCode) {
		this.opCode = opCode;
	}
	
	public Integer getOpCode() {
		return opCode;
	}
	
	public OperationLength getOperationLength() {
		return opLength;
	}
	
	public void setOpLength(OperationLength opLength) {
		this.opLength = opLength;
	}
	
	public OperationOperandType getOperandType() {
		return operandType;
	}
	
	public boolean sic() {
		return sic;
	}


}
