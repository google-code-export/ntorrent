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


package org.heldig.ntorrent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import org.heldig.ntorrent.event.ControllerEventListener;
import org.heldig.ntorrent.gui.Window;
import org.heldig.ntorrent.gui.profile.ClientProfile;
import org.heldig.ntorrent.gui.profile.ClientProfile.Protocol;
import org.heldig.ntorrent.gui.torrent.TorrentInfo;
import org.heldig.ntorrent.gui.torrent.TorrentPool;
import org.heldig.ntorrent.io.Rpc;
import org.heldig.ntorrent.io.RpcConnection;
import org.heldig.ntorrent.io.xmlrpc.XmlRpc;
import org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback;
import org.heldig.ntorrent.io.xmlrpc.http.XmlRpcConnection;
import org.heldig.ntorrent.io.xmlrpc.local.LocalConnection;
import org.heldig.ntorrent.io.xmlrpc.ssh.SshConnection;
import org.heldig.ntorrent.language.Language;
import org.heldig.ntorrent.settings.Constants;
import org.heldig.ntorrent.threads.ThreadController;

import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;



/**
 * @author   Kim Eik
 */
public class Controller implements ControllerEventListener, ActionListener{
	
	private String[] filesToLoad;
	private final GUIController GC = new GUIController(this);
	private final ThreadController TC = new ThreadController();
	private TorrentPool torrents = GC.getTableModel().getData();
	private Rpc rpc;
	private RpcConnection rpcLink;
	private SshClient ssh;
	private SftpClient sftp;
	public Protocol protocol;
	
	public Controller(String[] args, Window root){
		filesToLoad = args;
		root.setContentPane(GC.getContentPane());
		torrents.setMcThread(TC.getMainContentThread());

	}
	
	public final void connect(ClientProfile p){
		try{
			protocol = p.getProt();
			switch(protocol){
				case HTTP:
					rpcLink = new XmlRpcConnection(p);
					break;
				case SSH:
					rpcLink = new SshConnection(p);
					break;
				case LOCAL:
					rpcLink = new LocalConnection(p);
					break;
			}
		
			//2.Connect to server
			rpc = new XmlRpc(rpcLink.connect());
			if(rpcLink instanceof SshConnection){
				ssh = ((SshConnection)rpcLink).getSsh();
				sftp = ssh.openSftpClient();
			}
		}catch(Exception x){
			GC.showError(x);
			x.printStackTrace();
			System.exit(x.hashCode());
		}
		System.out.println(Constants.getReleaseName());
		
		//load the startup files
		loadStartupFiles(filesToLoad);
		//start the threads
		TC.startThreads(rpc, GC.getStatusBarComponent(),GC.getLabelList().getModel(), torrents);
	}
	
	private final void loadStartupFiles(String[] filesToLoad){
			try {
				for(String file: filesToLoad)
					if(rpc != null)
						rpc.loadTorrent(new File(file));
			} catch (Exception x){
				x.printStackTrace();
			}
	}
	
	private final void deleteFile(File file){
		while(!file.delete())
			for(File f: file.listFiles()){
				deleteFile(f);
			}
	}
	
	@Override
	public void viewListEvent(String view) {
		torrents.setView(view);
	}

	@Override
	public void localEvent(TorrentInfo[] tf, String actionCommand) {
		if(protocol.equals(Protocol.LOCAL)){
			Language l = Language.getFromString(actionCommand);
			for(TorrentInfo t : tf)
				switch(l){
				case Local_Menu_open_file:
					try {
						NTorrent.settings.runProgram(t);
					} catch (IOException z) {
						z.printStackTrace();
					}
					break;
				case Local_Menu_remove_data:
					File f = new File(t.getFilePath());
					deleteFile(f);
					break;
				}
		}
	}

	@Override
	public void sshCopyEvent(TorrentInfo t, File path) {
		if(protocol.equals(Protocol.SSH))
			TC.startFileTransfer(sftp, t, path);
	}

	@Override
	public void sshRemoveEvent(TorrentInfo[] tf) {
		if(protocol.equals(Protocol.SSH))
			for(TorrentInfo t : tf){
				try {
					sftp.rm(t.getFilePath(),true,true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
	}

	@Override
	public void torrentSelectionEvent(TorrentInfo t) {
		GC.fileTab.getInfoPanel().setInfo(t);
		rpc.getFileList(t.getHash(), GC.fileTab.getFileList());
		rpc.getTrackerList(t.getHash(), GC.fileTab.getTrackerList(t.getHash()));
	}
	
	public void labelSelectionEvent(String label){
		torrents.setLabelView(label);
	}

	@Override
	public void torrentCommand(String[] hash, String command) {
		rpc.torrentCommand(hash, command);
	}

	@Override
	public void setTorrentPriority(String[] hash, int i) {
		rpc.setTorrentPriority(hash, i);
	}

	@Override
	public void setFilePriority(String hash, int pri, int[] index) {
		rpc.setFilePriority(hash, pri, index);
	}

	@Override
	public void setTrackerEnabled(String hash, int[] id, boolean b,
			XmlRpcCallback c) {
		rpc.setTrackerEnabled(hash,id, b, c);
	}

	@Override
	public void loadTorrent(String url) {
		rpc.loadTorrent(url);
	}

	@Override
	public boolean loadTorrent(File file) {
		try {
			rpc.loadTorrent(file);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	GUIController getGC() {
		return GC;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
			switch (Language.getFromString(e.getActionCommand())){
				case Menu_File_start_all:
					torrentCommand(torrents.getHashList(), "d.start");
					break;
				case Menu_File_stop_all:
					torrentCommand(torrents.getHashList(), "d.stop");
					break;
			}
	}

	@Override
	public Protocol getProtocol() {
		return protocol;
	}

	@Override
	public void setLabel(String[] hash, String label) {
		rpc.setLabel(hash, label, null);
	}
}