package ntorrent.torrentfiles;

import java.util.Date;
import java.util.Enumeration;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.tree.TreeNode;

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
import ntorrent.torrentfiles.model.TorrentFile;
import ntorrent.torrentfiles.model.TorrentFilesTreeTableModel;
import ntorrent.torrentfiles.model.TreeTableModelAdapter;
import ntorrent.torrentfiles.view.JTreeTable;
import ntorrent.torrenttable.TorrentTableInterface;
import ntorrent.torrenttable.model.Percent;
import ntorrent.torrenttable.model.Priority;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentSelectionListener;

/**
 * @author Kim Eik
 *
 */
public class TorrentFilesInstance implements TorrentSelectionListener {

	 private TorrentFilesTreeTableModel treeModel = new TorrentFilesTreeTableModel();
	final private JTreeTable treeTable = new JTreeTable(treeModel);
	final private JScrollPane scrollpane = new JScrollPane(treeTable);
	
	final private JTabbedPane container;
	final private TorrentTableInterface tableController;
	
	final private XmlRpcClient client;
	
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
			treeModel = new TorrentFilesTreeTableModel();
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
			
				//System.out.println(result);
				for(int row = 0 ; row < result.size(); row++){
				XmlRpcArray rowArray = (XmlRpcArray) result.get(row);
					XmlRpcArray paths = (XmlRpcArray) rowArray.get(0);
					TorrentFile parent = (TorrentFile) treeModel.getRoot();
					for(int x = 0; x < paths.size(); x++){
						String name = paths.getString(x);
						TorrentFile tf = getNode(name,x+1);
						if(tf == null){
							//System.out.println("make the node where parent="+parent);
							tf = new TorrentFile(name);
							//filter out directories.
							if(x+1 == paths.size()){
								//set the priority
								tf.setPriority(rowArray.getLong(1));
								//set the precent
								long complete = rowArray.getLong(2);
								long done = rowArray.getLong(3);
								tf.setPercent((int)(complete*100/done));
								//set size
								tf.setSize(rowArray.getLong(4));
								//set created and open
								tf.setCreated(rowArray.getLong(5) == 1 ? true : false);
								tf.setOpen(rowArray.getLong(6) == 1 ? true : false);
								//set last touched
								tf.setLastTouched(""+new Date(rowArray.getLong(7)/1000));
							}
						}
						
						//System.out.println("inserting "+tf+" into "+parent);
						if(!parent.isNodeChild(tf))
							parent.insert(tf, parent.getChildCount());
						parent = tf;
						
					}
				}

			} catch (XmlRpcException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlRpcFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			treeTable.setModel(treeModel);
			TreeTableModelAdapter model = (TreeTableModelAdapter)treeTable.getModel();
			model.fireTableDataChanged();
			treeTable.setWidths();
		}	
	}
	
	private TorrentFile getNode(String name, int depth){
		Enumeration<TreeNode> children = ((TreeNode)treeModel.getRoot()).children();
		while(children.hasMoreElements()){
			TorrentFile tf = (TorrentFile) children.nextElement();
			//System.out.println("name="+name+" tfname="+tf.getName()+" depth="+depth+" tdepth="+tf.getDepth());
			if(tf.getName().equals(name) && tf.getDepth() == depth)
				return tf;
		}
		return null;
	}
	
	public boolean isStarted() {
		return started;
	}

}
