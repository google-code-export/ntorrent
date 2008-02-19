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

import ntorrent.env.Environment;

public class TorrentTableColumnModel extends DefaultTableColumnModel {
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
	
	public final static int[] widths = {
		300,
		50,
		50,
		50,
		20,
		20,
		50,
		50,
		50,
		50,
		50,
		50
	};
	
	public TorrentTableColumnModel() {
		for(int x = 0; x < cols.length; x++){
			TableColumn t = new TableColumn(x);
			t.setHeaderValue(Environment.getString(cols[x]));
			t.setIdentifier(cols[x]);
			t.setPreferredWidth(widths[x]);
			addColumn(t);
		}
	}
	
	
}
