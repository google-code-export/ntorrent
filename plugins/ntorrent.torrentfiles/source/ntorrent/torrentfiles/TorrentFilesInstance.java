package ntorrent.torrentfiles;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcFault;

import ntorrent.io.rtorrent.Download;
import ntorrent.io.rtorrent.File;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import ntorrent.locale.ResourcePool;
import ntorrent.session.ConnectionSession;
import ntorrent.session.view.SessionFrame;
import ntorrent.torrentfiles.model.TorrentFilesTreeTableModel;
import ntorrent.torrentfiles.view.JTreeTable;
import ntorrent.torrenttable.TorrentTableInterface;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentSelectionListener;

/**
 * @author Kim Eik
 *
 */
public class TorrentFilesInstance implements TorrentSelectionListener {

	final private TorrentFilesTreeTableModel treeModel = new TorrentFilesTreeTableModel();
	final private JTreeTable treeTable = new JTreeTable(treeModel);
	final private JScrollPane scrollpane = new JScrollPane(treeTable);
	
	final private JTabbedPane container;
	final private TorrentTableInterface tableController;
	
	final private XmlRpcClient client;
	//final private Download d;
	
	private boolean started;
	
	public TorrentFilesInstance(ConnectionSession session) {
		SessionFrame frame = session.getDisplay();
		
		//init needed variables
		container = frame.getTabbedPane();
		tableController = session.getTorrentTableController();
		
		XmlRpcConnection connection = session.getConnection();
		client = connection.getClient();
		//d = connection.getDownloadClient();
		
		//add this as a selection listener
		tableController.addTorrentSelectionListener(this);
	}
	
	public void start(){
		started = true;
		container.addTab(ResourcePool.getString("tabname", "locale", this), scrollpane);
	}
	
	public void stop(){
		started = false;
		int index = container.indexOfComponent(scrollpane);
		container.removeTabAt(index);
	}

	public void torrentsSelected(Torrent[] tor) {
		if(tor.length == 1){
			String hash = tor[0].getHash();
			try {
				XmlRpcArray result = (XmlRpcArray) client.invoke("f.multicall", 
						new Object[]{
						hash,
						"",
						"f.get_path_components=",
						"f.get_priority=",
						"f.get_completed_chunks=",
						"f.get_size_chunks=",
						"f.get_size_bytes=",
						"f.get_is_created=",
						"f.get_is_open=",
						"f.get_last_touched="
						});
			
				System.out.println(result);
			} catch (XmlRpcException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlRpcFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean isStarted() {
		return started;
	}

}
