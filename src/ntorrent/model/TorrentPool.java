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

import ntorrent.Controller;
import ntorrent.io.xmlrpc.Rpc;
import ntorrent.io.xmlrpc.RpcCallback;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;

public class TorrentPool extends RpcCallback{
	TorrentSet torrents = new TorrentSet();
	private TorrentSet viewset = new TorrentSet();
	String view = "main";
	Rpc rpc;
	TorrentTableModel table;
	long rateUp,rateDown;

	TorrentPool(){}

	public TorrentPool(Rpc r, TorrentTableModel t) throws XmlRpcException{
		rpc = r;
		table = t;
		rateUp = rateDown = 0;
	}	
	
	
	public String getView() {
		return view;
	}
	
	//From viewset
	public int size(){ return viewset.size(); }
	public TorrentFile get(int index){ return viewset.get(index);	}

	public void setView(String v){
		//tsk tsk
		torrents = new TorrentSet();
		
		view = v;
		if(v.equalsIgnoreCase("main"))
			viewset = torrents;
		else
			viewset = new TorrentSet();
	}
	
	public long getRateDown() {
		return rateDown;
	}
	
	public long getRateUp() {
		return rateUp;
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
	
	public void stopAll(){
		//for(TorrentFile tf : torrents)
			//rpc.fileCommand(tf.getHash(), "d.stop");
	}
	
	public void startAll(){
		//for(TorrentFile tf : torrents)
			//rpc.fileCommand(tf.getHash(), "d.start");		
	}

	private void removeOutdated() {
		for(int x = 0; x < viewset.size(); x++){
			TorrentFile tf = viewset.get(x);
			if(tf.isOutOfDate()){
				viewset.remove(x);
				table.fireTableRowsDeleted(x, x);
			}
		}
	}

	@Override
	public void handleResult(XmlRpcRequest pRequest, Object pResult) {
		Object[] obj = (Object[])pResult;
		
		for(int x = 0; x < obj.length; x++){
			Object[] raw = (Object[])obj[x];
			TorrentFile tf = torrents.get((String)raw[0]);
			if(tf == null){
				tf = new TorrentFile((String)raw[0]);
				torrents.add(tf);
				table.fireTableRowsInserted(x, x);
			}
			tf.update(raw);
			setRate(tf);
			viewset = torrents;
		}
		table.fireTableRowsUpdated(0, obj.length);
		removeOutdated();
	}
	
	private void setRate(TorrentFile tf){
		rateUp = rateDown = 0;
		rateUp += tf.getRateUp().getValue();
		rateDown += tf.getRateDown().getValue();
	}
	
}
