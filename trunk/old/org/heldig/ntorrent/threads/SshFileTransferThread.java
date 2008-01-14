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

import org.heldig.ntorrent.gui.torrent.TorrentInfo;
import org.heldig.ntorrent.gui.window.FileTransferGui;

import com.sshtools.j2ssh.SftpClient;

/**
 * @author Kim Eik
 *
 */
public class SshFileTransferThread extends Thread{

	SftpClient sftp;
	String remoteFile;
	File localDir;
	String fileName;
	boolean isDir;
	
	public SshFileTransferThread(SftpClient sftp, TorrentInfo torrent, File localDir) throws IOException {
		this.sftp = sftp;
		this.remoteFile = torrent.getFilePath();
		this.fileName = torrent.getFilename();
		this.localDir = localDir;
		this.isDir = torrent.getNumFiles() > 1;
		this.start();
	}
	
	
	public void run() {
		try {
			FileTransferGui progress = new FileTransferGui(remoteFile);
			if(!isDir)
				sftp.get(remoteFile,localDir.getAbsolutePath()+"/"+fileName,progress);
			else
				sftp.copyRemoteDirectory(remoteFile, localDir.getAbsolutePath(), true, false, true ,progress);
		} catch (Exception x){
			x.printStackTrace();
		}
	}
}
