package ntorrent.torrenttable.sorter;

import javax.swing.table.TableRowSorter;

import ntorrent.torrenttable.model.TorrentTableModel;

/**
 * @author Kim Eik
 *
 */
public class TorrentRowSorter extends TableRowSorter<TorrentTableModel> {
	public TorrentRowSorter(TorrentTableModel model) {
		super(model);
	}

	@Override
	public void rowsInserted(int firstRow, int endRow) {
		try{
			super.rowsInserted(firstRow, endRow);
		} catch(Exception x){
			x.printStackTrace();
		}
	}
	
	@Override
	public void rowsDeleted(int firstRow, int endRow) {
		try{
			super.rowsDeleted(firstRow, endRow);
		} catch(Exception x){
			x.printStackTrace();
		}
	}
	
	@Override
	public void rowsUpdated(int firstRow, int endRow) {
		try{
			super.rowsUpdated(firstRow, endRow);
		} catch(Exception x){
			x.printStackTrace();
		}
	}
}
