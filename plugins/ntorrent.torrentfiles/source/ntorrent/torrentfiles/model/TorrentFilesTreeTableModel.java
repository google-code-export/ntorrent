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

import ntorrent.torrenttable.model.Percent;
import ntorrent.torrenttable.model.Priority;

/**
 * @author Kim Eik
 *
 */
public class TorrentFilesTreeTableModel extends AbstractTreeTableModel implements TreeTableModel {


	String[] cols = {
			"priority",
			"node",
			"percent",
			"size",
			"created",
			"open",
			"lasttouched",
	};
	
	Class[] classes = {
		Priority.class,
		TreeTableModel.class,
		Percent.class,
		Byte.class,
		Boolean.class,
		Boolean.class,
		String.class
	};
	
	public TorrentFilesTreeTableModel() {
		super(new TorrentFile("/"));
		TorrentFile tf = new TorrentFile("filename");
		tf.setPercent(new Percent(50));
		tf.setPriority(new Priority(2));
		((TorrentFile)getRoot()).insert(tf, 0);
	}
	
	public int getColumnCount() {
		return cols.length;
	}

	public String getColumnName(int column) {
		return cols[column];
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
			case 1:
				return tf;
			case 2:
				return tf.getPercent();
			case 3:
			case 4:
			case 5:
			case 6:
				
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
