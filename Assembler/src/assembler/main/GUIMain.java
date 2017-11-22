package assembler.main;

import javax.swing.SwingUtilities;


/**
 * Final gui main class.
 * @author Amr
 *
 */
public final class GUIMain {
	/**
	 * Private constructor for utility class.
	 */
	private GUIMain() {
	}
	/**
	 * Main function.
	 * @param args
	 * cmd args.
	 */
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AssemblerFrame();
                }
        });
	}

}
