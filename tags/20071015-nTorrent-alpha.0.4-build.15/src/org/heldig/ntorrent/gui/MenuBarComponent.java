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

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JSeparator;

import org.heldig.ntorrent.language.Language;


/**
 * @author	Kim Eik
 */
public class MenuBarComponent extends JMenuBar implements ActionListener{
	private static final long serialVersionUID = 1L;
	private Vector<ActionListener> al = new Vector<ActionListener>();

	private final static JMenu fileMenu = new JMenu(Language.Menu_file);
	private final static JMenu helpMenu = new JMenu(Language.Menu_help);
	
	public MenuBarComponent(){
		fileMenu.add(Language.Menu_File_add_torrent).addActionListener(this);
		fileMenu.add(Language.Menu_File_add_url).addActionListener(this);
		fileMenu.add(new JSeparator());
		fileMenu.add(Language.Menu_File_start_all).addActionListener(this);
		fileMenu.add(Language.Menu_File_stop_all).addActionListener(this);
		fileMenu.add(new JSeparator());
		fileMenu.add(Language.Menu_File_quit).addActionListener(this);
		
		helpMenu.add(Language.Menu_Help_settings).addActionListener(this);
		helpMenu.add(Language.Menu_Help_about).addActionListener(this);
		add(fileMenu);
		add(helpMenu);
	}

	public void addActionListener(ActionListener e){
		al.add(e);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		for(ActionListener a : al)
			a.actionPerformed(e);
	}
}
