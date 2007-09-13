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
package ntorrent.gui.tray;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ntorrent.settings.Constants;
import ntorrent.settings.Constants.Commands;

public class TrayIconPopUpMenu {
	JPopupMenu popup = new JPopupMenu();
	TrayIconPopUpMenu() {
		String[] menu = {
				Commands.ADD_TORRENT.toString(),
				Commands.ADD_URL.toString(),
				null,
				Commands.START_ALL.toString(),
				Commands.STOP_ALL.toString(),
				null,
				Commands.QUIT.toString()
				
		};
		
		for(String mi : menu){
			if(mi == null)
				popup.addSeparator();
			else{
				JMenuItem menuItem = new JMenuItem(mi);
				menuItem.addActionListener(Constants.trayListener);
				popup.add(menuItem);
			}
		}
		
	}
	
	public JPopupMenu getPopup() {
		return popup;
	}
}
