package ntorrent.torrentfiles;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import ntorrent.io.rtorrent.Download;
import ntorrent.io.rtorrent.File;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import ntorrent.locale.ResourcePool;
import ntorrent.session.ConnectionSession;
import ntorrent.session.SessionInstance;
import ntorrent.session.view.SessionFrame;
import ntorrent.torrentfiles.model.TorrentFile;
import ntorrent.torrentfiles.model.TorrentFilesTreeTableModel;
import ntorrent.torrentfiles.model.TreeTableModelAdapter;
import ntorrent.torrentfiles.view.JTreeTable;
import ntorrent.torrentfiles.view.TorrentFilesPopupMenu;
import ntorrent.torrenttable.TorrentTableInterface;
import ntorrent.torrenttable.model.Priority;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentSelectionListener;
import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcFault;

/**
 * @author Kim Eik
 *
 */
public class TorrentFilesInstance implements SessionInstance,TorrentSelectionListener, ActionListener {

	 private TorrentFilesTreeTableModel treeModel = new TorrentFilesTreeTableModel();
	final private JTreeTable treeTable = new JTreeTable(treeModel);
	final private JScrollPane scrollpane = new JScrollPane(treeTable);
	final private TorrentFilesPopupMenu popup = new TorrentFilesPopupMenu(this,treeTable);
	
	final private JTabbedPane container;
	final private TorrentTableInterface tc;
	
	final private XmlRpcClient client;
	final private File f;
	final private Download d;
	
	
	private boolean started;
	
	public TorrentFilesInstance(ConnectionSession session) {
		SessionFrame frame = session.getDisplay();
		
		//init needed variables
		container = frame.getTabbedPane();
		tc = session.getTorrentTableController();
		
		XmlRpcConnection connection = session.getConnection();
		client = connection.getClient();
		f = connection.getFileClient();
		d = connection.getDownloadClient();
		
		//add mouselistener for popup
		treeTable.addMouseListener(popup);
		
		//get and fire selection
		torrentsSelected(tc.getSelectedTorrents());
	}
	
	public void start(){
		started = true;
		int preferredIndex = 1;
		if (preferredIndex > container.getTabCount())
			preferredIndex = container.getTabCount();
		
		container.insertTab(ResourcePool.getString("tabname", "locale", this), null, scrollpane,null,preferredIndex);
		
		//add this as a selection listener
		tc.addTorrentSelectionListener(this);
	}
	
	public void stop(){
		started = false;
		int index = container.indexOfComponent(scrollpane);
		container.removeTabAt(index);
		
		//remove this as a selection listener
		tc.removeTorrentSelectionListener(this);
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
						/*"f.get_is_created=", this api was changed in 0.8.0, must therefore be removed until a more stable api is out.
						"f.get_is_open=",*/  
						"f.get_last_touched=",
						});
			
				//System.out.println(result);
				for(int row = 0 ; row < result.size(); row++){
					XmlRpcArray rowArray = (XmlRpcArray) result.get(row);
					XmlRpcArray paths = (XmlRpcArray) rowArray.get(0);
					TorrentFile root = (TorrentFile) treeModel.getRoot();
					TorrentFile tf = null;
					
					//System.out.println(paths);
					for(Object o : paths){
						String name = (String)o;
						tf = root.contains(name);
						if(tf != null){
							root = tf;
							//System.out.println("setting "+tf+" as new root");
						}else{
							tf = new TorrentFile(name,hash);
							root.insert(tf, root.getChildCount());
							//System.out.println("adding new child "+tf+" to leaf "+root);
							root = tf;
						}
					}
					
					//set the priority
					tf.setPriority(rowArray.getLong(1));
					//set the precent
					long complete = rowArray.getLong(2);
					long done = rowArray.getLong(3);
					tf.setPercent((int)(complete*100/done));
					//set size
					tf.setSize(rowArray.getLong(4));
					//set created and open
					//tf.setCreated(rowArray.getLong(5) == 1 ? true : false);
					//tf.setOpen(rowArray.getLong(6) == 1 ? true : false);
					//set last touched
					tf.setLastTouched(""+new Date(rowArray.getLong(5)/1000));
					//set offset
					tf.setOffset(row);
				}

			} catch (XmlRpcException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (XmlRpcFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			treeTable.setModel(treeModel);
			TreeTableModelAdapter model = (TreeTableModelAdapter)treeTable.getModel();
			model.fireTableDataChanged();
			treeTable.setWidths();
			treeTable.setVisible(true);
		}else{
			treeTable.setVisible(false);
		}
				
	}
	
	public boolean isStarted() {
		return started;
	}

	public void actionPerformed(ActionEvent e) {
		final String[] pri = TorrentFilesPopupMenu.priority;
		final String cmd = e.getActionCommand();
		
		new Thread(){
			final HashSet<TorrentFile> hashset = new HashSet<TorrentFile>();
			
			public void run(){
				for(int x = 0; x < pri.length; x++){
					if(pri[x].equals(cmd)){
						for(int row : treeTable.getSelectedRows()){
							TorrentFile tf = (TorrentFile)treeTable.getValueAt(row, 2);
							setPriority(tf, x);
						}
						
						String hash = null;
						for(TorrentFile tf : hashset){
							if(hash == null)
								hash = tf.getParentHash();
							//System.out.println(hash+" "+x+" "+tf.getOffset());
							f.set_priority(hash,tf.getOffset(), x);
							((Priority)treeModel.getValueAt(tf, 0)).setPriority(x);
						}
						
						d.update_priorities(hash);
						((TreeTableModelAdapter)treeTable.getModel()).fireTableDataChanged();
						
						break;
					}

				}
				
			}
			private final void setPriority(TorrentFile tf, int pri){
				if(!tf.isLeaf()){
					Enumeration<TorrentFile> children = tf.children();
					while(children.hasMoreElements()){
						setPriority(children.nextElement(), pri);
					}
				}else{
					hashset.add(tf);
				}
			}
		}.start();
	}
}
