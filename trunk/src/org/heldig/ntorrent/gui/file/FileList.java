package org.heldig.ntorrent.gui.file;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.xmlrpc.XmlRpcRequest;
import org.heldig.ntorrent.event.ControllerEventListener;
import org.heldig.ntorrent.gui.listener.JTablePopupMenuImplementation;
import org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback;
import org.heldig.ntorrent.language.Language;
import org.heldig.ntorrent.model.Byte;
import org.heldig.ntorrent.model.Priority;

/**
 * @author   netbrain
 */
public class FileList extends JTablePopupMenuImplementation implements XmlRpcCallback {
	//Simple filelist.
	JTable filetable = new JTable(new FileJTableModel());
	JScrollPane fileList;
	private String hash;
	private ControllerEventListener event;
	
	final static Object[] menuItems = {
		Language.File_List_Menu_high,
		Language.File_List_Menu_low,
		Language.File_List_Menu_off,
	};

	public FileList(ControllerEventListener e){
		super(menuItems);
		event = e;
		filetable.setOpaque(false);
		//not stable here either
		//filetable.setAutoCreateRowSorter(true);
		filetable.setBackground(Color.white);
		filetable.setFillsViewportHeight(true);
		filetable.addMouseListener(this);
		
		/*TableColumn column = null;
		for (int i = 0; i < filetable.getColumnCount(); i++) {
			column = filetable.getColumnModel().getColumn(i);
			switch (i){
				case 0:
					column.setPreferredWidth(10);
				case 2:
					column.setPreferredWidth(100);
				
			}
		}*/
		
		fileList  = new JScrollPane(filetable);
	}
	
	/**
	 * @return
	 */
	public JScrollPane getFileList() {
		return fileList;
	}

	public void hideInfo() {
		filetable.setVisible(false);
	}

	@Override
	public void handleResult(XmlRpcRequest pRequest, Object pResult) {
		filetable.clearSelection();
		filetable.setVisible(true);
		setHash((String)pRequest.getParameter(0));
		FileJTableModel table = ((FileJTableModel)filetable.getModel());
		table.clear();
		Object[] array = (Object[])pResult;
			for(int y = 0; y < array.length ; y++){
				Object[] val = (Object[])array[y];
				table.setValueAt(new Priority((Long)val[0],false), y, 0);
				table.setValueAt(new Byte((Long)val[2]), y, 2);
				table.setValueAt(val[1], y, 1);
			}
		
	}
	
	/**
	 * @param hash
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	protected void maybeShowPopup(MouseEvent e) {
    	JTable source = (JTable)e.getComponent();
    	if(source.getSelectedRowCount() >= 1){
	        if (e.isPopupTrigger()) {
	        	selectedRows = source.getSelectedRows();
	            popup.show(source,  e.getX(), e.getY());
	        }
    	}     	
    }	

	public void actionPerformed(ActionEvent e) {
		System.out.println("Setting priority on "+selectedRows.length+" files = "+hash);
		int pri = 0;
			switch(Language.getFromString(e.getActionCommand())){
			case File_List_Menu_high:
				pri = 2;
				break;
			case File_List_Menu_low:
				pri = 1;
				break;
			case File_List_Menu_off:
				pri = 0;
				break;
			}
		event.setFilePriority(hash,pri,selectedRows);
		//event.handleEvent(new Event(hash,selectedRows,e.getActionCommand()));
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}	
	
}
