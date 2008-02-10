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

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import ntorrent.env.Environment;

public class TorrentTableModel implements TableModel {

	Vector<TableModelListener> listener = new Vector<TableModelListener>();
	Vector<Torrent> torrents = new Vector<Torrent>();
	
	public void addTableModelListener(TableModelListener l) {
		listener.add(l);
	}

	public Class<?> getColumnClass(int columnIndex) {
		try{
			return getValueAt(0, columnIndex).getClass();
		} catch(NullPointerException x){
			return String.class;
		}
	}

	public int getColumnCount() {
		return TorrentColumnModel.cols.length;
	}

	public String getColumnName(int columnIndex) {
		return TorrentColumnModel.cols[columnIndex];
	}

	public int getRowCount() {
		return torrents.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		System.out.println(rowIndex+","+columnIndex);
		return torrents.get(rowIndex).getProperty(getColKey(columnIndex));
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void removeTableModelListener(TableModelListener l) {
		listener.remove(l);
	}

	/**
	 * Doesnt do anything.
	 */
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		//dont do anything
	}
	
	public void setValueAt(Torrent value, int rowIndex) {
		torrents.add(rowIndex, value);
		fireRowChanged(rowIndex);
	}
	
	public void fireRowChanged(int rowIndex){
		for(TableModelListener l : listener){
			l.tableChanged(new TableModelEvent(this,rowIndex));
		}
	}
	
	private String getColKey(int columnIndex){
		return TorrentColumnModel.cols[columnIndex];
	}
}
