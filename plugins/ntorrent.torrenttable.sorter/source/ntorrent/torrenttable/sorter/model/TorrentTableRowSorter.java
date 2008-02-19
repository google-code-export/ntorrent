package ntorrent.torrenttable.sorter.model;


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
	
	@SuppressWarnings("unchecked")
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
		//allRowsChanged();
		//modelStructureChanged();
		sort();
		/*try{
			super.rowsUpdated(firstRow, endRow);
		}catch(Exception x){
			System.out.println(x.getMessage());
		}*/
	}
	
	@Override
	public void rowsDeleted(int firstRow, int endRow) {
		//allRowsChanged();
		//modelStructureChanged();
		sort();
		/*try{
			super.rowsDeleted(firstRow, endRow);
		}catch(Exception x){
			System.out.println(x.getMessage());
		}*/
	}
	
	@Override
	public void rowsInserted(int firstRow, int endRow) {
		//modelStructureChanged();
		sort();
		/*try{
			super.rowsInserted(firstRow, endRow);
		}catch(Exception x){
			x.printStackTrace();
			System.out.println("inserted: "+x.getMessage()+" "+firstRow+" "+endRow);
		}*/
	}
	

}
