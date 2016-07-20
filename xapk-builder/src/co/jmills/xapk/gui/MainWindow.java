package co.jmills.xapk.gui;

import javax.swing.JFrame;

import co.jmills.xapk.Strings;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 
 * @author Josh Mills
 */
public class MainWindow {

	// Views
	private JFrame mFrame;
	private JButton mBtnOpenXAPK;
	
	// Callbacks
	private MainWindowListener mListener;
	public interface MainWindowListener {
		void openXAPK(JFrame parent);
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
		
		mBtnOpenXAPK = new JButton(Strings.WINDOW_OPEN_XAPK_BUTTON);
		mFrame.getContentPane().add(mBtnOpenXAPK, BorderLayout.CENTER);
		mBtnOpenXAPK.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mListener.openXAPK(mFrame);
			}
		});
	}

}
