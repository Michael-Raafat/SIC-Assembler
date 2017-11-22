package assembler.register;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class RegisterMain {

	public static void main(String[] args) throws FileNotFoundException {
		List <Register> registers = new ArrayList<Register>();
		registers.add(new Register("A",0));
		registers.add(new Register("X",1));
		registers.add(new Register("L",2));
		registers.add(new Register("B",3));
		registers.add(new Register("S",4));
		registers.add(new Register("T",5));
		registers.add(new Register("F",6));
		registers.add(new Register("PC",8));
		registers.add(new Register("SW",9));
		writeTable(registers);
	}
	
	public static void writeTable(List<Register> list) throws FileNotFoundException {
        	XMLEncoder encoder =
        	        	new XMLEncoder(
        	              new BufferedOutputStream(
        	                new FileOutputStream("registers.xml")));
        	encoder.writeObject(list);
        	encoder.close();
	}
}
