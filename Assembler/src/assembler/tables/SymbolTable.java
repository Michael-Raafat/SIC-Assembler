package assembler.tables;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import assembler.register.IRegister;
import assembler.register.Register;

public class SymbolTable implements ISymbolTable {
	HashMap<String, Integer> symbolTable;
	HashMap<String, IRegister> registerTable;
	
	
	public SymbolTable() {
		symbolTable = new HashMap<String, Integer>();
		registerTable = new HashMap<String,IRegister>();
		loadRegisters();
	}
	
	@Override
	public boolean contains(String label) {
		return symbolTable.containsKey(label);
	}

	@Override
	public void add(String label, int address) {
		if(!contains(label))
			symbolTable.put(label, address);
	}

	@Override
	public int getAddress(String label) {
		return symbolTable.get(label);
	}

	@Override
	public boolean isRegister(String r) {
		return registerTable.containsKey(r.toLowerCase());
	}

	@Override
	public int getRegisterAddress(String reg) {
		return registerTable.get(reg).getAddress();
	}

	@Override
	public void clearTable() {
		symbolTable.clear();
	}
	
	@SuppressWarnings("unchecked")
	private void loadRegisters() {
		try {
			XMLDecoder decoder =
		            new XMLDecoder(new BufferedInputStream(
		                new FileInputStream("registers.xml")));
			List <Register> list = (List<Register>)decoder.readObject();
			decoder.close();
			for(Register reg : list)
				registerTable.put(reg.getName().toLowerCase(), reg);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public final List<String> getTableRows() {
		List<String> list = new ArrayList<String>();
		StringBuilder temp = new StringBuilder();
		temp.append("Symbol");
		while (temp.length() < 15) {
			temp.append(" ");
		}
		temp.append("address");
		list.add(temp.toString());
		for (String value : symbolTable.keySet()) {
			temp = new StringBuilder();
			temp.append(value);
			while (temp.length() < 15) {
				temp.append(" ");
			}
			temp.append(this.getAddress(value));
			list.add(temp.toString());
		}
		return list;
	}

}
