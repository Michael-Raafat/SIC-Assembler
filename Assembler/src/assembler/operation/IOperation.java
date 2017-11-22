package assembler.operation;

public interface IOperation {
	public String getName();
	
	public Integer getOpCode();
	
	public OperationLength getOperationLength();
	
	public OperationOperandType getOperandType();
	
	public boolean sic();
	
}
