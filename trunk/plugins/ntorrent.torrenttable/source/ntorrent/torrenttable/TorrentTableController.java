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

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;

import ntorrent.env.Environment;
import ntorrent.gui.window.Window;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import ntorrent.profile.model.LocalProfileModel;
import ntorrent.tools.Serializer;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentTableColumnModel;
import ntorrent.torrenttable.model.TorrentTableModel;
import ntorrent.torrenttable.view.TorrentTable;
import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcFault;

public class TorrentTableController implements Runnable{
	
	private final TorrentTableModel ttm = new TorrentTableModel();
	private final TorrentTable table = new TorrentTable(ttm);
	private final Map<String,Torrent> torrents = new HashMap<String,Torrent>();
	private final XmlRpcConnection connection;
	
	
	private final JPanel panel = new JPanel(new BorderLayout());

    Object[] download_variable = {
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
		panel.add(new JScrollPane(table));
		
		new Thread(this).start();
		
		if(Double.parseDouble(System.getProperty("java.specification.version")) >= 1.6)
			try {
				//throws nullpointer exception if Environment is not set up.
				PluginManager manager = Environment.getPluginManager();
				ExtensionPoint ext = manager.getRegistry().getExtensionPoint("ntorrent.torrenttable","TorrentTableSorter");
				for(Extension e : ext.getAvailableExtensions()){
					PluginDescriptor p = e.getDeclaringPluginDescriptor();
					Class cls = manager.getPluginClassLoader(p).loadClass(p.getPluginClassName());
					TorrentTableExtension tte = (TorrentTableExtension) cls.newInstance();
					tte.init(this);
				}
			} catch (Exception x) {
				// TODO Auto-generated catch block
				x.printStackTrace();
			}
		
	}
	

	public JPanel getDisplay() {
		return panel;
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
					Thread.sleep(5000);
					//ttm.removeRow(ttm.getRowCount()-1);
					//Thread.sleep(1000);
					//System.out.println(table.getSelectedRow());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		XmlRpcConnection c = new XmlRpcConnection(new LocalProfileModel());
		TorrentTableController t = new TorrentTableController(c);
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		Window w = new Window();
		w.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		w.setContentPane(t.getDisplay());
		w.drawWindow();		
		
	}

}
