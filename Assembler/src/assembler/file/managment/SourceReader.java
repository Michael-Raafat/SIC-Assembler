package assembler.file.managment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Implementation of the IReader.
 * @author Amr
 *
 */
public class SourceReader implements IReader {
	/**
	 * Constants.
	 */
	private static final int TAB_SIZE = 4,
			INSTRUCTION_SIZE = 35;
	@Override
	public final List<String> readFile(final String path) {
		List<String> instructions = new ArrayList<String>();
		File file = new File(path);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while (line != null) {
				StringBuilder ins = new StringBuilder();
				for (int i = 0; i < line.length(); i++) {
					if (line.charAt(i) == '\t') {
						int k = TAB_SIZE - (ins.length() % TAB_SIZE),
								j = 0;
						while (j < k) {
							j++;
							ins.append(" ");
						}
					} else {
						ins.append(line.charAt(i));
					}
				}
				for (int i = ins.length(); i < INSTRUCTION_SIZE; i++) {
					ins.append(" ");
				}
				instructions.add(ins.toString());
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("File not found");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error while reading file");
		}
		return instructions;
	}

}
