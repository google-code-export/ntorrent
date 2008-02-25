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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.UIManager;
import javax.swing.WindowConstants;

import ntorrent.env.Environment;
import ntorrent.gui.window.Window;
import ntorrent.io.rtorrent.Download;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import ntorrent.profile.model.LocalProfileModel;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentTableActionListener;
import ntorrent.torrenttable.model.TorrentTableModel;
import ntorrent.torrenttable.view.TorrentTable;
import ntorrent.torrenttable.view.TorrentTableJPopupMenu;
import ntorrent.viewmenu.ViewChangeListener;

import org.java.plugin.Plugin;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.PluginManager.EventListener;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;

import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcFault;

public class TorrentTableController implements Runnable, ViewChangeListener, EventListener, TorrentTableActionListener{
	
	private final TorrentTableModel ttm = new TorrentTableModel();
	private final TorrentTable table = new TorrentTable(ttm);
	private final Map<String,Torrent> torrents = new HashMap<String,Torrent>();
	private final XmlRpcConnection connection;
	private final Thread controllerTread = new Thread(this);
	
	private final PluginManager pluginManager;
	
	private final Vector<String> extensions = new Vector<String>();
	
	private final static String extensionPointPluginId = "ntorrent.torrenttable";
	private final static String extensionPointId = "TorrentTableSorter";

	
    private final Object[] download_variable = {
                    "", //reserved for view arg
                    "d.get_hash=",  //ID
                    "d.get_name=", //constant
                    "d.get_state=",         //variable
                    "d.get_completed_bytes=", //variable
                    "d.get_up_total=",      //variable
                    "d.get_peers_complete=",
                    "d.get_peers_accounted=",
                    "d.get_down_rate=", //variable
                    "d.get_up_rate=", //variable
                    "d.get_message=", //relative
                    "d.get_priority=", //relative
                    "d.get_size_bytes="
    };

	
	public TorrentTableController(XmlRpcConnection connection) {
		this.connection = connection;
		controllerTread.start();
		pluginManager = Environment.getPluginManager();
		pluginManager.registerListener(this);
		initExtensions();
		table.getTablePopup().addTorrentTableActionListener(this);
	}
	
	public TorrentTable getTable() {
		return table;
	}
	
	public void run() {
	   XmlRpcClient client = connection.getClient();
		try {
			while(true){
				XmlRpcArray download_list = (XmlRpcArray)client.invoke("d.multicall", download_variable);
				
				int rowsRecieved = download_list.size();
				int rowsPresent = ttm.getRowCount();
				
				for(int x = rowsPresent-1; x > rowsRecieved-1; x--){
					ttm.removeRow(x);
					//System.out.println("removing row: "+x);
				}
				
				for(int x = 0 ; x < rowsRecieved; x++){
					XmlRpcArray data = (XmlRpcArray)download_list.get(x);
					Torrent tor = new Torrent(data.getString(0));
					
					if(!torrents.keySet().contains(tor.getHash()))
						torrents.put(tor.getHash(),tor);
					else
						tor = torrents.get(data.getString(0));
					
					tor.setName(data.getString(1));
					tor.setStarted(data.getLong(2) == 1);
					tor.setCompletedBytes(data.getLong(3));
					tor.setUpTotal(data.getLong(4));
					tor.setPeersComplete(data.getLong(5));
					tor.setPeersAccounted(data.getLong(6));
					tor.setDownRate(data.getLong(7));
					tor.setUpRate(data.getLong(8));
					tor.setMessage(data.getString(9));
					tor.setPriority(data.getLong(10));
					tor.setSizeBytes(data.getLong(11));
					
					if(rowsPresent > x){
						//System.out.println("updating row: "+x);
						ttm.setValueAt(tor, x);
					}else{
						//System.out.println("adding row: "+x);
						ttm.addRow(tor);
					}
				}
				
				if(rowsRecieved > rowsPresent){
					//System.out.println("inserted rows,"+rowsPresent+","+(rowsRecieved-1));
					ttm.fireTableRowsInserted(rowsPresent, rowsRecieved-1);
				}else if(rowsRecieved < rowsPresent){
					//System.out.println("deleted rows,"+rowsRecieved+","+(rowsPresent-1));
					ttm.fireTableRowsDeleted(rowsRecieved, rowsPresent-1);
				}
				//System.out.println("updated rows,"+0+","+(rowsRecieved-1));
				ttm.fireTableRowsUpdated(0, rowsRecieved-1);
				
				
				
				try {
					Thread.sleep(500);
					//ttm.removeRow(ttm.getRowCount()-1);
					//Thread.sleep(1000);
					//System.out.println(table.getSelectedRow());
				} catch (InterruptedException e) {
					
				}
			}
			
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlRpcFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		XmlRpcConnection c = new XmlRpcConnection(new LocalProfileModel());
		TorrentTableController t = new TorrentTableController(c);
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		Window w = new Window();
		w.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		w.setContentPane(t.getTable().getDisplay());
		w.drawWindow();		
		
	}


	public void viewChanged(String view) {
		ttm.clear();
		ttm.fireTableDataChanged();
		download_variable[0] = view;
		controllerTread.interrupt();
	}

	private void initExtensions(){
		ExtensionPoint ext = pluginManager.getRegistry().getExtensionPoint(extensionPointPluginId,extensionPointId);
		for(Extension e : ext.getAvailableExtensions()){
			extensions.add(e.getDeclaringPluginDescriptor().getId());
			initExtension(e.getDeclaringPluginDescriptor());
		}
	}
	
	private void initExtension(PluginDescriptor p){
		try{
			if(pluginManager.isPluginActivated(p)){
				Plugin plugin = pluginManager.getPlugin(p.getId());
				((TorrentTableExtension)plugin).init(this);
			}
		}catch(PluginLifecycleException x){
			x.printStackTrace();
		}
	}
	
	public void pluginActivated(Plugin plugin) {
		if(extensions.contains(plugin.getDescriptor().getId())){
			initExtension(plugin.getDescriptor());
		}
		
	}

	public void pluginDeactivated(Plugin plugin) {}
	public void pluginDisabled(PluginDescriptor descriptor) {}
	public void pluginEnabled(PluginDescriptor descriptor) {}

	public void torrentActionPerformed(final Torrent[] tor, final String command) {
		//invoking the commands asynchronously so the gui won't be blocked
        new Thread(){
        	String[] mitems = TorrentTableJPopupMenu.mitems;
        	String[] priorityMenu = TorrentTableJPopupMenu.priorityMenu;
            public void run(){
        		Download d = connection.getDownloadClient();
        		for(Torrent t : tor){
            		String hash = t.getHash();
        			if(command.equals(mitems[0])){
        				//start
        				d.start(hash);
        			}else if(command.equals(mitems[1])){
        				//stop
        				d.stop(hash);
        			}else if(command.equals(mitems[2])){
        				//erase
        				d.erase(hash);
        			}else if(command.equals(mitems[3])){
        				//check hash
        				d.check_hash(hash);
        			}else if(command.equals(priorityMenu[1])){
        				//priority high
        				d.set_priority(hash,3);
        				//d.update_priorities(hash); might not need this here, only on individual file priorities.
        			}else if(command.equals(priorityMenu[2])){
        				//priority normal
        				d.set_priority(hash,2);
        				//d.update_priorities(hash);
        			}else if(command.equals(priorityMenu[3])){
        				//priority low
        				d.set_priority(hash,1);
        				//d.update_priorities(hash);
        			}else if(command.equals(priorityMenu[0])){
        				//priority off
        				d.set_priority(hash,0);
        				//d.update_priorities(hash);
        			}
        		}
            }
        }.start();
	}
}
