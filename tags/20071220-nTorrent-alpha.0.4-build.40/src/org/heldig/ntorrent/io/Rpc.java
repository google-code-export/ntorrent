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
package org.heldig.ntorrent.io;

import java.io.File;
import java.io.IOException;

import org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback;


public interface Rpc {
		public final Object[] download_constant = {			
			"d.get_hash=",	//ID
			"d.get_name=", //constant
			"d.get_size_bytes=", //constant
			"d.get_size_files=",//constant
			"d.get_base_path=", //constant}
		};
	
		public final Object[] download_variable = {
				"", //reserved for view arg
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
				"d.get_tracker_size=",
				"d.get_custom1="	//label
		};
		
		public final Object[] tracker_list = {
				"", //reserved for hash arg
				"dummyarg",
				"t.get_url=",
				"t.get_id=",
				"t.get_group=",
				"t.get_min_interval=",
				"t.get_normal_interval=",
				"t.get_scrape_complete=",
				"t.get_scrape_downloaded=",
				"t.get_scrape_incomplete=",
				"t.get_scrape_time_last=",
				"t.get_type=",
				"t.is_enabled=",
				"t.is_open="
				};
		
		public final Object[] file_list = {
				"", //reserved for hash arg
 				"i/0",
				"f.get_priority=",
				"f.get_path=",
				"f.get_size_bytes="
				};
		
		public final static String command_label = "d.set_custom1";
		public final static String command_tracker_enable = "t.set_enabled";
		public final static String command_download_set_priority = "d.set_priority";
		public final static String command_set_download_rate = "set_download_rate";
		public final static String command_set_upload_rate = "set_upload_rate";
		public final static String command_tracker = "t.multicall";
		public final static String command_download_update_priorities = "d.update_priorities";
		public final static String command_file_set_priority = "f.set_priority";
		public final static String command_file = "f.multicall";
		public final static String command_system_multicall = "system.multicall";
		public final static String command_system_multicall_method = "methodName";
		public final static String command_system_multicall_params = "params";
		public final static String command_system_client_version = "system.client_version";
		public final static String command_system_library_version = "system.library_version";
		public final static String command_download = "d.multicall";
		public final static String command_load_raw = "load_raw_verbose";
		public final static String command_load = "load_verbose";
		public final static String command_get_port_range = "get_port_range";
		public final static String command_get_download_rate = "get_download_rate";
		public final static String command_get_upload_rate = "get_upload_rate";
	
		public void getTorrentVariables(String view, XmlRpcCallback c);
		public void getTorrentSet(String view, XmlRpcCallback c);
		public void torrentCommand(String[] hash, String command);
		public void loadTorrent(File torrent) throws IOException;
		public void loadTorrent(String url);
		public void getPortRange(XmlRpcCallback c);
		public void getDownloadRate(XmlRpcCallback c);
		public void getUploadRate(XmlRpcCallback c);
		public void setDownloadRate(Integer i,XmlRpcCallback c);
		public void setUploadRate(Integer i,XmlRpcCallback c);
		public String getSystemClientVersion();
		public String getSystemLibraryVersion();
		public void getFileList(String hash, XmlRpcCallback c);
		public void setTorrentPriority(String[] hash, int pri);
		public void setFilePriority(String hash, int pri, int[] index);
		public void getTrackerList(String hash,XmlRpcCallback c);
		public void setTrackerEnabled(String hash, int[] id, boolean b, XmlRpcCallback c);
		public void setLabel(String[] hash, String label, XmlRpcCallback c);
		
	}
