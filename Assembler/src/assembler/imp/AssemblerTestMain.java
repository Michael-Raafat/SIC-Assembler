package assembler.imp;

import java.io.File;

import assembler.IAssembler;
/**
 * Main testing class.
 * @author Amr
 *
 */
public final class AssemblerTestMain {
	/**
	 * Private Constructor.
	 */
	private AssemblerTestMain() {
	}
	/**
	 * Main function.
	 * @param args
	 * String cmd arguments.
	 */
	public static void main(final String[] args) {
		IAssembler assembler = new Assembler();
		File folder = new File(".//tests");
		File[] listOfFiles = folder.listFiles();
	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	    	String inFile = ".//tests//" + listOfFiles[i].getName();
	        String outFile = ".//tests//Outputs//"
	        		+ listOfFiles[i].getName().split(".txt")[0];
	    	assembler.assemble(inFile, outFile, false);
	      }
	    }
	}

}
