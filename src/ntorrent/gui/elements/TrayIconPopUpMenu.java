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
package ntorrent.gui.elements;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import ntorrent.settings.Constants;
import ntorrent.settings.Constants.Commands;

public class TrayIconPopUpMenu {
	PopupMenu popup = new PopupMenu();
	public TrayIconPopUpMenu() {
		String[] menu = {
				Commands.ADD_TORRENT.toString(),
				Commands.ADD_URL.toString(),
				Commands.START_ALL.toString(),
				Commands.STOP_ALL.toString(),
				Commands.QUIT.toString()
				
		};
		
		for(String mi : menu){
			MenuItem menuItem = new MenuItem(mi);
			menuItem.addActionListener(Constants.trayListener);
			popup.add(menuItem);
		}
	}
	
	public PopupMenu getPopup() {
		return popup;
	}
}
