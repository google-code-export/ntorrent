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

package ntorrent.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import org.apache.xmlrpc.XmlRpcException;

import ntorrent.controller.Controller;
import ntorrent.gui.dialogues.PromptFile;
import ntorrent.gui.dialogues.PromptString;

public class FileMenuListener implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		if(s.equalsIgnoreCase("add torrent")){
			PromptFile filePrompt = new PromptFile();
			if(filePrompt.getFile() != null){
				try {
					Controller.getRpc().loadTorrent(filePrompt.getFile());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (XmlRpcException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("add torrent");
			}
		}else if(s.equalsIgnoreCase("add url")){
			PromptString stringPrompt = new PromptString();
			try {
				Controller.getRpc().loadTorrent(stringPrompt.getInput());
			} catch (XmlRpcException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(s.equalsIgnoreCase("quit")){
			System.out.println("QUIT!");
			System.exit(0);
		}

	}

}
