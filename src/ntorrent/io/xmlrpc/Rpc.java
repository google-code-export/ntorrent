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

package ntorrent.io.xmlrpc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import ntorrent.Controller;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.AsyncCallback;
import org.apache.xmlrpc.client.XmlRpcClient;

public class Rpc{
	RpcQueue client;
	String systemClientVersion;
	String systemLibraryVersion;
	
	Object[] constant = {			
			"d.get_hash=",	//ID
			"d.get_name=", //constant
			"d.get_size_bytes=", //constant
			"d.get_size_files=",//constant
			"d.get_base_path=", //constant}
	};
	
	public static Object[] variable = {
			"", //reserved for view
			"d.get_hash=",	//ID
			"d.get_up_total=",	//variable
			"d.get_completed_bytes=", //variable
			"d.get_down_rate=", //variable
			"d.get_up_rate=", //variable
			"d.get_state=",		//variable
			"d.get_message=", //relative
			"d.get_priority=", //relative
			"d.get_tied_to_file=", //constant?
			"d.get_peers_connected=",
			"d.get_peers_not_connected=",
			"d.get_peers_complete=",
			"d.get_tracker_size="	
	};
	
	
	public Rpc(XmlRpcClient c){
		client = (RpcQueue)c;
		Object[] params = {};
		try {
			systemClientVersion = (String)c.execute("system.client_version", params);
			systemLibraryVersion = (String)c.execute("system.library_version", params);
			Controller.writeToLog("Host running: rtorrent-"+systemClientVersion+" / libtorrent-"+systemLibraryVersion);
		} catch (XmlRpcException e) {
			Controller.writeToLog(e);
			Controller.getGui().showError(e.getLocalizedMessage());
		}

	}
	
	public void getTorrentVariables(String view, AsyncCallback c) throws XmlRpcException{
		variable[0] = view;
		client.executeAsync("d.multicall",variable,c);
	}
	
	public void getTorrentSet(String view, AsyncCallback c) throws XmlRpcException{
		Object[] params = new Object[constant.length+variable.length];
		params[0] = view;
		int offset;
		for(offset = 0; offset < constant.length; offset++)
			params[offset+1] = constant[offset];
		for(int x = 1; x < variable.length; x++)
			params[offset+x] = variable[x];
		
		client.executeAsync("d.multicall",params,c);
	}
	
	@SuppressWarnings("unchecked")
	private Vector<Object>[] multicallToVector(Object[] result){
		Vector<Object>[] out = new Vector[result.length];
		for(int x = 0; x < result.length; x++){
			out[x] = new Vector<Object>();
			for(Object obj : (Object[])result[x]){
				out[x].add(obj);
			}	
		}
		return out;
	}

	/*public Vector<Object>[] getCompleteList() throws XmlRpcException{
		return getCompleteList("main");
	}*/
	
	
	public void fileCommand(String hash,String command){
		Object[] params = {hash};
		client.executeAsync(command,params, null);

	}	
	
	public void loadTorrent(File torrent) throws IOException, XmlRpcException{
		Controller.writeToLog("Loading torrent from file: "+torrent );
		byte[] source = new byte[(int)torrent.length()];
		FileInputStream reader = new FileInputStream(torrent);
		reader.read(source, 0, source.length);
		Object[] params = {source};
		client.executeAsync("load_raw_verbose", params,null);
	}
	
	public void loadTorrent(String url) throws XmlRpcException{
		Controller.writeToLog("Loading torrent from url: "+url);
		if(url != null){
		Object[] params = {url};
		client.executeAsync("load_verbose",params, null);
		}
	}
	
	public String getPortRange() throws XmlRpcException{
		Object[] params = {};
		return ((String)client.execute("get_port_range",params));
	}
	
	public long getDownloadRate() throws XmlRpcException{
		Object[] params = {};
		return ((Long)client.execute("get_download_rate", params));
	}
	
	public long getUploadRate() throws XmlRpcException{
		Object[] params = {};
		return ((Long)client.execute("get_upload_rate", params));		
	}
	
	public String getSystemClientVersion() {
		return systemClientVersion;
	}
	
	public String getSystemLibraryVersion() {
		return systemLibraryVersion;
	}
	
	public Vector<Object>[] getFileList(String hash) throws XmlRpcException{
		Object[] result;
		Object[] params = {hash,"i/0",
				"f.get_priority=",
				"f.get_path=",
				"f.get_size_bytes="
				};
		result = (Object[])client.execute("f.multicall",params);
		return multicallToVector(result);
		
	}
	
	public void setFilePriority(String hash,int index,int pri) throws XmlRpcException{
		Object[] params = {hash,index,pri};
		client.executeAsync("f.set_priority",params,null);
	}

}
