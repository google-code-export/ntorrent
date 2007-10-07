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
import java.io.PrintStream;
import java.net.UnknownHostException;

import org.apache.xmlrpc.XmlRpcException;
import org.heldig.ntorrent.io.xmlrpc.XmlRpc;
import org.heldig.ntorrent.io.xmlrpc.http.XmlRpcConnection;
import org.heldig.ntorrent.io.xmlrpc.local.LocalConnection;
import org.heldig.ntorrent.io.xmlrpc.ssh.SshConnection;
import org.heldig.ntorrent.model.ClientProfile;
import org.heldig.ntorrent.settings.Constants;

import com.sshtools.j2ssh.transport.InvalidHostFileException;

/**
 * @author  Kim Eik
 */
public class IOController {

	public static ErrorStream log = new ErrorStream();
	private Rpc rpc;
	private RpcConnection rpcLink;
	
	public IOController() {
		System.setOut(new PrintStream(log));
		System.setErr(new PrintStream(log));
		System.out.println(Constants.getReleaseName());
	}
	
	public void connect(ClientProfile p) throws XmlRpcException, InvalidHostFileException, UnknownHostException, IOException {
		switch(p.getProt()){
			case HTTP:
				rpcLink = new XmlRpcConnection(p);
				break;
			case SSH:
				rpcLink = new SshConnection(p);
				break;
			case LOCAL:
				rpcLink = new LocalConnection(p);
				break;
		}
		rpcLink.setUsername(p.getUsername());
		rpcLink.setPassword(p.getPassword());
		//2.Connect to server
		rpc = new XmlRpc(rpcLink.connect());
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
	
	public RpcConnection getRpcLink() {
		return rpcLink;
	}

}
