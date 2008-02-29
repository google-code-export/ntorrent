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

import ntorrent.env.Environment;

public class TorrentTableModel extends AbstractTableModel  {
	private static final long serialVersionUID = 1L;

	private Vector<Torrent> torrents = new Vector<Torrent>();
	
	public int getColumnCount() {
		return TorrentTableColumnModel.cols.length;
	}

	public int getRowCount() {
		return torrents.size();
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		try{
			return getValueAt(0,columnIndex).getClass();
		}catch(NullPointerException x){
			return Object.class;
		}
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		try{
			
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
			
		}catch(ArrayIndexOutOfBoundsException x){
			//do nothing, will return null.
		}
		return null;
	}
	
	public void setValueAt(Torrent tor, int rowIndex) {
		//System.out.println("updating "+tor);
		
		torrents.set(rowIndex,tor);
		//fireTableRowsUpdated(rowIndex, rowIndex);
	}
	
	public void removeRow(int rowIndex){
		torrents.remove(rowIndex);
		//fireTableRowsDeleted(rowIndex, rowIndex);
	}

	public void addRow(Torrent tor) {
		//System.out.println("adding "+tor);
		//int row = torrents.size();
		torrents.addElement(tor);
		//fireTableRowsInserted(row, row);
	}
	
	public Torrent getRow(Integer row){
		return torrents.get(row);
	}

	public void clear() {
		torrents.clear();
	}
	
}
