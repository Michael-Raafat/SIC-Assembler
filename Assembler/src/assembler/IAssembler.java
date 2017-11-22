package assembler;
/**
 * Interface of the assembler class.
 * @author Amr
 *
 */
public interface IAssembler {
	/**
	 * Assembls the source code file and generates both
	 * listing file and object code if no errors are generated.
	 * Otherwise, only generates listing file.
	 * @param inFile
	 * The directory of the source code file.
	 * @param outFileLocation
	 * The location to generate the listing file and the object code.
	 * @param sicXE
	 * True if it is a SIC/XE program, false otherwise.
	 * @return
	 * True if it is assembled without errors.
	 */
	boolean assemble(String inFile, String outFileLocation, boolean sicXE);
}
