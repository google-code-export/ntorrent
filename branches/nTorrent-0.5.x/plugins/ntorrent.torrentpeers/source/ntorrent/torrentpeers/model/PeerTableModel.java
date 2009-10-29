package ntorrent.torrentpeers.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * @author Henry Zhou
 *
 */
public class PeerTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -4946923125366469036L;
	private List<Peer> peers = new ArrayList<Peer>();

	@Override
	public int getColumnCount() {
		return PeerTableColumnModel.cols.length;
	}

	@Override
	public int getRowCount() {
		return peers.size();
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		try{
			return getValueAt(0,columnIndex).getClass();
		}catch(NullPointerException x){
			return Object.class;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try{
			Peer peer = peers.get(rowIndex);
			switch(columnIndex) {
				case 0:
					return peer.getAddress();
				case 1:
					return peer.getClientVersion();
				case 2:
					return peer.getDownRate();
				case 3:
					return peer.getUpRate();
				case 4:
					return peer.getDownTotal();
				case 5:
					return peer.getUpTotal();
				case 6:
					return peer.getPeerRate();
				case 7:
					return peer.getPeerTotal();
				default:
					return "";
			}
		} catch (ArrayIndexOutOfBoundsException x){
			
		}
		return null;
	}
	
	public void removeRow(int rowIndex){
		peers.remove(rowIndex);
	}
	
	public void addRow(Peer peer) {
		peers.add(peer);
	}
	
	public void clear() {
		peers.clear();
	}
	
	public void setValueAt(Peer peer, int rowIndex) {
		peers.add(rowIndex, peer);
	}
}
