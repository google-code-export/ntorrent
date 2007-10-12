/**
 * 
 */
package org.heldig.ntorrent.event;

import java.io.File;

import org.heldig.ntorrent.gui.profile.ClientProfile;
import org.heldig.ntorrent.gui.torrent.TorrentInfo;
import org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback;

/**
 * @author Kim Eik
 *
 */
public interface ControllerEventListener{
	public void connect(ClientProfile p);
	public void localEvent(TorrentInfo[] t, String actionCommand);
	public void sshCopyEvent(TorrentInfo t, File file);
	public void sshRemoveEvent(TorrentInfo[] t);
	public void viewListEvent(String view);
	public void torrentSelectionEvent(TorrentInfo t);
	public void torrentCommand(String[] hash, String string);
	public void setTorrentPriority(String[] hash, int i);
	public void setFilePriority(String hash, int pri, int[] selectedRows);
	void setTrackerEnabled(String hash, int[] id, boolean b, XmlRpcCallback c);
	void loadTorrent(String url);
	void loadTorrent(File file);
}
