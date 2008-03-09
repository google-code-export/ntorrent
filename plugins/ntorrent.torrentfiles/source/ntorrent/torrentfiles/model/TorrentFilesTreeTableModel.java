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
package ntorrent.torrentfiles.model;

import javax.swing.tree.DefaultMutableTreeNode;

import ntorrent.locale.ResourcePool;
import ntorrent.torrenttable.model.Percent;
import ntorrent.torrenttable.model.Priority;

/**
 * @author Kim Eik
 *
 */
public class TorrentFilesTreeTableModel extends AbstractTreeTableModel implements TreeTableModel {


	String[] cols = {
			"priority",
			"size",
			"node",
			"percent",
			//"created", see TorrentFilesInstance for details
			//"open",
			"lasttouched",
	};
	
	Class[] classes = {
		Priority.class,
		Byte.class,
		TreeTableModel.class,
		Percent.class,
		//Boolean.class,
		//Boolean.class,
		String.class
	};
	
	public TorrentFilesTreeTableModel() {
		super(new TorrentFile());
	}
	
	public int getColumnCount() {
		return cols.length;
	}

	public String getColumnName(int column) {
		return ResourcePool.getString(cols[column], "locale", this);
	}
	
	@Override
	public Class getColumnClass(int column) {
		return classes[column];
	}

	public Object getValueAt(Object node, int column) {
		TorrentFile tf = (TorrentFile) node;
		switch(column){
			case 0:
				return tf.getPriority();
			case 2:
				return tf;
			case 3:
				return tf.getPercent();
			case 1:
				return tf.getSize();
			/*case 4:
				return tf.isCreated();
			case 5:
				return tf.isOpen();*/
			case 4:
				return tf.getLastTouched();
				
		}
		return null;
	}

	public Object getChild(Object parent, int index) {
		TorrentFile tf = (TorrentFile) parent;
		return tf.getChildAt(index);
	}

	public int getChildCount(Object parent) {
		TorrentFile tf = (TorrentFile) parent;
		return tf.getChildCount();
	}




}
