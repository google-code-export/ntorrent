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
package org.heldig.ntorrent.io.ssh;

import java.io.File;
import java.io.IOException;

import org.heldig.ntorrent.io.Rpc;
import org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback;

/**
 * @author Kim Eik
 *
 */
public class SshRpc implements Rpc {

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#fileCommand(java.lang.String[], java.lang.String)
	 */
	public void fileCommand(String[] hash, String command) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#getDownloadRate(org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	public void getDownloadRate(XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#getFileList(java.lang.String, org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	public void getFileList(String hash, XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#getPortRange(org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	public void getPortRange(XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#getSystemClientVersion()
	 */
	public String getSystemClientVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#getSystemLibraryVersion()
	 */
	public String getSystemLibraryVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#getTorrentSet(java.lang.String, org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	public void getTorrentSet(String view, XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#getTorrentVariables(java.lang.String, org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	public void getTorrentVariables(String view, XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#getTrackerList(java.lang.String, org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	public void getTrackerList(String hash, XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#getUploadRate(org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	public void getUploadRate(XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#loadTorrent(java.io.File)
	 */
	public void loadTorrent(File torrent) throws IOException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#loadTorrent(java.lang.String)
	 */
	public void loadTorrent(String url) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#setDownloadRate(java.lang.Integer, org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	public void setDownloadRate(Integer i, XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#setFilePriority(java.lang.String, int, int[])
	 */
	public void setFilePriority(String hash, int pri, int[] index) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#setTorrentPriority(java.lang.String[], int)
	 */
	public void setTorrentPriority(String[] hash, int pri) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#setTrackerEnabled(java.lang.String, int[], boolean, org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	public void setTrackerEnabled(String hash, int[] id, boolean b,
			XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#setUploadRate(java.lang.Integer, org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	public void setUploadRate(Integer i, XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

}
