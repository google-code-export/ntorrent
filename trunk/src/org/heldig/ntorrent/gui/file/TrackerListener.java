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
 * @author Kim Eik
 *
 */
public class TrackerListener extends JTablePopupMenuListener {

	
	final static Object[] menuItems = {
		"Enable","Disable"	
	};
	
	TrackerList parent;
	
	public TrackerListener(Controller c, TrackerList parent) {
		super(c, menuItems);
		this.parent = parent;
	}

	@Override
	protected void maybeShowPopup(MouseEvent e) {
		selectedRows = ((JTable)e.getSource()).getSelectedRows();
		popup.show(e.getComponent(), e.getX(), e.getY());
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("Enable")){
			C.getIO().getRpc().setTrackerEnabled(parent.getHash(), selectedRows, true, null);
		}else if(cmd.equals("Disable")){
			C.getIO().getRpc().setTrackerEnabled(parent.getHash(), selectedRows, false, null);
		}
	}
}
