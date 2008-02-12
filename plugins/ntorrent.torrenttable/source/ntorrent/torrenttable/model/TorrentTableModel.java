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
package ntorrent.torrenttable.model;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class TorrentTableModel extends AbstractTableModel  {
	private static final long serialVersionUID = 1L;

	private Vector<Torrent> torrents = new Vector<Torrent>();
	
	public final static String[] cols = {
		"torrenttable.name",
		"torrenttable.size",
		"torrenttable.down",
		"torrenttable.up",
		"torrenttable.seeders",
		"torrenttable.leechers",
		"torrenttable.downspeed",
		"torrenttable.upspeed",
		"torrenttable.eta",
		"torrenttable.percent",
		"torrenttable.ratio",
		"torrenttable.priority"
	};
	
	public int getColumnCount() {
		return cols.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return cols[column];
	}

	public int getRowCount() {
		return torrents.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Torrent tor = torrents.get(rowIndex);
		switch(columnIndex){
			case 0:
				return tor;
			case 1:
				return tor.getSizeBytes();
			case 2:
				return tor.getCompletedBytes();
			case 3:
				return tor.getUpTotal();
			case 4:
				return tor.getSeeders();
			case 5:
				return tor.getLeechers();
			case 6:
				return tor.getDownRate();
			case 7:
				return tor.getUpRate();
			case 8:
				return tor.getEta();
			case 9:
				return tor.getPercentDone();
			case 10:
				return tor.getRatio();
			case 11:
				return tor.getPriority();
		}
		return null;
	}
	
	public void setValueAt(Torrent tor, int rowIndex) {
		//fireTableRowsUpdated(rowIndex, rowIndex);
		if(torrents.contains(tor))
			torrents.remove(tor);
		torrents.add(rowIndex,tor);
	}
	
	public void removeRow(int rowIndex){
		torrents.remove(rowIndex);
	}
	
}
