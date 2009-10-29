/**
 * 
 */
package ntorrent.torrentpeers;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcClient;

import ntorrent.io.xmlrpc.XmlRpcConnection;
import ntorrent.locale.ResourcePool;
import ntorrent.session.ConnectionSession;
import ntorrent.session.SessionInstance;
import ntorrent.torrentpeers.model.Peer;
import ntorrent.torrentpeers.model.PeerTableModel;
import ntorrent.torrentpeers.view.PeerTable;
import ntorrent.torrenttable.TorrentTableInterface;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentSelectionListener;

/**
 * @author Henry Zhou
 *
 */
public class TorrentPeersInstance implements SessionInstance,
		TorrentSelectionListener, Runnable {
	private final TorrentTableInterface tc;
	private final JTabbedPane tab;
	
	private final PeerTableModel peerTableModel = new PeerTableModel();
	private final PeerTable peerTable = new PeerTable(peerTableModel);
	private final JScrollPane scrollpane = new JScrollPane(peerTable);
	
	//Xmlrpc constants.
	private final XmlRpcConnection connection;
	private final XmlRpcClient client;
	private String selectedTorrentHash = null;
	
	private boolean started = false;
	
	private Thread peerThread = new Thread(this);
	
	public TorrentPeersInstance(ConnectionSession session) {
		tc = session.getTorrentTableController();
		tab = session.getDisplay().getTabbedPane();
		connection = session.getConnection();
		client = connection.getClient();
	}

	@Override
	public boolean isStarted() {
		return started;
	}

	@Override
	public void start() {
		started = true;
		int preferredIndex = 7;
		if (preferredIndex > tab.getTabCount())
			preferredIndex = tab.getTabCount();

		tab.insertTab(ResourcePool.getString("tabname", "locale", this), null, scrollpane, null,preferredIndex);
		
		
		//add this as a listener
		tc.addTorrentSelectionListener(this);
		peerThread.start();
	}

	@Override
	public void stop() {
		started = false;
		//remove this as a listener
		tc.removeTorrentSelectionListener(this);
		tab.removeTabAt(tab.indexOfComponent(scrollpane));
		peerThread.interrupt();
	}

	@Override
	public void torrentsSelected(Torrent[] tor) {
		peerTableModel.clear();
		if (tor.length == 1) {
			Torrent torrent = tor[0];
			selectedTorrentHash = torrent.getHash();
		} else {
			selectedTorrentHash = null;
		}
		peerTableModel.fireTableDataChanged();
	}

	@Override
	public void run() {
		try {
			while(started) {
				if (selectedTorrentHash != null) {
					XmlRpcArray result = (XmlRpcArray) client.invoke("p.multicall",
							new Object[]{
							selectedTorrentHash,
							"", //dummy arg
							"p.get_address=",
							"p.get_client_version=", 
							"p.get_down_rate=", 
							"p.get_down_total=",
							"p.get_up_rate=",
							"p.get_up_total=",
							"p.get_peer_rate=",
							"p.get_peer_total="
						}
					);
					int rowsRecieved = result.size();
					int rowsPresent = peerTableModel.getRowCount();
					
					peerTableModel.clear();
					
					for(int x = 0 ; x < rowsRecieved; x++){
						XmlRpcArray row = (XmlRpcArray)result.get(x);
						Peer peer = new Peer();
						peer.setAddress(row.getString(0));
						peer.setClientVersion(row.getString(1));
						peer.setDownRate(row.getLong(2));
						peer.setDownTotal(row.getLong(3));
						peer.setUpRate(row.getLong(4));
						peer.setUpTotal(row.getLong(5));
						peer.setPeerRate(row.getLong(6));
						peer.setPeerTotal(row.getLong(7));
						
						peerTableModel.addRow(peer);
					}
					
					if(rowsRecieved > rowsPresent){
						peerTableModel.fireTableRowsInserted(rowsPresent, rowsRecieved-1);
					}else if(rowsRecieved < rowsPresent){
						peerTableModel.fireTableRowsDeleted(rowsRecieved, rowsPresent-1);
					}
					if(peerTableModel.getRowCount() > 0) {
						peerTableModel.fireTableRowsUpdated(0, rowsRecieved-1);
					}
				}
				
				Thread.sleep(2000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
