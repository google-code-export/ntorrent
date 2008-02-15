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

import javax.swing.DefaultRowSorter;

import ntorrent.torrenttable.model.TorrentTableModel;

public class TorrentTableRowSorter extends DefaultRowSorter<TorrentTableModel, Integer> {
	
	final TorrentTableModel model;
	
	public TorrentTableRowSorter(final TorrentTableModel model) {
		super.setSortsOnUpdates(true);
		super.setModelWrapper(new ModelWrapper<TorrentTableModel, Integer>(){

			@Override
			public int getColumnCount() {
				return model.getColumnCount();
			}

			@Override
			public Integer getIdentifier(int row) {
				return row;
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
	public int convertRowIndexToView(int index) {
		return index;
	}
	
	@Override
	public int convertRowIndexToModel(int index) {
		return super.convertRowIndexToModel(index);
	}
	
	@Override
	public void rowsUpdated(int firstRow, int endRow) {
		try{
			super.rowsDeleted(firstRow, endRow);
		}catch(Exception x){
			
		}
	}
	
	@Override
	public void rowsDeleted(int firstRow, int endRow) {
		try{
			super.rowsDeleted(firstRow, endRow);
		}catch(Exception x){
			
		}
	}
	
	@Override
	public void rowsInserted(int firstRow, int endRow) {
		try{
			super.rowsDeleted(firstRow, endRow);
		}catch(Exception x){
			
		}
	}
	
	

}
