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

package org.heldig.ntorrent.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JMenuBar;

import org.heldig.ntorrent.gui.core.MenuImplementation;
import org.heldig.ntorrent.language.Language;


/**
 * @author	Kim Eik
 */
public class MenuBarComponent extends JMenuBar implements ActionListener{
	private static final long serialVersionUID = 1L;
	private Vector<ActionListener> al = new Vector<ActionListener>();

	final static Object[] file_menu = {
			Language.Menu_file.toString(),
			Language.Menu_File_add_torrent,
			Language.Menu_File_add_url,
			null,
			Language.Menu_File_start_all,
			Language.Menu_File_stop_all,
			null,
			Language.Menu_File_quit,
	};
	
	final static Object[] help_menu = {
			Language.Menu_help.toString(),
			Language.Menu_Help_settings,
			Language.Menu_Help_about	
	};
	
	final static Object[] menu_items = {
			file_menu,
			help_menu,
	};
	
	public MenuBarComponent(){
		MenuImplementation.createMenuItems(this, menu_items,this);
	}

	public void addActionListener(ActionListener e){
		al.add(e);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		for(ActionListener a : al)
			a.actionPerformed(e);
	}
	
	/*@Override
	public void actionPerformed(ActionEvent e) {
		switch (Language.getFromString(e.getActionCommand())){
		case Menu_File_add_torrent:
			event.promptFile();
			break;
		case Menu_File_add_url:
			event.promptUrl();
			break;
		case Menu_File_start_all:
			break;
		case Menu_File_stop_all:
			break;
		case Menu_File_quit:
			System.exit(0);
			break;
		case Menu_Help_settings:
			event.showSettings();
			break;
		case Menu_Help_about:
			event.showAbout();
			break;
		}
	}*/
}
