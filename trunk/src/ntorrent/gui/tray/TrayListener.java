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
package ntorrent.gui.tray;

import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;

import ntorrent.Controller;
import ntorrent.gui.core.SuperActionListener;

public class TrayListener extends SuperActionListener implements MouseListener {
	public void mouseClicked(MouseEvent e) {
		switch (e.getButton()){
		case MouseEvent.BUTTON1:
			Window window = Controller.getGui().getRootWin(); 
			if(!window.isFocused()){
				window.requestFocus();
				window.toFront();
			}
			break;
		case MouseEvent.BUTTON3:
			JPopupMenu popup = Controller.getTrayIcon().getPopup();
            popup.setLocation(e.getX(), e.getY());
            popup.setInvoker(popup);
            popup.setVisible(true);
			break;
		}		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
