package assembler.operation;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.XMLReader;

import assembler.register.Register;

public class OperationMain {

	public static void main(String[] args) throws FileNotFoundException {
		List <Operation> operations = new ArrayList<Operation>();
		operations.add(new Operation("ADD",24,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("ADDF",88,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,false));
		operations.add(new Operation("ADDR",144,OperationLength.TWO_BYTE,
				OperationOperandType.REG_TO_REG,false));
		operations.add(new Operation("AND",64,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("CLEAR",180,OperationLength.TWO_BYTE,
				OperationOperandType.REG,false));
		operations.add(new Operation("COMP",40,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("COMPF",136,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,false));
		operations.add(new Operation("COMPR",160,OperationLength.TWO_BYTE,
				OperationOperandType.REG_TO_REG,false));
		operations.add(new Operation("DIV",36,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("DIVF",100,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,false));
		operations.add(new Operation("DIVR",156,OperationLength.TWO_BYTE,
				OperationOperandType.REG_TO_REG,false));
		operations.add(new Operation("FIX",196,OperationLength.ONE_BYTE,
				OperationOperandType.NONE,false));
		operations.add(new Operation("FLOAT",192,OperationLength.ONE_BYTE,
				OperationOperandType.NONE,false));
		operations.add(new Operation("HIO",244,OperationLength.ONE_BYTE,
				OperationOperandType.NONE,false));
		operations.add(new Operation("J",60,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("JEQ",48,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("JGT",52,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("JLT",56,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("JSUB",72,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("LDA",0,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("LDB",104,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,false));
		operations.add(new Operation("LDCH",80,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("LDF",112,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,false));
		operations.add(new Operation("LDL",8,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("LDS",108,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,false));
		operations.add(new Operation("LDT",116,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,false));
		operations.add(new Operation("LDX",4,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("LPS",208,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,false));
		operations.add(new Operation("MUL",32,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		
		operations.add(new Operation("MULF",96,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,false));
		operations.add(new Operation("MULR",152,OperationLength.TWO_BYTE,
				OperationOperandType.REG_TO_REG,false));
		operations.add(new Operation("NORM",200,OperationLength.ONE_BYTE,
				OperationOperandType.NONE,false));
		operations.add(new Operation("OR",68,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("RD",216,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("RMO",172,OperationLength.TWO_BYTE,
				OperationOperandType.REG_TO_REG,false));
		operations.add(new Operation("RSUB",76,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.NONE,true));
		operations.add(new Operation("SHIFTL",164,OperationLength.TWO_BYTE,
				OperationOperandType.REG_NBITS,false));
		operations.add(new Operation("SHIFTR",168,OperationLength.TWO_BYTE,
				OperationOperandType.REG_NBITS,false));
		operations.add(new Operation("SIO",240,OperationLength.ONE_BYTE,
				OperationOperandType.NONE,true));
		operations.add(new Operation("SSK",236,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("STA",12,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("STB",120,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,false));
		operations.add(new Operation("STCH",84,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("STF",128,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,false));
		operations.add(new Operation("STI",212,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,false));
		operations.add(new Operation("STL",20,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("STS",124,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,false));
		operations.add(new Operation("STSW",232,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("STT",132,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,false));
		operations.add(new Operation("STX",16,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("SUB",28,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("SUBF",92,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,false));
		operations.add(new Operation("SUBR",148,OperationLength.TWO_BYTE,
				OperationOperandType.REG_TO_REG,false));
		operations.add(new Operation("SVC",176,OperationLength.TWO_BYTE,
				OperationOperandType.N,false));
		operations.add(new Operation("TD",224,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("TIO",248,OperationLength.ONE_BYTE,
				OperationOperandType.NONE,false));
		operations.add(new Operation("TIX",44,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		operations.add(new Operation("TIXR",184,OperationLength.TWO_BYTE,
				OperationOperandType.REG,false));
		operations.add(new Operation("WD",220,OperationLength.THREE_OR_FOUR_BYTE,
				OperationOperandType.MEMORY,true));
		writeTable(operations);
	}
	
	public static void writeTable(List<Operation> list) throws FileNotFoundException {
		XMLEncoder encoder =
	        	new XMLEncoder(
	              new BufferedOutputStream(
	                new FileOutputStream("operations.xml")));
		encoder.writeObject(list);
		encoder.close();
	}

}
