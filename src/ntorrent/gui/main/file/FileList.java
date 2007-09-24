package ntorrent.gui.main.file;

import java.awt.Color;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import ntorrent.model.FileTableModel;
import ntorrent.model.units.Byte;
import ntorrent.model.units.Priority;

public class FileList {
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

	public void setInfo(String hash,Vector<Object>[] list) {
		filetable.clearSelection();
		filetable.setVisible(true);
		listener.setHash(hash);
		FileTableModel table = ((FileTableModel)filetable.getModel());
		table.clear();
		for(int y = 0; y < list.length; y++)
			for(int x = 0; x < 3; x++)
				if(x == 0)
					table.setValueAt(new Priority((Long)list[y].get(x)), y, x);
				else if(x == 2)
					table.setValueAt(new Byte((Long)list[y].get(x)), y, x);
				else
					table.setValueAt(list[y].get(x), y, x);
	}
}