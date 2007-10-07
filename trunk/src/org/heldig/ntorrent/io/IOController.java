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
import java.net.UnknownHostException;

import org.apache.xmlrpc.XmlRpcException;
import org.heldig.ntorrent.io.xmlrpc.XmlRpc;
import org.heldig.ntorrent.io.xmlrpc.http.XmlRpcConnection;
import org.heldig.ntorrent.io.xmlrpc.local.LocalConnection;
import org.heldig.ntorrent.io.xmlrpc.ssh.SshConnection;
import org.heldig.ntorrent.model.ClientProfile;

import com.sshtools.j2ssh.transport.InvalidHostFileException;

/**
 * @author  Kim Eik
 */
public class IOController {

	private Rpc rpc;
	
	public void connect(ClientProfile p) throws XmlRpcException, InvalidHostFileException, UnknownHostException, IOException {
		RpcConnection conn = null;
		switch(p.getProt()){
			case HTTP:
				conn = new XmlRpcConnection(p);
				break;
			case SSH:
				conn = new SshConnection(p);
				break;
			case LOCAL:
				conn = new LocalConnection(p);
				break;
		}
		conn.setUsername(p.getUsername());
		conn.setPassword(p.getPassword());
		//2.Connect to server
		rpc = new XmlRpc(conn.connect());
	}
	
	public boolean loadTorrent(File file) throws IOException, XmlRpcException{
		if(rpc != null){
			rpc.loadTorrent(file);
			return true;
		}
		return false;
	}

	public boolean loadTorrent(String url){
		if(rpc != null) {
			rpc.loadTorrent(url);
			return true;
		}
		return false;
	}
	
	public void loadStartupFiles(String[] filesToLoad){
			try {
				for(String file: filesToLoad)
					loadTorrent(new File(file));
			} catch (Exception x){
				x.printStackTrace();
			}
	}	
	
	/**
	 * @return
	 */
	public Rpc getRpc() {
		return rpc;
	}

}
