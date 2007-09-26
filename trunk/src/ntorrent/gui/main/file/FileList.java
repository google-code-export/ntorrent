package ntorrent.gui.main.file;

import java.awt.Color;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.xmlrpc.XmlRpcRequest;

import ntorrent.io.xmlrpc.Rpc;
import ntorrent.io.xmlrpc.RpcCallback;
import ntorrent.model.FileTableModel;
import ntorrent.model.units.Byte;
import ntorrent.model.units.Priority;

public class FileList extends RpcCallback {
	//Simple filelist.
	JTable filetable = new JTable(new FileTableModel());
	JScrollPane fileList;
	FileTableListener listener = new FileTableListener();
	
	FileList(){
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
		FileTableModel table = ((FileTableModel)filetable.getModel());
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
