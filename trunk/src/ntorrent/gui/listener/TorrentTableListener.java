/**
 *   nTorrent - A GUI client to administer a rtorrent process 
 *   over a network connection.
 *   
 *   Copyright (C) 2007  Kim Eik
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ntorrent.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.apache.xmlrpc.XmlRpcException;

import ntorrent.controller.Controller;
import ntorrent.gui.elements.FileTabComponent;
import ntorrent.model.TorrentFile;

public class TorrentTableListener extends MouseAdapter implements ActionListener {

	JPopupMenu popup;
	Vector<Integer> selectedRows = new Vector<Integer>();

	public TorrentTableListener(){
		String seperator = "---";
		String[] menuItems = {
				"start",
				"stop",
				seperator,
				"open",
				"check hash",
				"close",
				seperator,
				"erase"};
		
	    popup = new JPopupMenu();
	    for(String mitem : menuItems){
	    	if(mitem.equals(seperator)){
	    		popup.add(new JSeparator(SwingConstants.HORIZONTAL));
	    	} else{
		    	JMenuItem menuItem = new JMenuItem(mitem);
		    	menuItem.addActionListener(this);
		    	popup.add(menuItem);
	    	}
	    }
	}	
	
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
        hideFileTab();
    }



	public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
        maybeShowFileTab(e);
    }

	private void maybeShowFileTab(MouseEvent e) {
		JTable source = (JTable)e.getSource();
		if(source.getSelectedRowCount() == 1){
			TorrentFile tf = ((TorrentFile)source.getValueAt(source.getSelectedRow(), 0));
			FileTabComponent panel = Controller.getGui().getFileTab();
			panel.getInfoPanel().setInfo(tf);
			try {
				panel.getFileList().setInfo(Controller.getRpc().getFileList(tf.getHash()));
			} catch (XmlRpcException x) {
				Controller.writeToLog(x);
			}
		}
		
	}

	private void maybeShowPopup(MouseEvent e) {
    	JTable source = (JTable)e.getComponent();
    	if(source.getSelectedRowCount() > 0)
	        if (e.isPopupTrigger()) {
	        	selectedRows.clear();
	            for(int i : source.getSelectedRows())
	            	selectedRows.add(i);
	            popup.show(source,  e.getX(), e.getY());
	        }
    }
    
	private void hideFileTab() {
    	FileTabComponent panel = Controller.getGui().getFileTab();
    	panel.getFileList().hideInfo();
    	panel.getInfoPanel().hideInfo();
	}
    
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		for(int i : selectedRows)
			if(command.equals("start")){
				Controller.writeToLog("Starting torrent");
				Controller.getTorrents().start(i);
			}else if(command.equals("stop")){
				Controller.writeToLog("Stopping torrent");
				Controller.getTorrents().stop(i);
			}else if(command.equals("open")){
				Controller.writeToLog("Setting torrent open");
				Controller.getTorrents().open(i);
			} else if(command.equals("check hash")){
				Controller.writeToLog("Hash checking torrent");
				Controller.getTorrents().checkHash(i);
			}else if(command.equals("close")){
				Controller.writeToLog("Setting torrent closed");
				Controller.getTorrents().close(i);
			} else if(command.equals("erase")){
				Controller.writeToLog("Erasing torrent");
				Controller.getTorrents().erase(i);
			}
	}   
}