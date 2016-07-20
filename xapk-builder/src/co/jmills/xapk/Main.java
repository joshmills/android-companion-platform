package co.jmills.xapk;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import co.jmills.xapk.gui.MainWindow;
import co.jmills.xapk.gui.MainWindow.MainWindowListener;
import co.jmills.xapk.model.XAPKFile;

/**
 * 
 * @author Josh Mills
 */
public class Main {

	/**
	 * The starting point for the application.
	 */
	public static void main(String[] args) {
		
		// Run the GUI
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				// Create the listener for callbacks
				MainWindowListener listener = new MainWindowListener() {
					
					@Override
					public void openXAPK(JFrame parent) {
						
						System.out.println("Open XAPK");
						final JFileChooser fc = new JFileChooser();
						
						try {
							
							// Load the files
							String inputFilePath = handleFileInput(fc, parent);
							String outputFilePath = handleFileInput(fc, parent, true);
							
							// Log the output
							System.out.println("Zip file: " + inputFilePath + ".");
							System.out.println("Output file: " + outputFilePath + ".");
							
							// Unzip the file
							ZipHandler.unZip(inputFilePath, outputFilePath);
							
						} catch (IOException e) {
							System.err.println("No file chosen.");
						}
					}

					@Override
					public void saveXAPK(JFrame parent, String packageName, int versionCode) {
						
						System.out.println("Save XAPK");
						final JFileChooser fc = new JFileChooser();
						
						try {
							
							// Select the file input
							String inputFilePath = handleFileInput(fc, parent, true);
							System.out.println("Input file: " + inputFilePath + ".");
							
							// Handle the xapk compression
							XAPKFile generatedFile = ZipHandler.zip(inputFilePath, packageName, versionCode);
							
							// Output the result to the user
							String completedMessage = String.format(
									Locale.getDefault(), 
									Strings.XAPK_SUCCESS_DETAILS, 
									generatedFile.getPath(), 
									generatedFile.getBytes());
							System.out.println(completedMessage);
							JOptionPane.showMessageDialog(
									parent, 
									completedMessage);
							
						} catch (IOException e) {
							System.err.println("No file chosen.");
						}
					}
				};
				
				try {
					
					// Create the window
					MainWindow window = new MainWindow(listener);
					window.show();
					
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		});
	}
	
	/**
	 * @param chooser JFileChooser The chooser used to select the file.
	 * @param parent JFrame The parent window used to present the dialog.
	 * @return String The file path of the chosen file.
	 * @throws IOException
	 */
	private static String handleFileInput(JFileChooser chooser, JFrame parent) throws IOException {
		return handleFileInput(chooser, parent, false);
	}
	
	/**
	 * @param chooser JFileChooser The chooser used to select the file.
	 * @param parent JFrame The parent window used to present the dialog.
	 * @param onlyDirectories boolean True if the chooser will only display directories.
	 * @return String The file path of the chosen file.
	 * @throws IOException
	 */
	private static String handleFileInput(JFileChooser chooser, JFrame parent, boolean onlyDirectories) throws IOException {
				
		// Setup the chooser
		if (onlyDirectories) {
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		} else {
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		}
		
		// Show the dialog
		int result = chooser.showOpenDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selecetedFile = chooser.getSelectedFile();
			return selecetedFile.getAbsolutePath();
		} else {
			throw new IOException();
		}
	}
}
