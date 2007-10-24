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

package org.heldig.ntorrent.io.xmlrpc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.heldig.ntorrent.io.Rpc;

/**
 * @author  Kim Eik
 */
public class XmlRpcApacheImpl implements Rpc{
	private XmlRpcQueue client;
	String systemClientVersion;
	String systemLibraryVersion;
	
	public XmlRpcApacheImpl(XmlRpcClient c) throws XmlRpcException{
		client = (XmlRpcQueue)c;
		systemClientVersion = (String)c.execute(command_system_client_version, new Object[]{});
		systemLibraryVersion = (String)c.execute(command_system_library_version, new Object[]{});
		System.out.println("Connected to host running: rtorrent-"+systemClientVersion+" / libtorrent-"+systemLibraryVersion);
	}
	
	public String getSystemClientVersion() {
		return systemClientVersion;
	}

	public String getSystemLibraryVersion() {
		return systemLibraryVersion;
	}
	
	public void getTorrentVariables(String view, XmlRpcCallback c){
		download_variable[0] = view;
		client.addToExecutionQueue(command_download,download_variable,c);
	}
	
	public void getTorrentSet(String view, XmlRpcCallback c){
		Object[] params = new Object[download_constant.length+download_variable.length];
		params[0] = view;
		int offset;
		for(offset = 0; offset < download_constant.length; offset++)
			params[offset+1] = download_constant[offset];
		for(int x = 1; x < download_variable.length; x++)
			params[offset+x] = download_variable[x];
		
		client.addToExecutionQueue(command_download,params,c);
	}

	public void torrentCommand(String[] hash, String command){
		Object[][] params = new Object[hash.length][1];
		for(int x = 0; x < hash.length; x++)
			params[x][0] = hash[x];

		multiCall(command,params,null);
	}
		
	public void loadTorrent(File torrent) throws IOException{
		System.out.println("Loading torrent from file: "+torrent );
		byte[] source = new byte[(int)torrent.length()];
		FileInputStream reader = new FileInputStream(torrent);
		reader.read(source, 0, source.length);
		Object[] params = {source};
		client.addToExecutionQueue(command_load_raw, params,null);
	}
	
	public void loadTorrent(String url){
		System.out.println("Loading torrent from url: "+url);
		if(url != null){
			Object[] params = {url};
			client.addToExecutionQueue(command_load,params, null);
		}
	}

	public void getPortRange(XmlRpcCallback c){
		client.addToExecutionQueue(command_get_port_range,null,c);
	}
	
	public void getDownloadRate(XmlRpcCallback c) {
		client.addToExecutionQueue(command_get_download_rate,null,c);
	}
	
	public void getUploadRate(XmlRpcCallback c) {
		client.addToExecutionQueue(command_get_upload_rate,null,c);	
	}
	
	private void multiCall(String command, Object[][] params, XmlRpcCallback c){
		String[] cmds = new String[params.length];
		for(int x = 0; x < cmds.length; x++)
			cmds[x] = command;
		multiCall(cmds, params, c);
	}
	
	@SuppressWarnings("unchecked")
	private void multiCall(String[] commands, Object[][] params, XmlRpcCallback c){
		List<Map> list = new ArrayList<Map>();
		for(int x = 0; x < commands.length; x++){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put(command_system_multicall_method, commands[x]);
			map.put(command_system_multicall_params,params[x]);
			list.add(map);
		}
		Object[] structainer = {list};
		client.addToExecutionQueue(command_system_multicall,structainer,c);
	}
	
	public void getFileList(String hash, XmlRpcCallback c){
		file_list[0] = hash;
		client.addToExecutionQueue(command_file,file_list,c);
	}

	/**
	 * hash,index,pri
	 * @param hash
	 * @param pri
	 * @param index
	 */
	public void setFilePriority(String hash, int pri, int[] index){
		Object[][] fparams = new Object[index.length][3];
		Object[][] dparams = new Object[index.length][1];
		
		for(int x = 0; x < fparams.length; x++){
			fparams[x][0] = dparams[x][0] = hash; 
			fparams[x][1] = index[x]; 
			fparams[x][2] = pri;
		}
		multiCall(command_file_set_priority,fparams,null);
		multiCall(command_download_update_priorities,dparams,null);
	}

	public void getTrackerList(String hash, XmlRpcCallback c) {
		tracker_list[0] = hash;
		client.addToExecutionQueue(command_tracker,tracker_list,c);		
	}

	public void setDownloadRate(Integer i, XmlRpcCallback c) {
		Object[] params = {i};
		client.addToExecutionQueue(command_set_download_rate,params,c);
	}

	public void setUploadRate(Integer i, XmlRpcCallback c) {
		Object[] params = {i};
		client.addToExecutionQueue(command_set_upload_rate,params,c);
	}

	public void setTorrentPriority(String[] hash, int pri) {
		Object[][] tparams = new Object[hash.length][2];
		Object[][] dparams = new Object[hash.length][1];
		
		for(int x = 0; x < tparams.length; x++){
			tparams[x][0] = dparams[x][0] = hash[x]; 
			tparams[x][1] = pri; 
		}
		multiCall(command_download_set_priority,tparams,null);

	}

	public void setTrackerEnabled(String hash, int[] id, boolean b, XmlRpcCallback c) {
		Object[][] tparams = new Object[id.length][3];
		
		for(int x = 0; x < tparams.length; x++){
			tparams[x][0] = hash; 
			tparams[x][1] = id[x];
			tparams[x][2] = (b ? 1 : 0);
		}
		multiCall(command_tracker_enable,tparams,null);
	}

	@Override
	public void setLabel(String[] hash, String label, XmlRpcCallback c) {
		Object[][] dparams = new Object[hash.length][2];
		for(int x = 0; x < dparams.length; x++){
			dparams[x][0] = hash[x];
			dparams[x][1] = label;
		}
		multiCall(command_label, dparams, c);
	}

}
