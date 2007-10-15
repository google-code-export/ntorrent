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

package org.heldig.ntorrent.gui.torrent;
import javax.swing.table.AbstractTableModel;

import org.heldig.ntorrent.language.Language;

/**
 * @author  Kim Eik
 */
public class TorrentJTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	Language[] columns = {
			Language.Torrent_Table_Col_name,
			Language.Torrent_Table_Col_size,
			Language.Torrent_Table_Col_downloaded,
			Language.Torrent_Table_Col_uploaded,
			Language.Torrent_Table_Col_seeders,
			Language.Torrent_Table_Col_leechers,
			Language.Torrent_Table_Col_download_rate,
			Language.Torrent_Table_Col_upload_rate,
			Language.Torrent_Table_Col_percent,
			Language.Torrent_Table_Col_ratio,
			Language.Torrent_Table_Col_priority
			};
	TorrentPool data = new TorrentPool(this);
		
	public TorrentPool getData() {
		return data;
	}
	
	public int getColumnCount() {
		if(data == null)
			return 0;
		return columns.length;
	}
	
    public String getColumnName(int col) {
        return columns[col].toString();
    }

	public int getRowCount() {
		if(data == null)
			return 0;
		return data.size();
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(data.size() > rowIndex){
			TorrentInfo row = data.get(rowIndex);
			switch(columnIndex){
				case 0: return row;
				case 1: return row.getByteSize();
				case 2: return row.getBytesDownloaded();
				case 3: return row.getBytesUploaded();
				case 4:	return ""+row.getSeeders()+" ("+row.getPeersTotal()+")";
				case 5: return ""+row.getLeechers();
				case 6: return row.getRateDown();
				case 7: return row.getRateUp();
				case 8: return row.getPercentFinished();
				case 9: return row.getRatio();
				case 10: return row.getPriority();
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
