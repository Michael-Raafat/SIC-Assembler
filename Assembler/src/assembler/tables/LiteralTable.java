package assembler.tables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import assembler.instruction.factory.IInstructionFactory;
import assembler.literal.ILiteral;
import assembler.literal.Literal;

public class LiteralTable implements ILiteralTable {
	private HashMap<String, ILiteral> litTable;
	private IInstructionFactory instructionFactory;
	public LiteralTable() {
		litTable = new LinkedHashMap<String, ILiteral>();
	}

	@Override
	public final Boolean addLiteral(final String literal) {
		if (!litTable.containsKey(literal)) {
			ILiteral tempLiteral = new Literal(literal, instructionFactory);
			if (tempLiteral.isValid()) {
				litTable.put(literal, tempLiteral);
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public final boolean contains(final String literal) {
		return litTable.containsKey(literal);
	}

	@Override
	public final int getStartingAddress(final String literal) {
		ILiteral temp = litTable.get(literal);
		if (temp != null && temp.getAddress() != null) {
			return temp.getAddress();
		}
		return -1;
	}

	@Override
	public final int getSize(final String literal) {
		ILiteral temp = litTable.get(literal);
		if (temp != null) {
			return temp.getSize();
		}
		return -1;
	}

	@Override
	public final String getOpCode(final String literal) {
		ILiteral temp = litTable.get(literal);
		if (temp != null) {
			return temp.getOpCode();
		}
		return null;
	}

	@Override
	public final List<ILiteral> getAllLiterals() {
		List<ILiteral> ret = new ArrayList<ILiteral>();
		for (ILiteral lit : litTable.values()) {
			if(lit.getAddress() != null)
				ret.add(lit);
		}
		return ret;
	}

	@Override
	public final void assignAddress(final String literal, final int address) {
		ILiteral temp = litTable.get(literal);
		if (temp != null) {
			temp.setAddress(address);
		}
	}

	@Override
	public final void clear() {
		litTable.clear();
	}

	@Override
	public final void setInstructionFactory(final IInstructionFactory factory) {
		this.instructionFactory = factory;
	}

	@Override
	public List<ILiteral> getAllUnsignedLiterals() {
		List<ILiteral> ret = new ArrayList<ILiteral>();
		for (ILiteral lit : litTable.values())
			if (lit.getAddress() == null) {
				ret.add(lit);
			}
		return ret;
	}

	@Override
	public final List<String> getTableRows() {
		List<String> list = new ArrayList<String>();
		StringBuilder temp = new StringBuilder();
		temp.append("Literal");
		while (temp.length() < 15) {
			temp.append(" ");
		}
		temp.append("address");
		list.add(temp.toString());
		for (ILiteral value : litTable.values()) {
			temp = new StringBuilder();
			temp.append(value.getLiteral());
			while (temp.length() < 15) {
				temp.append(" ");
			}
			temp.append(value.getAddress());
			list.add(temp.toString());
		}
		return list;
	}

}
