package ntorrent;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import ntorrent.data.Environment;
import ntorrent.gui.MainWindow;
import ntorrent.io.logging.SystemLog;
import ntorrent.io.rtorrent.Global;
import ntorrent.io.socket.Client;
import ntorrent.io.socket.Server;
import ntorrent.locale.ResourcePool;
import ntorrent.profile.model.ClientProfileInterface;
import ntorrent.profile.model.ClientProfileListModel;
import ntorrent.tools.Serializer;

import org.java.plugin.Plugin;

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

public class Main extends Plugin {
		
	/** GUI **/
	public static MainWindow main;
	
	/** Sessions **/
	public static Vector<Session> sessions = new Vector<Session>();
	
	//private SystemLog nlog;
	
	public Main(){
		
		ResourcePool.setLocale(Environment.getUserLanguage(),Environment.getUserCountry());
		
		/** License **/
		System.out.println(ResourcePool.getString("lic",this));
		
		/** Loading environment **/
		File ntorrent = Environment.getNtorrentDir();
		if(!(ntorrent.isDirectory() || ntorrent.mkdir()))
			Logger.global.log(Level.WARNING,"Could not create directory:"+ntorrent);
		
		try{
			/** Load logging **/
			if(Environment.isLogging())
				/*nlog = */new SystemLog();
		}catch(Exception x){
			x.printStackTrace();
		}
		
		
		/** UImanager, set look and feel **/
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/** create gui **/
		main = new MainWindow();
		
		/** restore plugins **/
		//errors with plugin framework if this isn't threaded
		new Thread(){
			public void run(){
				main.getJpf().restore();
			}
		}.start();
	}
	
	public static Session newSession(){
		Session s = new Session(main);
		sessions.add(s);
		return s;
	}
	
	public static Session newSession(ClientProfileInterface p){
		Session s = new Session(main,p);
		sessions.add(s);
		return s;
	}
	
	/**
	 * Sends a raw byte for byte torrent file to a session and starts the torrent.
	 * @param torrent
	 * @param target
	 */
	private static void sendTorrentFileToSession(File torrent,Session target){
		if(target != null && torrent.exists() && torrent.isFile()){
			Global global = target.getConnection().getGlobalClient();
			try {
				byte[] source = new byte[(int)torrent.length()];
				FileInputStream reader = new FileInputStream(torrent);
				reader.read(source, 0, source.length);					
				global.load_raw_start(source);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Sends a string representing a torrent url to a session and calls the load_start method.
	 * @param url
	 * @param target
	 */
	private static void sendTorrentUrlToSession(String url, Session target){
		if(url != null && target != null){
			Global global = target.getConnection().getGlobalClient();
			global.load_start(url);
		}
	}
	
	/**
	 * Handles the dialogue between the user when adding a torrent.
	 * Three outcomes are possible. there is only one session, therefore
	 * automatically loaded to that session. There can be several sessions,
	 * then the user must choose which to add the torrent to.
	 * and third, no sessions are connected, in that case return false.
	 * @param line
	 * @return
	 */
	public static boolean addTorrentToSession(final String line){
		File f = new File(line);
		Logger.global.info("[clientSoConn] - "+line);
		Vector<Session> sessionList = new Vector<Session>();
			for(Session s : sessions){
				if(s.isConnected())
					sessionList.add(s);
			}
		
		Session target = null;
		if(sessionList.size() > 1){
			target = (Session) JOptionPane.showInputDialog(
					main,
					null, 
					null, 
					JOptionPane.PLAIN_MESSAGE,
					null, 
					sessionList.toArray(),
					null
					);
		}else if(sessionList.size() == 1){
			target = sessionList.get(0);
		}
		
		if(f.exists() && f.isFile()){
			sendTorrentFileToSession(f, target);
		}else{
			sendTorrentUrlToSession(line, target);
		}
		
		return target != null;
	}
			
	/**
	 * The first method being called when adding a torrent to the session.
	 * The string parameter can be either an torrent url, or a file path to a torrent.
	 * @param line
	 */
	public static void clientSoConn(final String line){
		if(!addTorrentToSession(line)){
			JOptionPane.showMessageDialog(main, "The torrent "+line+" will be added once you connect.");
			//TODO redo this to instead checking every few seconds. listen for session list changes.
			new Thread(){
				@Override
				public void run() {
					while(!addTorrentToSession(line)){
						try {
							sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}.start();
		}
		

		
	}
	
	public static Vector<Session> getSessions() {
		return sessions;
	}
	
	public static MainWindow getMainWindow() {
		return main;
	}

	@Override
	protected void doStart() throws Exception {
				
		/** autoconnect **/
		try{
			for(ClientProfileInterface p : Serializer.deserialize(ClientProfileListModel.class, Environment.getNtorrentDir()))
				if(p.isAutoConnect())
					newSession(p);
		}catch(Exception e){
			Logger.global.log(Level.WARNING,e.getMessage(),e);
		}
		
		if(!(sessions.size() > 0)){
			newSession();
		}
		
		/** Start socket server **/
		try{
			new Server().start();
			new Thread(){
				public void run(){
					for(String line : Environment.getArgs()){
						clientSoConn(line);
						break; //break since its not possible to add torrent without being connected, just show message.
					}
				}
			}.start();
		}catch(IOException e){
			Logger.global.info(e.getMessage());
			try {
				new Client(Environment.getArgs());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.exit(0);
		}
		
		/** Draw Gui **/
		main.drawWindow();
	}

	@Override
	protected void doStop() throws Exception {
		//do nothing
	}
}
