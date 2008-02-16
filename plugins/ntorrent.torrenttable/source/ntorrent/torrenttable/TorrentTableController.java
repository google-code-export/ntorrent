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

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableRowSorter;

import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcFault;

import ntorrent.gui.window.Window;
import ntorrent.io.rtorrent.Download;
import ntorrent.io.rtorrent.Global;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import ntorrent.profile.model.ClientProfileInterface;
import ntorrent.profile.model.LocalProfileModel;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentTableModel;
import ntorrent.torrenttable.view.TorrentTable;
import ntorrent.torrenttable.view.TorrentTablePopupMenu;

public class TorrentTableController implements Runnable{
	
	final TorrentTableModel ttm = new TorrentTableModel();
	final TorrentTable table = new TorrentTable(ttm);
	final Map<String,Torrent> torrents = new HashMap<String,Torrent>();
	final TorrentTableRowSorter sorter = new TorrentTableRowSorter(ttm);
	private final XmlRpcConnection connection;
	

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
		table.setRowSorter(sorter);
		new Thread(this).start();
	}
	

	public Component getDisplay() {
		return table;
	}
	
	public void run() {
	   XmlRpcClient client = connection.getClient();
		try {
			while(true){
				XmlRpcArray download_list = (XmlRpcArray)client.invoke("d.multicall", download_variable);
			
				if(ttm.getRowCount() > download_list.size()){
					int diff = ttm.getRowCount()-download_list.size();
					int x = 0;
					while(x < diff){
						//System.out.println("removing row: "+(download_list.size()+x));
						ttm.removeRow(download_list.size()+x);
						x++;
					}

				}
				
				for(int x = 0 ; x < download_list.size(); x++){
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
					
					//System.out.println(ttm.getRowCount()+" "+x);
					if(ttm.getRowCount() > x)
						ttm.setValueAt(tor, x);
					else
						ttm.addRow(tor);
					//System.out.println(ttm.getRowCount()+" "+x);
				}
				try {
					Thread.sleep(1000);
					//ttm.removeRow(ttm.getRowCount()-1);
					Thread.sleep(1000);
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

	public static void main(String[] args) {
		XmlRpcConnection c = new XmlRpcConnection(new LocalProfileModel());
		TorrentTableController t = new TorrentTableController(c);
		Window w = new Window();
		w.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		w.setContentPane(new JScrollPane(t.getDisplay()));
		w.drawWindow();		
		
	}

}
