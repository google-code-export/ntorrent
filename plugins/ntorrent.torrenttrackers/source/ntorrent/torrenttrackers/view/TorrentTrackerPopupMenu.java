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
package ntorrent.torrenttrackers.view;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ntorrent.locale.ResourcePool;

/**
 * @author Kim Eik
 *
 */
public class TorrentTrackerPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	
	public static final String[] MENU_ITEMS = {
		"enable",
		"disable"
	};
	
	public TorrentTrackerPopupMenu(ActionListener listener) {
		for(String s : MENU_ITEMS){
			JMenuItem item = new JMenuItem(ResourcePool.getString(s, this));
			item.setActionCommand(s);
			item.addActionListener(listener);
			add(item);
		}
	}

}
