package assembler.pass.parsers.imp;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import assembler.instruction.Instruction;
import assembler.instruction.factory.IInstructionFactory;
import assembler.literal.ILiteral;
import assembler.pass.parsers.IFirstPassParser;
import assembler.tables.ILiteralTable;
import assembler.tables.IOperationTable;
import assembler.tables.ISymbolTable;

/**
 * First parser implementation.
 * 
 * @author Ahmed
 *
 */
public class FirstPassParser implements IFirstPassParser {
	/**
	 * The symbol table.
	 */
	private ISymbolTable symTable;
	/**
	 * The operation table.
	 */
	private IOperationTable opTable;
	/**
	 * The instruction factory.
	 */
	private IInstructionFactory insFactory;
	/**
	 * the literal table.
	 */
	private ILiteralTable litTable;
	/**
	 * End index.
	 */
	private Integer endIndex;
	/**
	 * Constants.
	 */
	private static final int LABEL_START = 0, LABEL_END = 7, MNEMONIC_START = 9, MNEMONIC_END = 14, OPERANDS_START = 17,
			OPERANDS_END = 34, BASE = 16, MAX_MEMORY_ADDRESS = 32768;
	/**
	 * Expression Evaluator.
	 */
	private ScriptEngineManager mgr;
	private ScriptEngine engine;

	/**
	 * Constructor.
	 * 
	 * @param sym
	 *            The symbol table to use.
	 * @param opt
	 *            The operation table to use.
	 * @param factory
	 *            The factory to use.
	 * @param lTable
	 *            The literal table.
	 */
	public FirstPassParser(final ISymbolTable sym, final IOperationTable opt, final IInstructionFactory factory,
			final ILiteralTable lTable) {
		symTable = sym;
		opTable = opt;
		insFactory = factory;
		litTable = lTable;
		mgr = new ScriptEngineManager();
		engine = mgr.getEngineByName("JavaScript");
	}

	@Override
	public final List<Instruction> firstPass(final List<String> sourceCode) {
		List<Instruction> list = new ArrayList<Instruction>();
		int locctr = 0, firstStatement = -1;
		endIndex = null;
		int lastORG = -1;
		int equLoc = 0;
		boolean start = false;
		boolean ltorg = false, equ = false;
		for (int i = 0; i < sourceCode.size(); i++) {
			ltorg = false;
			equ = false;
			String line = sourceCode.get(i);
			// comment line
			if (line.charAt(0) == '.') {
				continue;
			}
			// set the first statement in the sourceCode
			// (in case there were comments)
			if (firstStatement == -1) {
				firstStatement = i;
			}
			String label = "", mnemonic = "", operands = "";
			label = line.substring(LABEL_START, LABEL_END).trim();
			mnemonic = line.substring(MNEMONIC_START, MNEMONIC_END).trim();
			operands = line.substring(OPERANDS_START, OPERANDS_END).trim();
			Instruction ins = insFactory.generateInstruction();
			if (operands.startsWith("=")) {
				if (operands.equals("=*")) {
					String modOperand = "=W'" + Integer.toString(locctr) + "'";
					if (!litTable.addLiteral(modOperand)) {
						ins.setError(true);
						ins.setErrorMessage("Invalid Literal");
					}
				} else {
					if (!litTable.addLiteral(operands)) {
						ins.setError(true);
						ins.setErrorMessage("Invalid Literal");
					}
				}
			}
			ins.setLabel(label);
			ins.setOperands(operands);
			ins.setOperation(mnemonic);
			ins.setLocation(locctr);
			if (!label.matches("[a-zA-Z]+[a-zA-Z0-9]*\\s*") && label.length() != 0) {
				ins.setError(true);
				ins.setErrorMessage("Label must start with a letter" + " and only contain letters and numbers.");
			}
			if (!(line.substring(LABEL_END, MNEMONIC_START).trim().isEmpty()
					&& line.substring(MNEMONIC_END, OPERANDS_START).trim().isEmpty())) {
				ins.setError(true);
				ins.setErrorMessage("Doesn't follow the fixed format.");
			}
			// multiple "START"
			if (mnemonic.compareToIgnoreCase("START") == 0) {
				if (start || firstStatement != i) {
					ins.setError(true);
					ins.setErrorMessage("START must be " + "used only as the first statemenet!");
				} else {
					start = true;
					if (!operands.equals("")) {
						try {
							if (operands.length() > 1 && operands.substring(0, 2).compareToIgnoreCase("0x") == 0) {
								locctr = Integer.parseInt(operands.substring(2), BASE);
							} else {
								locctr = Integer.parseInt(operands, BASE);
							}
							ins.setLocation(locctr);
						} catch (Exception e) {
							ins.setError(true);
							ins.setErrorMessage("Invalid operand" + " for start directive.");
						}
					}
					if (!label.equals("")) {
						symTable.add(label, locctr);
					}
				}
			} else if (symTable.contains(label)) {
				ins.setError(true);
				ins.setErrorMessage("label: " + label + " is used before!");
			} else {
				if (mnemonic.compareToIgnoreCase("ORG") == 0) {
					if (label.length() > 0) {
						ins.setError(true);
						ins.setErrorMessage("ORG statement doens't have labels");
					} else {
						if (operands.length() == 0) {
							if (lastORG != -1)
								locctr = lastORG;
						} else {
							Integer loc = evaluateExpression(operands, locctr, true);
							if (loc < 0) {
								ins.setError(true);
								ins.setErrorMessage("Invalid Expression");
							} else {
								lastORG = locctr;
								locctr = loc;
								ins.setLocation(locctr);
							}
						}
					}
				} else if (mnemonic.compareToIgnoreCase("EQU") == 0) {
					if (label.length() == 0) {
						ins.setError(true);
						ins.setErrorMessage("EQU statement requires a symbol");
					} else {
						if (operands.length() == 0) {
							ins.setError(true);
							ins.setErrorMessage("EQU statement requires a value");
						} else {
							Integer loc = evaluateExpression(operands, locctr, false);
							if (loc < 0) {
								ins.setError(true);
								ins.setErrorMessage("Invalid Expression");
							} else {
								symTable.add(label, loc);
								equLoc = loc;
								equ = true;
							}
						}
					}
				} else if (mnemonic.compareToIgnoreCase("LTORG") == 0) {
					if (!label.isEmpty() || !operands.isEmpty()) {
						ins.setError(true);
						ins.setErrorMessage("Label and Operands must be empty with LTORG!");
					} else {
						ltorg = true;
					}
				} else if (!label.equals("")) {
					if (label.matches("[a-zA-Z]+[a-zA-Z0-9]*\\s*"))
						symTable.add(label, locctr);
					// } else {
					// ins.setError(true);
					// ins.setErrorMessage("Label must start with a letter"
					// + " and only contain letters and numbers.");
					// }
				}
			}
			if (opTable.isOperation(label) || opTable.isDirective(label)) {
				ins.setError(true);
				ins.setErrorMessage("Label can't be an " + "opeartion or a directive.");
			}
			locctr = ins.getEndLocation();
			if (locctr >= MAX_MEMORY_ADDRESS) {
				ins.setError(true);
				ins.setErrorMessage("Out of sic machine memory !");
			}
			list.add(ins);
			if (ltorg) {
				locctr = assignLiterals(locctr, list);
			}
			if (equ) {
				ins.setLocation(equLoc);
			}
		}
		endIndex = list.size() - 1;
		Instruction endIns = list.remove((int) endIndex);
		assignLiterals(locctr, list);
		list.add(endIns);
		return list;
	}

	private int evaluateExpression(String s, int locctr, boolean reloc) {
		int ret = 0;
		int cntAddedSubtracted = 0;
		Boolean add = true, startedWord = false, startedAddress = false;
		String temp = new String(s.concat("+"));
		StringBuilder finalWord = new StringBuilder();
		StringBuilder word = new StringBuilder();
		for (int i = 0; i < temp.length(); i++) {
			if (temp.charAt(i) == '+' || temp.charAt(i) == '-') {
				add = temp.charAt(i) == '+';
				if (startedWord) {
					if (word.toString().equals("*")) {
						finalWord.append(Integer.toString(locctr));
						cntAddedSubtracted += add ? 1 : -1;
					} else if (symTable.contains(word.toString())) {
						finalWord.append(Integer.toString(symTable.getAddress(word.toString())));
						cntAddedSubtracted += add ? 1 : -1;
					} else if (litTable.getStartingAddress(word.toString()) != -1) {
						finalWord.append(Integer.toString(litTable.getStartingAddress(word.toString())));
						cntAddedSubtracted += add ? 1 : -1;
					} else {
						ret = -1;
						break;
						// finalWord.append(word);
						// int hex = hexToInt(word.toString());
						// if(hex != -1) {
						// finalWord.append(Integer.toString(hex));
						// }
						// else {
						// ret = -1;
						// break;
						// }
					}
				} else {

					int hex = verifyInt(word.toString());
					if (hex != -1) {
						finalWord.append(Integer.toString(hex));
					} else {
						ret = -1;
						break;
					}
				}
				word = new StringBuilder();
				startedWord = false;
				startedAddress = false;
				finalWord.append(temp.charAt(i));
			} else if (temp.charAt(i) != ' ') {
				word.append(temp.charAt(i));
				if (!startedWord && !startedAddress) {
					if (Character.isAlphabetic(temp.charAt(i)) || temp.charAt(i) == '=' || temp.charAt(i) == '*') {
						startedWord = true;
					} else if (Character.isDigit(temp.charAt(i))) {
						startedAddress = true;
					} else {
						ret = -1;
						break;
					}
				}
			}
		}
		if (ret < 0 || (reloc && (cntAddedSubtracted > 1 || cntAddedSubtracted < 0)))
			return -1;
		try {
			finalWord.append("0");
			ret = (int) engine.eval(finalWord.toString());
		} catch (ScriptException e) {
			ret = -1;
		}
		return ret;
	}

	private int verifyInt(String word) {
		int ret = 0;
		try {
			ret = Integer.parseInt(word, 10);
		} catch (NumberFormatException e) {
			ret = -1;
		}
		return ret;
	}

	private int assignLiterals(int locctr, List<Instruction> list) {
		int newLocctr = locctr;
		List<ILiteral> literals = litTable.getAllUnsignedLiterals();
		for (ILiteral lit : literals) {
			litTable.assignAddress(lit.getLiteral(), newLocctr);
			newLocctr += lit.getSize();
			if (newLocctr >= MAX_MEMORY_ADDRESS) {
				lit.getInstruction().setError(true);
				lit.getInstruction().setErrorMessage("Out of sic machine memory !");
			}
			if (list != null) {
				list.add(lit.getInstruction());
			}
		}
		return newLocctr;
	}

	@Override
	public final Integer getEndIndex() {
		return endIndex;
	}

}