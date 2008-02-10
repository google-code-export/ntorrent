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

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;


public class TorrentColumnModel extends DefaultTableColumnModel {
	private static final long serialVersionUID = 1L;
	
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
	
	public final static int[] colsWidth = {
		200,
		50,
		50,
		50,
		50,
		50,
		50,
		50,
		75,
		100,
		75,
		100
	};

	public TorrentColumnModel() {
		for(String col : cols){
			TableColumn c = new TableColumn();
			c.setHeaderValue(col);
			c.setIdentifier(col);
			addColumn(c);
		}
	}
	
	public void setColumnWidths(){
		for(int x = 0; x < colsWidth.length; x++){
			getColumn(x).setPreferredWidth(colsWidth[x]);
		}
	}
	

}
