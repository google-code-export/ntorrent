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
package ntorrent.gui.elements;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;


public abstract class AbstractPopupMenu extends MouseAdapter implements ActionListener{
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
	
	protected void maybeShowPopup(MouseEvent e) {
		System.out.println(e);
	}
}
