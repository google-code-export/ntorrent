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
import ntorrent.io.Rpc;
import ntorrent.io.RpcConnection;
import ntorrent.model.TorrentPool;

import org.apache.xmlrpc.client.XmlRpcClient;

public class Controller {
	protected static ContentThread mainContentThread;
	protected static StatusThread statusThread;
	protected static TorrentPool torrents;
	protected static MainGui gui = new MainGui();
	protected static Rpc rpc;
	private static RpcConnection conn;
	
	public static void load(String host, String username, String password) throws MalformedURLException{
		conn = new RpcConnection(host);
		conn.setUsername(username);
		conn.setPassword(password);
		//2.Connect to server
		XmlRpcClient client = conn.connect();
		rpc = new Rpc(client);
		torrents = new TorrentPool(rpc,gui.getTorrentTableModel());
		gui.getTorrentTableModel().fillData(torrents);
	}
	
	public static void drawMainGui(){
		//3.Draw gui.
		gui.drawMainWindow();
	}
	
	public static void startThreads(){
		//4.Start threads.
		mainContentThread = new ContentThread();
		statusThread = new StatusThread();
		mainContentThread.run();
	}
	
	public static void changeMainPane(String name){
		torrents.setView(name);
		torrents.update();
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
}