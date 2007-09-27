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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ntorrent.Controller;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

public class Rpc{
	private static RpcQueue client;
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
	
	public void getTorrentVariables(String view, RpcCallback c) throws XmlRpcException{
		variable[0] = view;
		client.addToExecutionQueue("d.multicall",variable,c);
	}
	
	public void getTorrentSet(String view, RpcCallback c) throws XmlRpcException{
		Object[] params = new Object[constant.length+variable.length];
		params[0] = view;
		int offset;
		for(offset = 0; offset < constant.length; offset++)
			params[offset+1] = constant[offset];
		for(int x = 1; x < variable.length; x++)
			params[offset+x] = variable[x];
		
		client.addToExecutionQueue("d.multicall",params,c);
	}

	public void fileCommand(String[] hash, String command){
		Object[][] params = new Object[hash.length][1];
		for(int x = 0; x < hash.length; x++)
			params[x][0] = hash[x];

		multiCall(command,params,null);
		//client.addToExecutionQueue(command,params, null);
	}
	
	/**
	 * @deprecated
	 * @param torrent
	 * @throws IOException
	 * @throws XmlRpcException
	 */
	public void fileCommand(String hash,String command){
		//Object[] params = {hash};
		//client.addToExecutionQueue(command,params, null);

	}
	
	public void loadTorrent(File torrent) throws IOException, XmlRpcException{
		Controller.writeToLog("Loading torrent from file: "+torrent );
		byte[] source = new byte[(int)torrent.length()];
		FileInputStream reader = new FileInputStream(torrent);
		reader.read(source, 0, source.length);
		Object[] params = {source};
		client.addToExecutionQueue("load_raw_verbose", params,null);
	}
	
	public void loadTorrent(String url) throws XmlRpcException{
		Controller.writeToLog("Loading torrent from url: "+url);
		if(url != null){
			Object[] params = {url};
			client.addToExecutionQueue("load_verbose",params, null);
		}
	}
	//@TODO deprecated
	public void getPortRange(RpcCallback c) throws XmlRpcException{
		client.addToExecutionQueue("get_port_range",null,c);
	}
	
	public void getDownloadRate(RpcCallback c) throws XmlRpcException{
		client.addToExecutionQueue("get_download_rate",null,c);
	}
	
	public void getUploadRate(RpcCallback c) throws XmlRpcException{
		client.addToExecutionQueue("get_upload_rate",null,c);		
	}
	//
	
	public String getSystemClientVersion() {
		return systemClientVersion;
	}
	
	public String getSystemLibraryVersion() {
		return systemLibraryVersion;
	}
	
	private void multiCall(String command, Object[][] params, RpcCallback c){
		String[] cmds = new String[params.length];
		for(int x = 0; x < cmds.length; x++)
			cmds[x] = command;
		multiCall(cmds, params, c);
	}
	
	private void multiCall(String[] commands, Object[][] params, RpcCallback c){
		List<Map> list = new ArrayList<Map>();
		for(int x = 0; x < commands.length; x++){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("methodName", commands[x]);
			map.put("params",params[x]);
			list.add(map);
		}
		Object[] structainer = {list};
		client.addToExecutionQueue("system.multicall",structainer,c);
	}
	
	public static void getFileList(String hash, RpcCallback c){
		Object[] params = {hash,"i/0",
				"f.get_priority=",
				"f.get_path=",
				"f.get_size_bytes="
				};
		client.addToExecutionQueue("f.multicall",params,c);
		
	}

	/**
	 * hash,index,pri
	 * @param hash
	 * @param pri
	 * @param index
	 */
	public void setFilePriority(String hash, int pri, int[] index){
		Object[][] params = new Object[index.length][3];
		for(int x = 0; x < index.length; x++){
			params[x][0] = hash; 
			params[x][1] = index[x]; 
			params[x][2] = pri;
		}
		multiCall("f.set_priority",params,null);
	}
}
