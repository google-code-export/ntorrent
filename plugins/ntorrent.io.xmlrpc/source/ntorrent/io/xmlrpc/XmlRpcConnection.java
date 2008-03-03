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

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.util.logging.Logger;

import ntorrent.io.rtorrent.Download;
import ntorrent.io.rtorrent.File;
import ntorrent.io.rtorrent.Global;
import ntorrent.io.rtorrent.PeerConnection;
import ntorrent.io.rtorrent.System;
import ntorrent.io.rtorrent.Tracker;
import ntorrent.profile.model.ClientProfileInterface;
import ntorrent.profile.model.HttpProfileModel;
import ntorrent.profile.model.LocalProfileModel;
import ntorrent.profile.model.ProxyProfileModel;
import ntorrent.profile.model.SshProfileModel;
import ntorrent.tools.LocalPort;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcHTTPClient;
import redstone.xmlrpc.XmlRpcProxy;
import redstone.xmlrpc.XmlRpcSocketClient;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.ProxyHTTP;
import com.jcraft.jsch.ProxySOCKS5;
import com.jcraft.jsch.Session;

public class XmlRpcConnection {
	XmlRpcClient client;
	JSch jsch;
	Session session;
	ClientProfileInterface profile;
	
	public XmlRpcConnection(ClientProfileInterface p) throws XmlRpcException {
		profile = p;
		switch (p.getProtocol()) {
		case HTTP:
			//TODO make streaming in clientprofile?
			try {
					HttpProfileModel profile = (HttpProfileModel) p;
					
					ProxyProfileModel proxyProfile = profile.getProxy();
					Proxy proxy = null;
					
					switch(profile.getProxy().getType()){
					case DIRECT:
						proxy = Proxy.NO_PROXY;
						break;
					case HTTP:
					case SOCKS:
						proxy = new Proxy(proxyProfile.getType(),new InetSocketAddress(proxyProfile.getHost(),proxyProfile.getPort()));
						break;
					}
					
					String url = "http://"+profile.getHost()+":"+profile.getPort()+profile.getMountpoint();
					Logger.global.info("URL: "+url);
					
					XmlRpcHTTPClient client = new XmlRpcHTTPClient(url,proxy,false);
					
					/** set username & pass **/
					client.setBasicUsername(profile.getUsername());
					client.setBasicPassword(profile.getPassword());
					
					this.client = client;
					
				} catch (MalformedURLException e) {
					throw new XmlRpcException(e.getMessage(),e);
				}
			break;
			
		case SSH:
			try{
				SshProfileModel profile = (SshProfileModel) p;
				JSch.setLogger(new SSHLogger());
				jsch = new JSch();
				session = jsch.getSession(
						profile.getUsername(), 
						profile.getHost(), 
						profile.getPort());
				
				session.setPassword(profile.getPassword());
				session.setConfig("StrictHostKeyChecking","no");
				
				ProxyProfileModel proxy = profile.getProxy();
				switch(profile.getProxy().getType()){
					case HTTP:
						session.setProxy(new ProxyHTTP(proxy.getHost(),proxy.getPort()));
						break;
					case SOCKS:
						session.setProxy(new ProxySOCKS5(proxy.getHost(),proxy.getPort()));
						break;
				}
				
				int localPort = LocalPort.findFreePort();
				
				session.connect();
				
				session.setPortForwardingL(
						localPort,
						profile.getHost(),
						profile.getSocketport());
				
				client = new XmlRpcSocketClient(
						"127.0.0.1", // must be 127.0.0.1, dont change this again in your sleep!
						localPort);
			}catch(Exception x){
				throw new XmlRpcException(x.getMessage(),x);
			}
			break;
			
		case LOCAL:
			LocalProfileModel profile = (LocalProfileModel) p;
			client = new XmlRpcSocketClient(profile.getHost(),
					profile.getSocketport());
			break;
		}
		
		
		/** Set xmlrpc dialect **/
		getGlobalClient().xmlrpc_dialect("i8");
		
		System system = getSystemClient();
		
		Logger.global.info("Connected to: Host "+
				system.hostname()+" Running: client "+
				system.client_version()+", library "+
				system.library_version()+", pid="+system.pid());
		
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
	
	public ClientProfileInterface getProfile() {
		return profile;
	}

}
