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
	
	public MainMenuBar(ActionListener listener) {
		JMenu file = new JMenu(ResourcePool.getString("filemenu", bundle, this));
		JMenu help = new JMenu(ResourcePool.getString("helpmenu", bundle, this));
		file.add(new JMenuItem(ResourcePool.getString("filemenu.addtorrent", bundle, this))).addActionListener(listener);
		file.add(new JMenuItem(ResourcePool.getString("filemenu.addurl", bundle, this))).addActionListener(listener);
		file.add(new JSeparator());
		file.add(new JMenuItem(ResourcePool.getString("filemenu.startall", bundle, this))).addActionListener(listener);
		file.add(new JMenuItem(ResourcePool.getString("filemenu.stopall", bundle, this))).addActionListener(listener);
		file.add(new JSeparator());
		file.add(new JMenuItem(ResourcePool.getString("filemenu.connect", bundle, this))).addActionListener(listener);
		file.add(new JSeparator());
		file.add(new JMenuItem(ResourcePool.getString("filemenu.quit", bundle, this))).addActionListener(listener);
		help.add(new JMenuItem(ResourcePool.getString("helpmenu.settings", bundle, this))).addActionListener(listener);
		help.add(new JMenuItem(ResourcePool.getString("helpmenu.about", bundle, this))).addActionListener(listener);
		add(file);
		add(help);
		file.addActionListener(listener);
		help.addActionListener(listener);
		new PluginHandlerMenuBar(this);
	}
}
