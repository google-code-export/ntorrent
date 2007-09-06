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

package ntorrent.model;
import javax.swing.table.AbstractTableModel;

import ntorrent.controller.Controller;

public class TorrentTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	//Vector<String> columns = new Vector<String>();
	String[] columns = {
			"Name",
			"Size",
			"Downloaded",
			"Uploaded",
			"DLR",
			"ULR",
			"%",
			"Ratio"
			};
	TorrentPool data = new TorrentPool();
	
	public void fillData(TorrentPool torrents){
		data = torrents;
		Controller.writeToLog("Created JTable");
	}
	
	public int getColumnCount() {
		// TODO Auto-generated method stub
		//return columns.size();
		return columns.length;
	}
	
    public String getColumnName(int col) {
        //return columns.get(col);
        return columns[col];
    }

	public int getRowCount() {
		return data.size();
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(data.size() > rowIndex){
			TorrentFile row = data.get(rowIndex);
			switch(columnIndex){
				case 0: return row;
				case 1: return row.getByteSize();
				case 2: return row.getBytesDownloaded();
				case 3: return row.getBytesUploaded();
				case 4: return row.getRateDown();
				case 5: return row.getRateUp();
				case 6: return row.getPercentFinished();
				case 7: return row.getRatio();
				default: return "";
			}
		}
		return "";
	}
	
	@Override
	public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

}
