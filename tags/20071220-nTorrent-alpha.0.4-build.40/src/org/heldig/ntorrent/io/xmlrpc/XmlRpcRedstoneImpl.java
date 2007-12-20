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

	
	public void getDownloadRate(XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	
	public void getFileList(String hash, XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	
	public void getPortRange(XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	
	public String getSystemClientVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getSystemLibraryVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void getTorrentSet(String view, XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	
	public void getTorrentVariables(String view, XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	
	public void getTrackerList(String hash, XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	
	public void getUploadRate(XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	
	public void loadTorrent(File torrent) throws IOException {
		// TODO Auto-generated method stub
		
	}

	
	public void loadTorrent(String url) {
		// TODO Auto-generated method stub
		
	}

	
	public void setDownloadRate(Integer i, XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	
	public void setFilePriority(String hash, int pri, int[] index) {
		// TODO Auto-generated method stub
		
	}

	
	public void setLabel(String[] hash, String label, XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	
	public void setTorrentPriority(String[] hash, int pri) {
		// TODO Auto-generated method stub
		
	}

	
	public void setTrackerEnabled(String hash, int[] id, boolean b,
			XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	
	public void setUploadRate(Integer i, XmlRpcCallback c) {
		// TODO Auto-generated method stub
		
	}

	
	public void torrentCommand(String[] hash, String command) {
		// TODO Auto-generated method stub
		
	}



}
