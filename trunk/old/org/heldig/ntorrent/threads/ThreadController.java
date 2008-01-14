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
package org.heldig.ntorrent.threads;

import java.io.File;
import java.io.IOException;

import org.heldig.ntorrent.gui.StatusBarComponent;
import org.heldig.ntorrent.gui.label.LabelListModel;
import org.heldig.ntorrent.gui.torrent.TorrentInfo;
import org.heldig.ntorrent.gui.torrent.TorrentPool;
import org.heldig.ntorrent.io.Rpc;

import com.sshtools.j2ssh.SftpClient;

/**
 * @author  Kim Eik
 */
public class ThreadController{
	private Thread mainContentThread;
	private StatusBarThread throttleThread;

	public void startThreads(Rpc r, StatusBarComponent c, LabelListModel l, TorrentPool tp){
		mainContentThread = new ContentThread(r,c,l,tp);
		throttleThread = new StatusBarThread(r,c);
		tp.setMcThread(mainContentThread);
		System.out.println("Starting threads.");
		mainContentThread.start();
		throttleThread.start();
	}	
	/**
	 * @return
	 */
	public Thread getMainContentThread() {
		return mainContentThread;
	}
	

	public void startFileTransfer(SftpClient sftp, TorrentInfo torrent, File selectedFile) {
		try {
			new SshFileTransferThread(sftp,torrent,selectedFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
