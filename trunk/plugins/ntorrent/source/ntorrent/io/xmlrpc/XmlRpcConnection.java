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

import ntorrent.io.rtorrent.Download;
import ntorrent.io.rtorrent.File;
import ntorrent.io.rtorrent.Global;
import ntorrent.io.rtorrent.PeerConnection;
import ntorrent.io.rtorrent.System;
import ntorrent.io.rtorrent.Tracker;
import ntorrent.profile.model.ConnectionProfile;

import org.apache.log4j.Logger;

import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcProxy;


/**
 * This class handles an xmlrpc connection.
 * @author Kim Eik
 */
public class XmlRpcConnection {
	private final ConnectionProfile profile;
	private XmlRpcClient client;
	
	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(XmlRpcConnection.class);
	
	/**
	 * @param p
	 * @throws XmlRpcException
	 */
	public XmlRpcConnection(ConnectionProfile profile) throws XmlRpcException {
		this.profile = profile;
	}
	
	/**
	 * Returns the raw XmlRpcClient
	 * @return
	 */
	public XmlRpcClient getClient() {
		return client;
	}
	
	/**
	 * Proxies the client to the System interface.
	 * @return System
	 */
	public System getSystemClient(){
		return (System)XmlRpcProxy.createProxy("system",new Class[] { System.class }, client);
	}
	
	/**
	 * Proxies the client to the Download interface.
	 * @return Download
	 */
	public Download getDownloadClient(){
		return (Download)XmlRpcProxy.createProxy("d",new Class[] { Download.class }, client);
	}
	
	/**
	 * Proxies the client to the Global interface.
	 * @return Global
	 */
	public Global getGlobalClient(){
		return (Global)XmlRpcProxy.createProxy("",new Class[] { Global.class }, client);
	}
	
	/**
	 * Proxies the client to the PeerConnection interface.
	 * @return PeerConnection
	 */
	public PeerConnection getPeerConnectionClient(){
		return (PeerConnection)XmlRpcProxy.createProxy("p",new Class[] { PeerConnection.class }, client);
	}
	
	/**
	 * Proxies the client to the File interface.
	 * @return File
	 */
	public File getFileClient(){
		return (File)XmlRpcProxy.createProxy("f",new Class[] { File.class }, client);
	}
	
	/**
	 * Proxies the client to the Tracker interface.
	 * @return Tracker
	 */
	public Tracker getTrackerClient(){
		return (Tracker)XmlRpcProxy.createProxy("t",new Class[] { Tracker.class }, client);
	}
	
	/**
	 * Returns the profile submitted to this XmlRpcConnection instance.
	 * @return ClientProfileInterface
	 */
	public ConnectionProfile getProfile() {
		return profile;
	}

}
