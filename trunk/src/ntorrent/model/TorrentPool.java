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

package ntorrent.model;

import java.util.Vector;

import ntorrent.io.Rpc;

import org.apache.xmlrpc.XmlRpcException;

public class TorrentPool {
	TorrentSet torrents = new TorrentSet();
	private TorrentSet viewset = new TorrentSet();
	String view = "main";
	Rpc rpc;
	TorrentTableModel table;

	TorrentPool(){}

	public TorrentPool(Rpc r, TorrentTableModel t) throws XmlRpcException{
		rpc = r;
		table = t;
		update(false);
	}	
	
	//From viewset
	public int size(){ return viewset.size(); }
	public TorrentFile get(int index){ return viewset.get(index);	}

	public void setView(String v){
		view = v;
		if(v.equalsIgnoreCase("main"))
			viewset = torrents;
		else
			viewset = new TorrentSet();
	}
	public void update() throws XmlRpcException{ update(true);}
	private void update(boolean refresh) throws XmlRpcException{
		//Inefficient!
		torrents = new TorrentSet();
		
		Vector<Object>[] updateList = rpc.getCompleteList(view);
		//System.out.println("Running update on "+updateList.length+" torrents");
		for(Vector obj : updateList){
			TorrentFile tf;
			//hash
			String hash = (String)obj.get(0);
			if((tf = viewset.get(hash)) == null){
				if((tf = torrents.get(hash)) == null){
					//Torrent doesnt exist.
					System.out.println("Adding torrent "+hash);
					tf = new TorrentFile(hash);
					torrents.add(tf);
					//name
					tf.setFilename((String)obj.get(1));
					//size
					tf.setByteSize((Long)obj.get(2));
				}
				viewset.add(torrents.get(hash));
			}
			//up-total
			tf.setBytesUploaded((Long)obj.get(3));
			//down-total
			tf.setBytesDownloaded((Long)obj.get(4));
			//down-rate
			tf.setRateDown((Long)obj.get(5));
			//up-rate
			tf.setRateUp((Long)obj.get(6));
			//state
			tf.setStarted(((Long)obj.get(7) == 1 ? true : false));
		}
		
		if((table.getRowCount() > 0) && refresh){
			table.fireTableRowsUpdated(0, table.getRowCount()-1);
		}
	}
	

	public void checkHash(int i){
		rpc.fileCommand(get(i).getHash(),"d.check_hash");
	}
	public void close(int i){
		rpc.fileCommand(get(i).getHash(), "d.close");
	}
	public void erase(int i){
		rpc.fileCommand(get(i).getHash(), "d.erase");
	}
	public void open(int i){
		rpc.fileCommand(get(i).getHash(), "d.open");
	}
	public void start(int i){
		rpc.fileCommand(get(i).getHash(), "d.start");
	}
	public void stop(int i){
		rpc.fileCommand(get(i).getHash(), "d.stop");
	}	
	
}
