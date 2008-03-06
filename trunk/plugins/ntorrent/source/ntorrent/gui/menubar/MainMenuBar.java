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
package ntorrent.gui.menubar;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import ntorrent.jpf.PluginHandlerMenuBar;
import ntorrent.locale.ResourcePool;

public class MainMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	private static final String bundle = "locale";
	
	public final static String[] identifiers = {
		"filemenu.addtorrent",
		"filemenu.addurl",
		"filemenu.connect",
		"filemenu.quit",
		"helpmenu.settings",
		"helpmenu.about"
	};
		
	public MainMenuBar(ActionListener listener) {
		JMenu file = new JMenu(ResourcePool.getString("filemenu", bundle, this));
		JMenu help = new JMenu(ResourcePool.getString("helpmenu", bundle, this));
		
		JMenuItem[] items = new JMenuItem[identifiers.length];
		for(int x = 0; x < items.length; x++){
			String id = identifiers[x];
			JMenuItem item = new JMenuItem(ResourcePool.getString(id, bundle, this));
			item.setActionCommand(id);
			item.addActionListener(listener);
			items[x] = item;
		}
		
		int x = 0;
		file.add(items[x++]);
		file.add(items[x++]);
		file.add(new JSeparator());
		file.add(items[x++]);
		file.add(new JSeparator());
		file.add(items[x++]);
		help.add(items[x++]);
		help.add(items[x++]);
		add(file);
		add(help);
		
		new PluginHandlerMenuBar(this);
	}
}
