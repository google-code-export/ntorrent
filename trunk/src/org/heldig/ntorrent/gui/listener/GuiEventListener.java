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
/*
package org.heldig.ntorrent.gui.listener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.heldig.ntorrent.Controller;
import org.heldig.ntorrent.gui.dialogue.PromptProfile;
import org.heldig.ntorrent.gui.dialogue.PromptString;
import org.heldig.ntorrent.gui.window.AboutGui;
import org.heldig.ntorrent.gui.window.SettingsGui;
import org.heldig.ntorrent.language.Language;
import org.heldig.ntorrent.settings.Constants.Commands;



/**
 * @author  Kim Eik
 */
//public class GuiEventListener implements ChangeListener, ActionListener{
	//private static final long serialVersionUID = 1L;
	//protected Controller C;
	/*public GuiEventListener(Controller c) {
		C = c;
	}*/

	//public void stateChanged(ChangeEvent e) {
		//System.out.println(e.getSource());
		/*JTabbedPane pane = (JTabbedPane)e.getSource();
		if(pane.getName().equals("views"))
			C.MC.getTorrentPool().setView(pane.getTitleAt(pane.getSelectedIndex()));*/
/*	}

	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		/*switch(Commands.getFromString(e.getActionCommand())){
		case ABOUT:
			AboutGui about = new AboutGui();
			about.drawWindow();
			break;
		case ADD_TORRENT:
			JFileChooser filePrompt = new JFileChooser();
			filePrompt.showOpenDialog(C.GC.getRootWin());
			if(filePrompt.getSelectedFile() != null){
				try {
					C.IO.loadTorrent(filePrompt.getSelectedFile());
				} catch (Exception x) {
					C.GC.showError(x);
				}
			}	
			break;
		case ADD_URL:
			PromptString stringPrompt = new PromptString(C.GC.getRootWin());
			if(stringPrompt.getInput() != null)
				C.IO.loadTorrent(stringPrompt.getInput());
			break;
		case CONNECT:
			new PromptProfile(C.GC.getRootWin(),C);
			break;
		case QUIT:
			System.exit(0);	
			break;
		case SETTINGS:
			new SettingsGui();
			break;
		case START_ALL:
			System.out.println("Starting all torrents.");
			C.MC.getTorrentPool().startAll();
			break;
		case STOP_ALL:
			System.out.println("Stopping all torrents.");
			C.MC.getTorrentPool().stopAll();
			break;
		}*/
/*	}

}*/