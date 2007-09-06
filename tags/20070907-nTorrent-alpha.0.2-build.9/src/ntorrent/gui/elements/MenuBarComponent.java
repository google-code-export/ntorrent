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

import java.awt.Menu;
import java.awt.MenuBar;
import ntorrent.settings.Constants.Commands;
import ntorrent.gui.listener.FileMenuListener;
import ntorrent.gui.listener.HelpMenuListener;

public class MenuBarComponent{
	MenuBar menubar = new MenuBar();
	
	public MenuBarComponent(){
		Menu file = new Menu("File");
		Menu help = new Menu("Help");
		file.add(Commands.CONNECT.toString());
		file.addSeparator();
		file.add(Commands.ADD_TORRENT.toString());
		file.add(Commands.ADD_URL.toString());
		file.addSeparator();
		file.add(Commands.START_ALL.toString());
		file.add(Commands.STOP_ALL.toString());
		file.addSeparator();
		file.add(Commands.QUIT.toString());
		menubar.add(file);
		menubar.add(help);
		help.add(Commands.SETTINGS.toString());
		help.add(Commands.ABOUT.toString());
		file.addActionListener(new FileMenuListener());
		help.addActionListener(new HelpMenuListener());
	}
	
	public MenuBar getMenubar() {
		return menubar;
	}
}
