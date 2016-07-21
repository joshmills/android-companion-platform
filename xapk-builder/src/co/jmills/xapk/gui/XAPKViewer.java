package co.jmills.xapk.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import co.jmills.xapk.Strings;
import co.jmills.xapk.model.XAPKFile;

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
	private JButton mBtnSearch;
	
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
		
		JPanel panel = new JPanel();
		mFrame.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		mBtnRefresh = new JButton(Strings.WINDOW_RETRY);
		panel.add(mBtnRefresh);
		mBtnRefresh.addActionListener(XAPKViewer.this);
		
		mBtnSearch = new JButton(Strings.WINDOW_SEARCH);
		panel.add(mBtnSearch);
		mBtnSearch.addActionListener(XAPKViewer.this);
	}
	
	private DefaultMutableTreeNode parseFiles(FilenameFilter filenameFilter) {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(this.mXapkFile.getPath());
		
		File topFile = new File(this.mXapkFile.getPath());
		if (topFile.isDirectory()) {
			for (File file : topFile.listFiles(filenameFilter)) {
				MutableTreeNode innerNode = parseInnerFiles(file, filenameFilter);
				
				if (innerNode != null) {
					top.add(innerNode);
				}
			}
		}
		
		return top;
	}
	
	private MutableTreeNode parseInnerFiles(File file, FilenameFilter filenameFilter) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(file.getName());
		if (file.isDirectory()) {
			
			for (File innerFile : file.listFiles(filenameFilter)) {
				MutableTreeNode innerNode = parseInnerFiles(innerFile, filenameFilter);
				if (innerNode != null) {
					if (innerFile.isDirectory() && innerNode.getChildCount() > 0 || innerFile.isFile()){
						node.add(innerNode);
					}
				}
			}
			
			if (node.getChildCount() > 0) {
				return node;	
			} else {
				return null;
			}

		} else {
			return node;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == this.mBtnRefresh) {
			this.refresh();
		}
		
		else if (e.getSource() == this.mBtnSearch) {
			this.search();
		}
	}
	
	private void setTree() { 
		setTree("");
	}
	
	private void setTree(final String nameFilter) {
		mTree = new JTree(parseFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (new File(dir.getAbsolutePath() + File.separator + name).isDirectory()) {
					// Allow directories
					return true;
				} else {
					return name.contains(nameFilter);	
				}
			}
		}));
		
		mTree.setEditable(true);
		
		// Add support for right clicking on the tree of files
		MouseListener mouseListener = new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					
					final int row = mTree.getClosestRowForLocation(e.getX(), e.getY());
					mTree.setSelectionRow(row);
					
					// Show the context menu for the closest row
					PopUpMenu menu = new PopUpMenu(new PopUpMenuListener() {
						public void delete() {
							String fileName = mTree.getPathForRow(row).getLastPathComponent().toString();
							System.out.println("Delete file : " + fileName);
							DefaultTreeModel model = (DefaultTreeModel) mTree.getModel();
							model.removeNodeFromParent((MutableTreeNode) mTree.getPathForRow(row).getLastPathComponent());
						}

						@Override
						public void showSizeInfo() {
							File selectedFile = getFileFromTreePath(mTree.getPathForRow(row));
							
							String sizeString = "Size : " + selectedFile.length() + "b";
							System.out.println(sizeString);
							JOptionPane.showMessageDialog(
									mFrame, 
									sizeString);
						};
					});
					menu.show(mTree, e.getX(), e.getY());		
				}
			}
		};
		mTree.addMouseListener(mouseListener);	
		mFrame.getContentPane().add(mTree, BorderLayout.CENTER);
		mFrame.invalidate();
		mFrame.validate();
		expandAll(mTree);
	}
	
	private File getFileFromTreePath(TreePath treePath) {
		String path = "";
		for (Object node : treePath.getPath()) {
			path += ((MutableTreeNode) node).toString() + File.separator;
		}
		return new File(path);
	}
	
	private void refresh() {		
		refresh("");
	}
	
	private void refresh(String searchFilter) {
		System.out.println("Refresh");
		mFrame.getContentPane().remove(mTree);
		setTree(searchFilter);
	}
	
	private void search() {
		System.out.println("Search");
				
		final String searchString = (String) JOptionPane.showInputDialog(
				mFrame,
				"",
				"Search:",
				JOptionPane.PLAIN_MESSAGE,
				null,
				null,
				"");
				
		if ((searchString != null) && (searchString.length() > 0)) {
			System.out.println("Search value: " + searchString);
			refresh(searchString);
		} else {
			refresh();
		}
	}
	
	private void expandAll(JTree tree) {
		int j = tree.getRowCount();
		int i = 0;
		while (i < j) {
			tree.expandRow(i);
			i++;
			j = tree.getRowCount();
		}	
	}
	
	@SuppressWarnings("serial")
	class PopUpMenu extends JPopupMenu implements ActionListener {
		
		private JMenuItem mSizeInfoItem;
		private JMenuItem mDeleteItem;
		private PopUpMenuListener mListener;
		
		public PopUpMenu(PopUpMenuListener listener) {
			mListener = listener;
			
			mDeleteItem = new JMenuItem(Strings.XAPK_POPUP_DELETE);
			mDeleteItem.addActionListener(this);
			
			mSizeInfoItem = new JMenuItem(Strings.XAPK_POPUP_SIZE);
			mSizeInfoItem.addActionListener(this);
			
			add(mDeleteItem);
			add(mSizeInfoItem);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == mDeleteItem) {
				mListener.delete();
			}
			
			else if (e.getSource() == mSizeInfoItem) {
				mListener.showSizeInfo();
			}
		}
	}
	
	public interface PopUpMenuListener {
		void delete();
		void showSizeInfo();
	}
}
