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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;


public abstract class AbstractPopupMenu extends SuperActionListener implements MouseListener{
	protected JPopupMenu popup;

	public AbstractPopupMenu(String[] menuItems) {
	    popup = new JPopupMenu();
	    for(String mitem : menuItems){
	    	if(mitem == null){
	    		popup.add(new JSeparator(SwingConstants.HORIZONTAL));
	    	} else{
		    	JMenuItem menuItem = new JMenuItem(mitem);
		    	menuItem.addActionListener(this);
		    	popup.add(menuItem);
	    	}
	    }
	}
	
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }


	public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }
	
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	protected abstract void maybeShowPopup(MouseEvent e);
}
