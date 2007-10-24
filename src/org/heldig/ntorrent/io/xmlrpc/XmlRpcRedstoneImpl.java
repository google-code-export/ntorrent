/**
 * 
 */
package org.heldig.ntorrent.io.xmlrpc;

import java.io.File;
import java.io.IOException;

import org.heldig.ntorrent.io.Rpc;

/**
 * @author netbrain
 *
 */
public class XmlRpcRedstoneImpl implements Rpc {

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#getDownloadRate(org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	@Override
	public void getDownloadRate(XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#getFileList(java.lang.String, org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	@Override
	public void getFileList(String hash, XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#getPortRange(org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	@Override
	public void getPortRange(XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#getSystemClientVersion()
	 */
	@Override
	public String getSystemClientVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#getSystemLibraryVersion()
	 */
	@Override
	public String getSystemLibraryVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#getTorrentSet(java.lang.String, org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	@Override
	public void getTorrentSet(String view, XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#getTorrentVariables(java.lang.String, org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	@Override
	public void getTorrentVariables(String view, XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#getTrackerList(java.lang.String, org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	@Override
	public void getTrackerList(String hash, XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#getUploadRate(org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	@Override
	public void getUploadRate(XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#loadTorrent(java.io.File)
	 */
	@Override
	public void loadTorrent(File torrent) throws IOException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#loadTorrent(java.lang.String)
	 */
	@Override
	public void loadTorrent(String url) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#setDownloadRate(java.lang.Integer, org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	@Override
	public void setDownloadRate(Integer i, XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#setFilePriority(java.lang.String, int, int[])
	 */
	@Override
	public void setFilePriority(String hash, int pri, int[] index) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#setLabel(java.lang.String[], java.lang.String, org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	@Override
	public void setLabel(String[] hash, String label, XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#setTorrentPriority(java.lang.String[], int)
	 */
	@Override
	public void setTorrentPriority(String[] hash, int pri) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#setTrackerEnabled(java.lang.String, int[], boolean, org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	@Override
	public void setTrackerEnabled(String hash, int[] id, boolean b,
			XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#setUploadRate(java.lang.Integer, org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback)
	 */
	@Override
	public void setUploadRate(Integer i, XmlRpcCallback c) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.heldig.ntorrent.io.Rpc#torrentCommand(java.lang.String[], java.lang.String)
	 */
	@Override
	public void torrentCommand(String[] hash, String command) {
		// TODO Auto-generated method stub

	}

}
