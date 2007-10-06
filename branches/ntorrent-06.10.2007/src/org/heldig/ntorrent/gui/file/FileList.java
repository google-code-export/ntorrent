package org.heldig.ntorrent.gui.file;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTable;


import org.apache.xmlrpc.XmlRpcRequest;
import org.heldig.ntorrent.Controller;
import org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback;
import org.heldig.ntorrent.model.FileJTableModel;
import org.heldig.ntorrent.model.units.Byte;
import org.heldig.ntorrent.model.units.Priority;

/**
 * @author   netbrain
 */
public class FileList extends XmlRpcCallback {
	//Simple filelist.
	JTable filetable = new JTable(new FileJTableModel());
	JScrollPane fileList;
	FileListener listener;
	
	public FileList(Controller c){
		listener =  new FileListener(c);
		filetable.setOpaque(false);
		//not stable here either
		//filetable.setAutoCreateRowSorter(true);
		filetable.setBackground(Color.white);
		filetable.setFillsViewportHeight(true);
		filetable.addMouseListener(listener);
		
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
		listener.setHash((String)pRequest.getParameter(0));
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
}
