package assembler.register;

public class Register implements IRegister{
	private String name;
	private Integer address;
	

	public Register() {
		name = null;
		address = null;
	}
	
	public Register(String name,Integer address) {
		this.name = name;
		this.address = address;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getName() {
		return name;
	}
	
	public void setAddress(Integer address) {
		this.address = address;
	}

	public Integer getAddress() {
		return address;
	}
	
}
