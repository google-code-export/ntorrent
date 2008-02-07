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
package ntorrent.jpf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.java.plugin.Plugin;
import org.java.plugin.PluginManager;
import org.java.plugin.boot.ApplicationPlugin;
import org.java.plugin.registry.PluginDescriptor;

import ntorrent.env.Environment;
import ntorrent.gui.menubar.MainMenuBar;
import ntorrent.gui.menubar.MenuBarExtension;

public class PluginHandlerMenuBar extends Plugin implements MenuBarExtension,ActionListener {

	public void init(JMenuBar menuBar) {
		JMenu plugin = new JMenu(Environment.getString("plugin"));
		menuBar.add(plugin);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doStart() throws Exception {
		//
	}

	@Override
	protected void doStop() throws Exception {
		//
	}

}
