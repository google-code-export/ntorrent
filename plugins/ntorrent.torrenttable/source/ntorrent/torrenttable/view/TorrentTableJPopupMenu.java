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
package ntorrent.torrenttable.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ntorrent.env.Environment;
import ntorrent.torrenttable.TorrentTableController;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentTableActionListener;

public class TorrentTableJPopupMenu extends JPopupMenu implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	Vector<TorrentTableActionListener> listeners = new Vector<TorrentTableActionListener>();

	private TorrentTable table;
	
	public TorrentTableJPopupMenu(TorrentTable table) {
		this.table = table;
		for(String s : TorrentTableController.mitems)
			add(Environment.getString(s),s);
	}

	public void add(String text, String actionCommand) {
		JMenuItem item = add(text);
		item.setActionCommand(actionCommand);
		item.addActionListener(this);
	}
	
	public void show(TorrentTable table, int x, int y) {
		if(!table.getSelectionModel().isSelectionEmpty())
			super.show(table, x, y);
	}
	
	public void addTorrentTableActionListener(TorrentTableActionListener listener){
		listeners.add(listener);
	}

	public void actionPerformed(ActionEvent e) {
		int[] rows = table.getSelectedRows();
		Torrent[] tor = new Torrent[rows.length];
		
		for(int x = 0; x < rows.length; x++)
			tor[x] = table.getModel().getRow(rows[x]);
		
		for(TorrentTableActionListener x : listeners)
			x.torrentActionPerformed(tor, e.getActionCommand());
	}
	
}
