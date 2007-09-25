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
package ntorrent.gui.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import ntorrent.Controller;
import ntorrent.gui.AboutGui;
import ntorrent.gui.SettingsGui;
import ntorrent.gui.dialogue.PromptEnv;
import ntorrent.gui.dialogue.PromptFile;
import ntorrent.gui.dialogue.PromptString;
import ntorrent.settings.Constants;

import org.apache.xmlrpc.XmlRpcException;

public abstract class SuperActionListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() != null)
		switch(Constants.Commands.getFromString(e.getActionCommand())){
			case ADD_TORRENT:
				PromptFile filePrompt = new PromptFile();
				if(filePrompt.getFile() != null){
					try {
						Controller.loadTorrent(filePrompt.getFile());
					} catch (IOException x) {
						Controller.writeToLog(x);
					} catch (XmlRpcException x) {
						Controller.writeToLog(x);
					}
				}				
				break;
			case ADD_URL:
				PromptString stringPrompt = new PromptString();
				if(stringPrompt.getInput() != null)
					Controller.loadTorrent(stringPrompt.getInput());				
				break;
			case CONNECT:
				PromptEnv env = new PromptEnv(Controller.getGui().getRootWin());
				env.setHost(Controller.getProfile().getHost());
				env.setUsername(Controller.getProfile().getUsername());
				env.drawWindow();				
				break;
			case QUIT:
				System.exit(0);				
				break;
			case START_ALL:
				Controller.writeToLog("Starting all torrents.");
				Controller.getTorrents().startAll();
				break;
			case STOP_ALL:
				Controller.writeToLog("Stopping all torrents.");
				Controller.getTorrents().stopAll();
				break;
			case ABOUT:
				AboutGui about = new AboutGui();
				about.drawWindow();
				break;
			case SETTINGS:
				new SettingsGui();
				break;				
		}

	}
}
