package co.jmills.xapk.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import co.jmills.xapk.Strings;
import co.jmills.xapk.model.XAPKFile;

/**
 * 
 * @author Josh Mills
 */
public class MainWindow implements ActionListener {

	// Views
	private JFrame mFrame;
	private JButton mBtnExtractXAPK;
	private JButton mBtnOpenXAPK;
	private JButton mBtnSaveXAPK;
	private JButton mBtnAddFile;
	
	private JTextField mTxtPackageName;
	private JTextField mTxtVersioncode;
	private JPanel panel;
	
	// Callbacks
	private MainWindowListener mListener;
	public interface MainWindowListener {
		void extractXAPK(MainWindow context, JFrame parent);
		void openXAPK(MainWindow context, JFrame parent);
		void saveXAPK(MainWindow context, JFrame parent, String packageName, int versionCode);
		void addToXAPK(MainWindow context, JFrame parent);
	}
	
	/**
	 * Create the application.
	 */
	public MainWindow(MainWindowListener listener) {
		this.mListener = listener;
		initialize();
	}
	
	public void show() {
		mFrame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		mFrame = new JFrame();
		mFrame.setBounds(100, 100, 450, 300);
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mFrame.setTitle(Strings.WINDOW_TITLE);
		mFrame.getContentPane().setLayout(new BoxLayout(mFrame.getContentPane(), BoxLayout.Y_AXIS));
		
		mBtnOpenXAPK = new JButton(Strings.WINDOW_OPEN_XAPK_BUTTON);
		mFrame.getContentPane().add(mBtnOpenXAPK);
		mBtnOpenXAPK.addActionListener(this);
		
		mBtnExtractXAPK = new JButton(Strings.WINDOW_EXTRACT_XAPK_BUTTON);
		mFrame.getContentPane().add(mBtnExtractXAPK);
		mBtnExtractXAPK.addActionListener(this);
		
		mBtnSaveXAPK = new JButton("Save XAPK");
		mFrame.getContentPane().add(mBtnSaveXAPK);
		mBtnSaveXAPK.addActionListener(this);
		
		mBtnAddFile = new JButton(Strings.WINDOW_ADD_FILE);
		mBtnAddFile.setEnabled(false);
		mFrame.getContentPane().add(mBtnAddFile);
		mBtnAddFile.addActionListener(this);
		
		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mFrame.getContentPane().add(panel);
		
		mTxtPackageName = new JTextField();
		mTxtPackageName.setText("com.example.package");
		panel.add(mTxtPackageName);
		mTxtPackageName.setColumns(20);
		
		mTxtVersioncode = new JTextField();
		panel.add(mTxtVersioncode);
		mTxtVersioncode.setText("0");
		mTxtVersioncode.setColumns(10);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mBtnSaveXAPK) {
			
			int versionCode;
			try {
				
				versionCode = Integer.parseInt(mTxtVersioncode.getText());
				mListener.saveXAPK(MainWindow.this, mFrame, mTxtPackageName.getText(), versionCode);
				
			} catch (NumberFormatException ex) {
				System.err.println("Invalid version code: NaN");
			}
		} else if (e.getSource() == mBtnExtractXAPK) {
			mListener.extractXAPK(MainWindow.this, mFrame);
		} else if (e.getSource() == mBtnAddFile) {
			mListener.addToXAPK(MainWindow.this, mFrame);
		} else if (e.getSource() == mBtnOpenXAPK) {
			mListener.openXAPK(MainWindow.this, mFrame);
		}
	}

	public void setXAPKFile(XAPKFile loadedFile) {
		mTxtPackageName.setText(loadedFile.getPackageName());
		mTxtVersioncode.setText(String.valueOf(loadedFile.getVersionCode()));
	}

}
