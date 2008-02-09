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

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;

import ntorrent.gui.window.Window;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentTableModel;
import ntorrent.torrenttable.view.TorrentTableView;

public class TorrentTableController {
	public static void main(String[] args) {
		Window w = new Window();
		w.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		JTable t = new TorrentTableView();
		TorrentTableModel ttm = new TorrentTableModel();
		Torrent tor = new Torrent("08234i9pojkasmd");
		tor.setProperty("torrenttable.eta", "1");
		ttm.setValueAt(tor, 0);
		ttm.setValueAt(new Torrent("08234i9pojk234asmd"), 0);
		
		t.setModel(ttm);
		
		w.setContentPane(new JScrollPane(t));
		w.drawWindow();
		t.validate();
		t.repaint();
		
	}
}
