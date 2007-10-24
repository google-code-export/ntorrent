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

	@Override
	public void getDownloadRate(XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getFileList(String hash, XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getPortRange(XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSystemClientVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSystemLibraryVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getTorrentSet(String view, XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getTorrentVariables(String view, XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getTrackerList(String hash, XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getUploadRate(XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadTorrent(File torrent) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadTorrent(String url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDownloadRate(Integer i, XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilePriority(String hash, int pri, int[] index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabel(String[] hash, String label, XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTorrentPriority(String[] hash, int pri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTrackerEnabled(String hash, int[] id, boolean b,
			XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUploadRate(Integer i, XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void torrentCommand(String[] hash, String command) {
		// TODO Auto-generated method stub
		
	}



}
