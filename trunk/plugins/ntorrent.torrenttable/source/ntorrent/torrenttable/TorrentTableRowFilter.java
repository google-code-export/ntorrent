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
package ntorrent.torrenttable;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.DefaultRowSorter;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.RowSorter;

import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentTableModel;

@SuppressWarnings("unchecked")
public class TorrentTableRowFilter extends RowFilter<TorrentTableModel,Torrent> implements KeyListener {
	private final TorrentTableRowSorter sorter;
	
	Vector<RowFilter> extensions = new Vector<RowFilter>();
	String nameFilter = "";
	
	
	public TorrentTableRowFilter(TorrentTableRowSorter r) {
		sorter = r;
	}
	

	@Override
	public boolean include(RowFilter.Entry<? extends TorrentTableModel, ? extends Torrent> entry) {
		if(nameFilter == "")
			return true;
		
		for(RowFilter f : extensions)
			if(!f.include(entry))
				return false;
		
		String name = entry.getIdentifier().getName().toLowerCase();
		return name.contains(nameFilter.toLowerCase());
	}

	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {
		nameFilter = ((JTextField)e.getSource()).getText();
		updateFilter();
	}
	public void keyTyped(KeyEvent e) {

	}
	
	public void updateFilter(){
		sorter.setRowFilter(this);
	}


}
