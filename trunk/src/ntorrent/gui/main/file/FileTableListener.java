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
package ntorrent.gui.main.file;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import ntorrent.gui.core.AbstractJTablePopupMenu;
import ntorrent.threads.FileCommandThread;

/**
 * 
 * @author Kim Eik
 *
 */
public class FileTableListener extends AbstractJTablePopupMenu {
	final static String[] menuItems = {"High","Low","Off"};
	private String hash;
	
	public FileTableListener() {
		super(menuItems);
	}
	
	/**
	 * @param hash
	 * @uml.property  name="hash"
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
		Runnable fileCommand = new FileCommandThread(e.getActionCommand(),hash,selectedRows);
		Thread actionThread = new Thread(fileCommand);
		actionThread.start();
	}


}
