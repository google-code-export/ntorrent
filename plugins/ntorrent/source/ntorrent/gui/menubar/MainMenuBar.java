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

public class MainMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	public MainMenuBar(ActionListener listener) {
		JMenu file = new JMenu(new GuiAction("filemenu",listener));
		JMenu help = new JMenu(new GuiAction("helpmenu",listener));
		file.add(new JMenuItem(new GuiAction("filemenu.addtorrent",listener)));
		file.add(new JMenuItem(new GuiAction("filemenu.addurl",listener)));
		file.add(new JSeparator());
		file.add(new JMenuItem(new GuiAction("filemenu.startall",listener)));
		file.add(new JMenuItem(new GuiAction("filemenu.stopall",listener)));
		file.add(new JSeparator());
		file.add(new JMenuItem(new GuiAction("filemenu.connect",listener)));
		file.add(new JSeparator());
		file.add(new JMenuItem(new GuiAction("filemenu.quit",listener)));
		help.add(new JMenuItem(new GuiAction("helpmenu.settings",listener)));
		help.add(new JMenuItem(new GuiAction("helpmenu.about",listener)));
		add(file);
		add(help);
		file.addActionListener(listener);
		help.addActionListener(listener);
	}
}
