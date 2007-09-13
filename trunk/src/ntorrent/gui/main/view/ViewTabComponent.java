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

package ntorrent.gui.main.view;

import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;

public class ViewTabComponent {
	JTabbedPane viewPane = new JTabbedPane();
	public ViewTabComponent(ChangeListener listener, Component content) {
		viewPane.setName("views");
		viewPane.addTab("main", new JScrollPane(content));
		viewPane.addTab("started", null);
		viewPane.addTab("stopped", null);
		viewPane.addTab("complete", null);
		viewPane.addTab("incomplete", null);
		viewPane.addTab("hashing", null);
		viewPane.addTab("seeding", null);
		viewPane.setMnemonicAt(0, KeyEvent.VK_1);
		viewPane.setMnemonicAt(1, KeyEvent.VK_2);
		viewPane.setMnemonicAt(2, KeyEvent.VK_3);
		viewPane.setMnemonicAt(3, KeyEvent.VK_4);
		viewPane.setMnemonicAt(4, KeyEvent.VK_5);
		viewPane.setMnemonicAt(5, KeyEvent.VK_6);
		viewPane.setMnemonicAt(6, KeyEvent.VK_7);
		viewPane.addChangeListener(listener);
	}
	
	public JTabbedPane getViewPane() {
		return viewPane;
	}
	
}
