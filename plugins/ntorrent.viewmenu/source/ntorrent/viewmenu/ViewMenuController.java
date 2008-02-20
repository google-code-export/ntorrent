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
package ntorrent.viewmenu;

import java.util.Vector;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ntorrent.torrenttable.TorrentTableController;
import ntorrent.viewmenu.model.View;
import ntorrent.viewmenu.model.ViewListModel;
import ntorrent.viewmenu.view.ViewList;

public class ViewMenuController implements ListSelectionListener {
	
	ViewListModel model = new ViewListModel();
	ViewList list = new ViewList(model,this);
	Vector<ViewChangeListener> listener = new Vector<ViewChangeListener>();
	
	public ViewMenuController(ViewChangeListener listener) {
		this.listener.add(listener);
	}

	public JList getDisplay(){
		return list;
	}

	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting()){
			int index = list.getSelectedIndex();
			
			for(ViewChangeListener l : listener)
				l.viewChanged(((View)model.getElementAt(index)).getView());
		}
	}
}
