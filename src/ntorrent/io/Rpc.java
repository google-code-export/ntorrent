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

package ntorrent.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import ntorrent.controller.Controller;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

public class Rpc{
	XmlRpcClient client;
	String systemClientVersion;
	String systemLibraryVersion;
	
	public Rpc(XmlRpcClient c){
		client = c;
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
	
	public Vector<Object>[] getCompleteList(String view) throws XmlRpcException{
		Object[] result;
		Object[] params = {view,
					"d.get_hash=",	//constant
					"d.get_name=", //constant
					"d.get_size_bytes=", //constant
					"d.get_up_total=",	//variable
					"d.get_completed_bytes=", //variable
					"d.get_down_rate=", //variable
					"d.get_up_rate=", //variable
					"d.get_state=",		//variable
					"d.get_size_files=",//constant
					"d.get_base_path=", //constant
					"d.get_message=", //relative
					"d.get_priority=", //relative
					"d.get_tied_to_file=" //constant?
					//"d.get_chunk_size="//constant?
				
					};
		result = (Object[])client.execute("d.multicall",params);
		//get_size_chunks = size in chunks
		return multicallToVector(result);
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

	public Vector<Object>[] getCompleteList() throws XmlRpcException{
		return getCompleteList("main");
	}
	
	
	public void fileCommand(String hash,String command){
		Object[] params = {hash};
		try {
			client.execute(command,params);
		} catch (XmlRpcException e) {
			Controller.writeToLog(e);
		}
	}
	
	public void loadTorrent(File torrent) throws IOException, XmlRpcException{
		byte[] source = new byte[(int)torrent.length()];
		FileInputStream reader = new FileInputStream(torrent);
		reader.read(source, 0, source.length);
		Object[] params = {source};
		client.execute("load_raw", params);
	}
	
	public void loadTorrent(String url) throws XmlRpcException{
		if(url != null){
		Object[] params = {url};
		client.execute("load",params);
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
	
	/**
	 *  Index 109 String: 'f.get_completed_chunks'
  Index 110 String: 'f.get_frozen_path'
  Index 111 String: 'f.get_is_created'
  Index 112 String: 'f.get_is_open'
  Index 113 String: 'f.get_last_touched'
  Index 114 String: 'f.get_match_depth_next'
  Index 115 String: 'f.get_match_depth_prev'
  Index 116 String: 'f.get_offset'
  Index 117 String: 'f.get_path'
  Index 118 String: 'f.get_path_components'
  Index 119 String: 'f.get_path_depth'
  Index 120 String: 'f.get_priority'
  Index 121 String: 'f.get_range_first'
  Index 122 String: 'f.get_range_second'
  Index 123 String: 'f.get_size_bytes'
  Index 124 String: 'f.get_size_chunks'
  Index 125 String: 'f.multicall'
  Index 126 String: 'f.set_priority'

	 */
	
	/**
	 * set_upload_rate
	 * set_download_rate

	 */

}
