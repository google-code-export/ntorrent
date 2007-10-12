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

package org.heldig.ntorrent.gui.torrent;


import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import org.apache.xmlrpc.XmlRpcRequest;
import org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback;
import org.heldig.ntorrent.model.Bit;

/**
 * @author	Kim Eik
 */
public class TorrentPool implements XmlRpcCallback{
	private TorrentSet torrents = new TorrentSet();
	private TorrentSet viewset = new TorrentSet();
	private String view = "main";
	public AbstractTableModel table;
	private Bit rateUp = new Bit(0);
	private Bit rateDown = new Bit(0);
	private Thread mcThread;

	public TorrentPool(AbstractTableModel table){
		this.table = table;
	}
	
	public void setMcThread(Thread mcThread) {
		this.mcThread = mcThread;
	}
	
	/**
	 * @return
	 */
	public String getView() {
		return view;
	}

	public int size(){ return viewset.size(); }
	public TorrentInfo get(int index){ return viewset.get(index);	}

	/**
	 * @param  v
	 */
	public void setView(String v){
		System.out.println("Setting view to "+v);
		view = v.toLowerCase();
		if(v.equals("main"))
			viewset = torrents;
		else
			viewset = new TorrentSet();
		
		//interrupt thread
		mcThread.interrupt();
	}
	
	/**
	 * @return
	 */
	public Bit getRateDown() {
		return rateDown;
	}
	
	/**
	 * @return
	 */
	public Bit getRateUp() {
		return rateUp;
	}

	
	public String[] getLabels(){
		String labels = "";
		for(String hash : torrents.getHashSet()){
			String label = torrents.get(hash).getLabel();
			if(label != "")
				labels += (labels == "" ? label : ";"+label);
				
		}
		return labels.split(";");
	}
	
	public String[] getHash(int[] i){
		int x = 0;
		String[] hashlist = new String[i.length];
		for(int index : i){
			hashlist[x++] = get(index).getHash();
		}
		return hashlist;
	}
	
	public String[] getHashList(){
		String[] list = new String[torrents.size()];
		torrents.getHashSet().toArray(list);
		return list;
		
	}
	
	private void removeOutdated() {
		for(int x = 0; x < viewset.size(); x++){
			TorrentInfo tf = viewset.get(x);
			if(tf.isOutOfDate()){
				viewset.remove(x);
				table.fireTableRowsDeleted(x, x);
			}
		}
	}

	@Override
	public void handleResult(XmlRpcRequest pRequest, Object pResult) {
		rateUp.setValue(0);
		rateDown.setValue(0);
		Object[] obj = (Object[])pResult;
		int viewSize = viewset.size();
		
		for(int x = 0; x < obj.length; x++){
			HashMap<String,Object> result = new HashMap<String,Object>();
			Object[] raw = (Object[])obj[x];
			TorrentInfo tf = torrents.get((String)raw[0]);
			for(int z = 2; z < pRequest.getParameterCount(); z++)
				result.put((String)pRequest.getParameter(z), raw[z-1]);
			
			if(tf == null/* && fullUpdate*/){
				tf = new TorrentInfo((String)raw[0]);
				torrents.add(tf);
				table.fireTableRowsInserted(x, x);
				mcThread.interrupt();
			}
	
			viewset.add(tf);
			tf.setInfo(result);
			
			
			rateUp.appendValue(tf.getRateUp());
			rateDown.appendValue(tf.getRateDown());
			
		}
		if(viewSize == 0)
			table.fireTableDataChanged();
		else
			table.fireTableRowsUpdated(0, obj.length);
		removeOutdated();
		
	}		
}
