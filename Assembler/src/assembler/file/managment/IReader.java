package assembler.file.managment;

import java.util.List;

/**
 * Interface for the file reader.
 * @author Amr
 *
 */
public interface IReader {
	/**
	 * Scans and returns a list of string containing the data
	 * in a file.
	 * @param path
	 * The path of the file to read.
	 * @return
	 * List of strings, each string containing a line of the file.
	 */
	List<String> readFile(String path);
}
