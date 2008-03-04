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
package ntorrent.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

import ntorrent.Main;
import ntorrent.locale.ResourcePool;

/**
 * @author Kim Eik
 *
 */
public class ConnectionTab extends JTabbedPane implements MouseListener, ActionListener {
	private final static JPopupMenu popup = new JPopupMenu();
	public final static String[] actions = {
		"tab.new",
		"tab.close"
	};

	public ConnectionTab(int placement) {
		super(placement);
		
		addMouseListener(this);
		
		for(String s : actions){
			JMenuItem item = new JMenuItem(ResourcePool.getString(s, "locale", this));
			item.setActionCommand(s);
			item.addActionListener(this);
			popup.add(item);
		}
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) {
		if(e.isPopupTrigger())
			popup.show(this,e.getX(),e.getY());
	}
	public void mouseReleased(MouseEvent e) {
		if(e.isPopupTrigger())
			popup.show(this,e.getX(),e.getY());
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if(action.equals(actions[0])){
			Main.newSession();
		}else if(action.equals(actions[1])){
			/**
			 * This only removes the contents of the tab,
			 * not the processes behind it!!!
			 */
			removeTabAt(getSelectedIndex());
		}
	}
}
