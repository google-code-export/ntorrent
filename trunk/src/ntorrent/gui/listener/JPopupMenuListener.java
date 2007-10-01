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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import ntorrent.Controller;


public abstract class JPopupMenuListener extends GuiEventListener implements MouseListener{
	protected JPopupMenu popup = new JPopupMenu();
	
	public JPopupMenuListener(Controller c,Object[] menuItems) {
		super(c);
	    createMenuItems(popup,menuItems);

	}
	
	private void createMenuItems(JComponent target,Object[] menuItems){
	    for(Object mitem : menuItems){
	    	if(mitem instanceof Object[]){
	    		
	    		target.add(createMenu((Object[])mitem));
	    	}else if(mitem == null){
	    		target.add(new JSeparator(SwingConstants.HORIZONTAL));
	    	} else if(mitem instanceof String) {
		    	JMenuItem menuItem = new JMenuItem((String)mitem);
		    	menuItem.addActionListener(this);
		    	target.add(menuItem);
	    	}
	    }
	    
	}
	
	private JMenu createMenu(Object[] objects) {
		JMenu submenu = new JMenu((String)objects[0]);
		Object[] menuitems = new Object[objects.length-1];
		for(int x = 1; x < objects.length; x++)
			menuitems[x-1] = objects[x];
		createMenuItems(submenu, menuitems);
		return submenu;		
	}

	public void mousePressed(MouseEvent e) {
		if(e.isPopupTrigger())
			maybeShowPopup(e);
		
	}
	public void mouseReleased(MouseEvent e) {
		if(e.isPopupTrigger())
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
