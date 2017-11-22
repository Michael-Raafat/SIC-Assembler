package assembler.literal;

import assembler.instruction.Instruction;

public interface ILiteral {
	public String getLiteral();
	
	public Integer getAddress();
	
	public void setAddress(int address);
	
	public String getOpCode();
	
	public Integer getSize();
	
	public Boolean isValid();
	
	public Instruction getInstruction();
}
