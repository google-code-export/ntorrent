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
package ntorrent.torrenttable;

import java.util.Comparator;

import javax.swing.DefaultRowSorter;

import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentTableModel;

public class TorrentTableRowSorter extends DefaultRowSorter<TorrentTableModel, Torrent> {
	
	final TorrentTableModel model;
	
	public TorrentTableRowSorter(final TorrentTableModel model) {
		super.setSortsOnUpdates(true);
		super.setModelWrapper(new ModelWrapper<TorrentTableModel, Torrent>(){

			@Override
			public int getColumnCount() {
				return model.getColumnCount();
			}

			@Override
			public Torrent getIdentifier(int row) {
				return model.getRow(row);
			}

			@Override
			public TorrentTableModel getModel() {
				return model;
			}

			@Override
			public int getRowCount() {
				return model.getRowCount();
			}

			@Override
			public Object getValueAt(int row, int column) {
				return model.getValueAt(row, column);
			}
			
		});
		this.model = model;
	}
	
	@Override
	public Comparator<?> getComparator(int column) {
		if(model.getValueAt(0, column) instanceof Comparator)
			return (Comparator) model.getValueAt(0, column);
		return null;
	}
	
	@Override
	public int convertRowIndexToView(int index) {
		return index;
	}
	
	@Override
	public int convertRowIndexToModel(int index) {
		try {
			return super.convertRowIndexToModel(index);
		}catch (Exception x){
			//why are they throwing exceptions at me?
			return index;
		}
	}
	
	@Override
	public void rowsUpdated(int firstRow, int endRow) {
		//modelStructureChanged();
		/*try{
			super.rowsUpdated(firstRow, endRow);
		}catch(Exception x){
			System.out.println(x.getMessage());
		}*/
	}
	
	@Override
	public void rowsDeleted(int firstRow, int endRow) {
		modelStructureChanged();
		/*try{
			super.rowsDeleted(firstRow, endRow);
		}catch(Exception x){
			System.out.println(x.getMessage());
		}*/
	}
	
	@Override
	public void rowsInserted(int firstRow, int endRow) {
		modelStructureChanged();
		/*try{
			super.rowsInserted(firstRow, endRow);
		}catch(Exception x){
			System.out.println("inserted: "+x.getMessage());
		}*/
	}
	

}
