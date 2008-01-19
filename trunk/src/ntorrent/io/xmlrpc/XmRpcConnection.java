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

import java.util.logging.Logger;

import ntorrent.gui.profile.ClientProfile;
import ntorrent.io.logging.SSHLogger;
import ntorrent.io.rtorrent.Download;
import ntorrent.io.rtorrent.File;
import ntorrent.io.rtorrent.Global;
import ntorrent.io.rtorrent.PeerConnection;
import ntorrent.io.rtorrent.System;
import ntorrent.io.rtorrent.Tracker;
import ntorrent.io.tools.LocalPort;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcHTTPClient;
import redstone.xmlrpc.XmlRpcProxy;
import redstone.xmlrpc.XmlRpcSocketClient;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class XmRpcConnection {
	XmlRpcClient client;
	JSch jsch;
	Session session;
	
	public XmRpcConnection(ClientProfile profile) {
		try{
			switch (profile.getProtocol()) {
			case HTTP:
				//TODO make streaming in clientprofile?
				client = new XmlRpcHTTPClient("http://"+profile.getHost()+
						":"+profile.getPort()+
						profile.getMountPoint(),
						false);
				break;
				
			case SSH:
				JSch.setLogger(new SSHLogger());
				jsch = new JSch();
				session = jsch.getSession(
						profile.getUsername(), 
						profile.getHost(), 
						profile.getPort());
				
				session.setPassword(profile.getPassword());
				session.setConfig("StrictHostKeyChecking","no");
				
				int localPort = LocalPort.findFreePort();
				
				session.connect();
				
				session.setPortForwardingL(
						localPort,
						"127.0.0.1",
						profile.getSocketPort());
								
				client = new XmlRpcSocketClient(
						"127.0.0.1",
						localPort);
				break;
				
			case LOCAL:
				client = new XmlRpcSocketClient(profile.getHost(),
						profile.getSocketPort());
				break;
			}
			
			System system = getSystemClient();
			
			Logger.global.info("Connected to: Host "+
					system.hostname()+" Running: client "+
					system.client_version()+", library "+
					system.library_version()+", pid="+system.pid());
			
		}catch(Exception x){
			//TODO FIX
			x.printStackTrace();
		}
	}
	
	public XmlRpcClient getClient() {
		return client;
	}
	
	public System getSystemClient(){
		return (System)XmlRpcProxy.createProxy("system",new Class[] { System.class }, client);
	}
	
	public Download getDownloadClient(){
		return (Download)XmlRpcProxy.createProxy("d",new Class[] { Download.class }, client);
	}
	
	public Global getGlobalClient(){
		return (Global)XmlRpcProxy.createProxy("",new Class[] { Global.class }, client);
	}
	
	public PeerConnection getPeerConnectionClient(){
		return (PeerConnection)XmlRpcProxy.createProxy("p",new Class[] { PeerConnection.class }, client);
	}
	
	public File getFileClient(){
		return (File)XmlRpcProxy.createProxy("f",new Class[] { File.class }, client);
	}
	
	public Tracker getTrackerClient(){
		return (Tracker)XmlRpcProxy.createProxy("t",new Class[] { Tracker.class }, client);
	}

}
