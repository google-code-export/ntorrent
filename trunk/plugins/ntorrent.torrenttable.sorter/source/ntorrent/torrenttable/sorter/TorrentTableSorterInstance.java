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
package ntorrent.torrenttable.sorter;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import ntorrent.session.ConnectionSession;
import ntorrent.session.SessionInstance;
import ntorrent.torrenttable.SelectionValueInterface;
import ntorrent.torrenttable.TorrentTableInterface;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.sorter.model.TorrentRowFilter;
import ntorrent.torrenttable.sorter.view.TorrentTableFinder;
import ntorrent.torrenttable.view.TorrentTable;

/**
 * @author Kim Eik
 *
 */
public class TorrentTableSorterInstance implements SessionInstance {
	private final TorrentTableInterface tc;
	private final TorrentTable table;
	private final JPanel panel;
	
	private final SelectionValueInterface selectionMethod = new SelectionValueInterface(){

		public Torrent getTorrentFromView(int index) {
			return table.getModel().getRow(sorter.convertRowIndexToModel(index));
		}
		
		
	};
	
	private final TorrentRowSorter sorter;
	private final TorrentRowFilter filter;
	private final TorrentTableFinder finder;
	private boolean started = false;
	
	public TorrentTableSorterInstance(ConnectionSession session) {
		tc = session.getTorrentTableController();
		table = tc.getTable();
		panel = table.getDisplay();
		
		sorter = new TorrentRowSorter(table.getModel());
		sorter.setSortsOnUpdates(true);
		filter = new TorrentRowFilter(sorter);
		finder = new TorrentTableFinder(filter);
	}
	
	public void start(){
		tc.setSelectionMethod(selectionMethod);
		table.setRowSorter(sorter);
		panel.add(finder,BorderLayout.NORTH);
		panel.revalidate();
		panel.repaint();
	}
	
	public void stop(){
		tc.setSelectionMethod(null);
		table.setRowSorter(null);
		panel.remove(finder);
		panel.revalidate();
		panel.repaint();
	}
	
	public boolean isStarted(){
		return started;
	}

}
