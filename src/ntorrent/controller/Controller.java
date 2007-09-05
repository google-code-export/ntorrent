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


package ntorrent.controller;

import java.net.MalformedURLException;

import ntorrent.controller.threads.ContentThread;
import ntorrent.controller.threads.StatusThread;
import ntorrent.gui.MainGui;
import ntorrent.gui.dialogues.PromptEnv;
import ntorrent.gui.elements.ProcessTrayIcon;
import ntorrent.io.Rpc;
import ntorrent.io.RpcConnection;
import ntorrent.model.TorrentPool;
import ntorrent.settings.Constants;
import ntorrent.settings.ProfileSettings;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;


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
	
	public static void load(String host, String username, String password) throws MalformedURLException, XmlRpcException{
		writeToLog("Connecting.");
		conn = new RpcConnection(host);
		conn.setUsername(username);
		conn.setPassword(password);
		//2.Connect to server
		XmlRpcClient client = conn.connect();
		rpc = new Rpc(client);
		torrents = new TorrentPool(rpc,gui.getTorrentTableModel());
		gui.getTorrentTableModel().fillData(torrents);
		gui.getTorrentTableModel().fireTableDataChanged();
		gui.getViewTab().getViewPane().setEnabled(true);
		startThreads();
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
	
	public static Rpc getRpc() {
		return rpc;
	}
	
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
		gui.writeToLog(msg);
	}
	
	public static void writeToLog(Exception x){
		writeToLog(x.getMessage());
		for(StackTraceElement s : x.getStackTrace())
			writeToLog("Line: "+s.getLineNumber()+"\t"+s.getFileName());
	}
}