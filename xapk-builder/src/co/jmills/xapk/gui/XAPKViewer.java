package co.jmills.xapk.gui;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import co.jmills.xapk.Strings;
import co.jmills.xapk.model.XAPKFile;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;

/**
 * 
 * @author Josh Mills
 */
public class XAPKViewer implements ActionListener {
	
	private XAPKFile mXapkFile;
	private String mTitle;
	
	// Views
	private JFrame mFrame;
	private JTree mTree;
	private JButton mBtnRefresh;
	
	public interface XAPKViewListener {
		
	}
	
	/**
	 * Create the application.
	 */
	public XAPKViewer(XAPKFile xapkFile) {
		this.mXapkFile = xapkFile;
		this.mTitle = xapkFile.getPath();
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
		mFrame.setTitle(mTitle);
		
		setTree();
		
		mBtnRefresh = new JButton(Strings.WINDOW_RETRY);
		mFrame.getContentPane().add(mBtnRefresh, BorderLayout.SOUTH);
		mBtnRefresh.addActionListener(XAPKViewer.this);
	
	}
	
	private DefaultMutableTreeNode parseFiles() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(this.mXapkFile.getPackageName());
		
		File topFile = new File(this.mXapkFile.getPath());
		if (topFile.isDirectory()) {
			for (File file : topFile.listFiles()) {
				top.add(parseInnerFiles(file));
			}
		}
		
		return top;
	}
	
	private MutableTreeNode parseInnerFiles(File file) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(file.getName());
		if (file.isDirectory()) {
			for (File innerFile : file.listFiles()) {
				node.add(parseInnerFiles(innerFile));
			}
			return node;
		} else {
			return node;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.mBtnRefresh) {
			this.refresh();
		}
	}
	
	private void setTree() { 
		mTree = new JTree(parseFiles());
		mFrame.getContentPane().add(mTree, BorderLayout.CENTER);
		mFrame.invalidate();
		mFrame.validate();
	}
	
	private void refresh() {		
		mFrame.getContentPane().remove(mTree);
		setTree();
	}
}
