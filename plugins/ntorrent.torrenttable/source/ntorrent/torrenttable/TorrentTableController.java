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

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;

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
	private final XmlRpcConnection connection;
	
	public TorrentTableController(XmlRpcConnection connection) {
		Torrent tor = new Torrent("asdasd");
		tor.setProperty("torrenttable.down", 23);
		tor.setProperty("torrenttable.eta", 5);
		tor.setProperty("torrenttable.name", "LOST");
		ttm.setValueAt(tor,0);
		
		this.connection = connection;
		
		new Thread(this).start();
	}
	

	public Component getDisplay() {
		return table;
	}
	
	public void run() {
		
	   final String[] download_variable = {
               "", //reserved for view arg
               "d.get_hash=",  //ID
               "d.get_name=", //constant
               "d.get_size_bytes=", //constant
               "d.get_completed_bytes=", //variable
               "d.get_up_total=",      //variable
               "d.get_peers_complete=",
               "cat=$d.get_peers_connected=,(,$d.get_peers_not_connected=,)",
               "d.get_down_rate=", //variable
               "d.get_up_rate=", //variable
               //"d.get_state=",         //variable
               "d.get_message=", //relative
               "d.get_priority=", //relative
               //"d.get_tied_to_file=", //constant?
               //"d.get_tracker_size=",
               //#"d.get_custom1="        //label
               //"d.get_size_files=",//constant
               //"d.get_base_path=" //constant
	   };
		
	   XmlRpcClient client = connection.getClient();
	   
		try {
			XmlRpcArray download_list = (XmlRpcArray)client.invoke("d.multicall", download_variable);
			
			int i = 0;
			for(Object d : download_list){
				XmlRpcArray	data = (XmlRpcArray) d;
				Torrent tor = new Torrent(data.getString(0));
				ttm.setValueAt(tor, i++);
				for(int x = 1; x < data.size(); x++){
					tor.setProperty(TorrentTableModel.cols[x-1], data.get(x));
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
