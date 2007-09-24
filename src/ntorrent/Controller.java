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


package ntorrent;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;

import ntorrent.gui.MainGui;
import ntorrent.gui.dialogue.PromptEnv;
import ntorrent.gui.tray.ProcessTrayIcon;
import ntorrent.io.xmlrpc.Rpc;
import ntorrent.io.xmlrpc.RpcConnection;
import ntorrent.io.xmlrpc.RpcQueue;
import ntorrent.model.TorrentPool;
import ntorrent.settings.Constants;
import ntorrent.settings.ProfileSettings;
import ntorrent.threads.ContentThread;
import ntorrent.threads.StatusThread;

import org.apache.xmlrpc.XmlRpcException;


public class Controller {
	protected static Thread mainContentThread;
	protected static Thread statusThread;
	protected static Thread torrentThread;
	protected static TorrentPool torrents;
	protected static MainGui gui = new MainGui();
	protected static Rpc rpc;
	private static RpcConnection conn;
	private static ProfileSettings profile = new ProfileSettings();
	private static ProcessTrayIcon trayIcon;
	private static String[] filesToLoad = {};
	
	public static void load(String host, String username, String password) throws MalformedURLException, XmlRpcException{
		writeToLog("Connecting.");
		conn = new RpcConnection(host);
		conn.setUsername(username);
		conn.setPassword(password);
		//2.Connect to server
		RpcQueue client = conn.connect();
		rpc = new Rpc(client);
		torrents = new TorrentPool(rpc,gui.getTorrentTableModel());
		gui.getTorrentTableModel().fillData(torrents);
		gui.getTorrentTableModel().fireTableDataChanged();
		gui.getViewTab().getViewPane().setEnabled(true);
		startThreads();
		loadStartupFiles();
	}
	
	public static void drawMainGui(){
		writeToLog(Constants.getReleaseName());
		writeToLog("Drawing gui");
		//3.Draw gui.
		gui.drawMainWindow();
		gui.getViewTab().getViewPane().setEnabled(false);
		trayIcon = new ProcessTrayIcon();
		PromptEnv env = new PromptEnv(Controller.getGui().getRootWin());
		env.setHost(profile.getHost());
		env.setUsername(profile.getUsername());
		env.drawWindow();
	}
	
	private static void startThreads(){
		writeToLog("Starting threads.");
		//4.Start threads.
		mainContentThread = new Thread(new ContentThread());
		statusThread = new Thread(new StatusThread());
		mainContentThread.start();
		statusThread.start();
	}
	
	public static void changeMainPane(String name){
		torrents.setView(name);
		mainContentThread.interrupt();
	}
	
	public static MainGui getGui() {
		return gui;
	}
	
	/*public static Rpc getRpc() {
		return rpc;
	}*/
	
	public static TorrentPool getTorrents() {
		return torrents;
	}
	
	public static ProfileSettings getProfile() {
		return profile;
	}
	
	public static ProcessTrayIcon getTrayIcon() {
		return trayIcon;
	}
	
	public static void writeToLog(String msg){
		System.out.println(msg);
		gui.writeToLog(msg);
	}
	
	public static void writeToLog(Throwable x){
		x.printStackTrace();
		writeToLog(x.getMessage());
		for(StackTraceElement s : x.getStackTrace())
			writeToLog("Line: "+s.getLineNumber()+"\t"+s.getFileName());
	}
	
	public static boolean loadTorrent(String url){
		if(rpc != null)
			try {
				rpc.loadTorrent(url);
				return true;
			} catch (XmlRpcException e) {
				writeToLog(e);
			}
		return false;
	}
	
	public static boolean loadTorrent(File file) throws IOException, XmlRpcException{
		if(rpc != null){
			rpc.loadTorrent(file);
			return true;
		}
		return false;
	}

	public static Vector<Object>[] getFileList(String hash) {
		try {
			return rpc.getFileList(hash);
		} catch (XmlRpcException e) {
			writeToLog(e);
		}
		return null;
	}

	public static void setStartupFiles(String[] args) {
		filesToLoad = args;
	}
	
	private static void loadStartupFiles(){
			try {
				for(String file: filesToLoad)
					loadTorrent(new File(file));
			} catch (Exception x){
				writeToLog(x);
			}
	}
	
}