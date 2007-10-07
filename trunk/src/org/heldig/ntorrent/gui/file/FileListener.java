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
package org.heldig.ntorrent.gui.file;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import org.heldig.ntorrent.Controller;
import org.heldig.ntorrent.gui.listener.JTablePopupMenuListener;


/**
 * @author  Kim Eik
 */
public class FileListener extends JTablePopupMenuListener {
	final static String[] menuItems = {"High","Low","Off"};
	private String hash;
	private Controller C;
	
	public FileListener(Controller c) {
		super(c,menuItems);
		C = c;
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
		String command = e.getActionCommand();
		if(command.equals("High")){
			C.IO.getRpc().setFilePriority(hash, 2, selectedRows);
		}else if(command.equals("Low")){
			C.IO.getRpc().setFilePriority(hash, 1, selectedRows);			
		}else if(command.equals("Off")){
			C.IO.getRpc().setFilePriority(hash, 0, selectedRows);		
		}
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
