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
package ntorrent.torrentlabels.model;

import javax.swing.RowFilter;

import ntorrent.torrentlabels.LabelController;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentTableModel;

/**
 * @author Kim Eik
 *
 */
public class TorrentTableFilter extends RowFilter<TorrentTableModel, Torrent> implements
		TorrentLabelFilterInterface {

	private String label = null;

	public void allLabel() {
		this.label = null;
	}

	public void noneLabel() {
		this.label = "";
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public boolean include(
			javax.swing.RowFilter.Entry<? extends TorrentTableModel, ? extends Torrent> entry) {
		if(label == null)
			return true;
		return entry.getIdentifier().getProperty(LabelController.PROPERTY).equals(label); 
	}

}
