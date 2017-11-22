package assembler.main;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import assembler.IAssembler;
import assembler.imp.Assembler;
/**
 * JFrame of the gui for the assembler gui.
 * @author Amr
 *
 */
public class AssemblerFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4637703319694127353L;
	/**
	 * Screen size and width.
	 */
	private static final int DEFAULT_SCREEN_WIDTH = 500,
			DEFAULT_SCREEN_HEIGHT = 500,
			FONT_SIZE = 16,
			BORDER_LEFT = 10, BORDER_TOP = 10, BORDER_BOTTOM = 10,
			BORDER_RIGHT = 10;
	/**
	 * Assembler.
	 */
	private IAssembler assembler;
	/**
	 * Labels.
	 */
	private JLabel status, inPath, outPath;
	/**
	 * Constructor.
	 */
	public AssemblerFrame() {
		super();
		this.setTitle("Assembler");
		this.setSize(DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT);
		Container contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		assembler = new Assembler();
		JButton button = new JButton("Assemble");
		status = new JLabel("Waiting for input....");
		inPath = new JLabel("The source code file path...");
		outPath = new JLabel("The output path and name");
		status.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, FONT_SIZE));
		Border empty = BorderFactory.createEmptyBorder(BORDER_TOP,
				BORDER_LEFT, BORDER_BOTTOM,
				BORDER_RIGHT);
		status.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLoweredBevelBorder(),
				empty));
		inPath.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedSoftBevelBorder(),
				empty));
		outPath.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedSoftBevelBorder(),
				empty));
		status.setOpaque(true);
		status.setBackground(Color.white);
		status.setAlignmentX(CENTER_ALIGNMENT);
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(inPath);
		JButton inFile = new JButton("Choose source file");
		inFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter txtfilter
				= new FileNameExtensionFilter(
						"txt files (*.txt)", "txt");
				FileNameExtensionFilter asmfilter
				= new FileNameExtensionFilter(
						"asm files (*.asm)", "asm");
				chooser.addChoosableFileFilter(txtfilter);
				chooser.addChoosableFileFilter(asmfilter);
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileFilter(asmfilter);
				int choice = chooser.showOpenDialog(null);
				if (choice != JFileChooser.APPROVE_OPTION) {
					return;
				}
				File chosenFile = chooser.getSelectedFile();
				if (chooser.getFileFilter().equals(txtfilter)
						|| chooser.getFileFilter().equals(asmfilter)) {
					inPath.setText(chosenFile.getAbsolutePath());
				} else {
					inPath.setText("Invalid file chosen.");
				}
			}
		});
		panel.add(inFile);
		panel.setAlignmentX(CENTER_ALIGNMENT);
		this.add(Box.createVerticalGlue());
		this.add(panel);
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(outPath);
		JButton outFile = new JButton("Choose output name");
		outFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				int choice = chooser.showOpenDialog(null);
				if (choice != JFileChooser.APPROVE_OPTION) {
					return;
				}
				File chosenFile = chooser.getSelectedFile();
				if (!chooser.getSelectedFile().getName().trim().equals("")) {
					outPath.setText(chosenFile.getAbsolutePath());
				} else {
					outPath.setText("Invalid file name or directory.");
				}
			}
		});
		panel.add(outFile);
		panel.setAlignmentX(CENTER_ALIGNMENT);
		this.add(Box.createVerticalGlue());
		this.add(panel);
		button.setAlignmentX(CENTER_ALIGNMENT);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				try {
					if (assembler.assemble(inPath.getText(),
							outPath.getText(), false)) {
						status.setText("Assembled successfully");
						status.setBackground(Color.GREEN);
					} else {
						status.setText("Failed! Only listing "
								+ "file might be generated");
						status.setBackground(Color.YELLOW);
					}
				} catch (Exception e) {
					status.setText("CRITICAL ERROR! No files generated.");
					status.setBackground(Color.RED);
				}
			}
		});
		this.add(Box.createVerticalGlue());
        this.add(button);
		this.add(Box.createVerticalGlue());
		this.add(status);
		this.add(Box.createVerticalGlue());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

}
