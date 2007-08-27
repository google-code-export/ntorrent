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

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

public class Rpc{
	XmlRpcClient client;
	public Rpc(XmlRpcClient c){
		client = c;
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Object>[] getCompleteList(String view) throws XmlRpcException{
		Object[] result;
		Object[] params = {view,
					"d.get_hash=",
					"d.get_base_filename=",
					"d.get_size_bytes=",
					"d.get_up_total=",
					"d.get_completed_bytes=",
					"d.get_down_rate=",
					"d.get_up_rate=",
					"d.get_state="};
		result = (Object[])client.execute("call_download",params);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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

}
