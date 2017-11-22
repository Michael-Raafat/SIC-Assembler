package assembler.file.managment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import assembler.instruction.Instruction;
import assembler.record.Record;
/**
 * Implementation of the IWriter interface.
 * @author Amr
 *
 */
public class SourceWriter implements IWriter {

	@Override
	public final void writeRecord(final String outFile,
			final List<Record> records) {
		File file = new File(outFile);
		BufferedWriter write;
		try {
			write = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < records.size(); i++) {
				write.write(records.get(i).toString());
				if (i != records.size() - 1) {
				write.newLine();
				}
			}
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public final void writeInstructions(final String outFile,
			final List<Instruction> instructions,
			final boolean intermediate) {
		File file = new File(outFile);
		BufferedWriter write;
		try {
			write = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < instructions.size(); i++) {
				if (intermediate) {
					write.write(instructions.get(i).toIntermediateString());
				} else {
					write.write(instructions.get(i).toString());
				}
				if (i != instructions.size() - 1) {
					write.newLine();
				}
			}
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public final void writeTables(final String outFile,
			final List<String> tableRows) {
		File file = new File(outFile);
		BufferedWriter write;
		try {
			write = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < tableRows.size(); i++) {

					write.write(tableRows.get(i));
				if (i != tableRows.size() - 1) {
					write.newLine();
				}
			}
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
