package co.jmills.xapk.gui;

import javax.swing.JFrame;

import co.jmills.xapk.Strings;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;

/**
 * 
 * @author Josh Mills
 */
public class MainWindow implements ActionListener {

	// Views
	private JFrame mFrame;
	private JButton mBtnOpenXAPK;
	private JButton mBtnSaveXAPK;
	
	// Callbacks
	private MainWindowListener mListener;
	public interface MainWindowListener {
		void openXAPK(JFrame parent);
		void saveXAPK(JFrame parent);
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
		
		mBtnSaveXAPK = new JButton("Save XAPK");
		mFrame.getContentPane().add(mBtnSaveXAPK);
		mBtnSaveXAPK.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mBtnSaveXAPK) {
			mListener.saveXAPK(mFrame);
		} else if (e.getSource() == mBtnOpenXAPK) {
			mListener.openXAPK(mFrame);
		}
	}

}
