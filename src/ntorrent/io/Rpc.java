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
import java.io.IOException;

import ntorrent.io.xmlrpc.XmlRpcCallback;

public interface Rpc {		
		Object[] constant = {			
				"d.get_hash=",	//ID
				"d.get_name=", //constant
				"d.get_size_bytes=", //constant
				"d.get_size_files=",//constant
				"d.get_base_path=", //constant}
		};
		
		Object[] variable = {
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
		
		
		
		public void getTorrentVariables(String view, XmlRpcCallback c);
		public void getTorrentSet(String view, XmlRpcCallback c);
		public void fileCommand(String[] hash, String command);
		public void loadTorrent(File torrent) throws IOException;
		public void loadTorrent(String url);
		
		//@TODO deprecated
		public void getPortRange(XmlRpcCallback c);
		public void getDownloadRate(XmlRpcCallback c);
		public void getUploadRate(XmlRpcCallback c);
		
		public String getSystemClientVersion();
		public String getSystemLibraryVersion();
		public void getFileList(String hash, XmlRpcCallback c);
		public void setFilePriority(String hash, int pri, int[] index);
		
	}
