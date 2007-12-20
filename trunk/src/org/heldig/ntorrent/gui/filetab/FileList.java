package org.heldig.ntorrent.gui.filetab;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.xmlrpc.XmlRpcRequest;
import org.heldig.ntorrent.event.ControllerEventListener;
import org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback;
import org.heldig.ntorrent.language.Language;
import org.heldig.ntorrent.model.Byte;
import org.heldig.ntorrent.model.Priority;

/**
 * @author   Kim Eik
 */
public class FileList extends JScrollPane implements XmlRpcCallback,MouseListener,ActionListener {
	private static final long serialVersionUID = 1L;
	private static final JTable filetable = new JTable(new FileJTableModel());
	private static final JPopupMenu popup = new JPopupMenu();
	private int[] selectedRows;
	private String hash;
	private ControllerEventListener event;
	
	public FileList(ControllerEventListener e){
		super(filetable);
		event = e;
		popup.add(Language.File_List_Menu_high).addActionListener(this);
		popup.add(Language.File_List_Menu_low).addActionListener(this);
		popup.add(Language.File_List_Menu_off).addActionListener(this);
		filetable.setOpaque(false);
		filetable.setBackground(Color.white);
		filetable.addMouseListener(this);
	}

	
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

	
	public void mousePressed(MouseEvent e) {
		if(e.isPopupTrigger())
			maybeShowPopup(e);
	}

	
	public void mouseReleased(MouseEvent e) {
		if(e.isPopupTrigger())
			maybeShowPopup(e);
	}	
	
}
