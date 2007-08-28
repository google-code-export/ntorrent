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

package ntorrent.gui.dialogues;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import ntorrent.controller.Controller;

public class PromptAbout {
	JFrame window = new JFrame();
	JTextArea about = new JTextArea();

	public PromptAbout(){
		about.setText(
				"nTorrent is licensed under GPLv3\n" +
				"Author: Kim Eik\n\n" +
				"http://ntorrent-java.sf.net"
				);
		about.setEditable(false);
		about.setFocusable(false);
		window.setVisible(true);
		window.add(about);
		window.setLocationRelativeTo(Controller.getGui().getRootWin());
	}
	
	public void drawWindow(){
		window.validate();
		window.pack();
	}
}
