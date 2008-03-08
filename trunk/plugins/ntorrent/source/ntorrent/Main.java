package ntorrent;


import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ntorrent.data.Environment;
import ntorrent.gui.MainWindow;
import ntorrent.io.logging.SystemLog;
import ntorrent.io.socket.Client;
import ntorrent.io.socket.Server;
import ntorrent.locale.ResourcePool;
import ntorrent.profile.model.ClientProfileInterface;
import ntorrent.profile.model.ClientProfileListModel;

import org.java.plugin.Plugin;
import org.java.plugin.PluginManager;
import org.java.plugin.boot.Application;
import org.java.plugin.boot.ApplicationPlugin;
import org.java.plugin.util.ExtendedProperties;

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
	
	/** Logging system **/
	private static SystemLog log;
	
	/** GUI **/
	public static MainWindow main;
	
	/** Sessions **/
	public static Vector<Session> sessions = new Vector<Session>();
	
	private SystemLog nlog;
	
	public Main(){
		
		ResourcePool.setLocale(Environment.getUserLanguage(),Environment.getUserCountry());
		
		/** License **/
		System.out.println(ResourcePool.getString("lic","locale",this));
		
		/** Loading environment **/
		File ntorrent = Environment.getNtorrentDir();
		if(!(ntorrent.isDirectory() || ntorrent.mkdir()))
			Logger.global.log(Level.WARNING,ResourcePool.getString("ntdir","exceptions",this)+ntorrent);
		
		/** Load logging **/
		//nlog = new SystemLog();

		/** Load settings**/
		//try{
			//settings = (LocalSettings)Serializer.deserialize(LocalSettings.class,ntorrent);
		//} catch(FileNotFoundException e){
			//Logger.global.log(Level.WARNING,messages.getString("F404")+e.getMessage());
			//Serializer.serialize(settings,ntorrent);
		//}
		
		/** Start socket server **/
		try{
			new Server().start();
		}catch(IOException e){
			Logger.global.info(e.getMessage());
			try {
				new Client(Environment.getArgs());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.exit(0);
		}
		
		/** UImanager, set look and feel **/
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/** create gui **/
		main = new MainWindow();
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
			
	public static void clientSoConn(String line){
		File f = new File(line);
		if(f.isFile())
			System.out.println("load this as a file: "+line);
		else
			System.out.println("load this as a url: "+line);
		
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
			for(ClientProfileInterface p : ClientProfileListModel.Deserialize())
				if(p.isAutoConnect())
					newSession(p);
		} catch(FileNotFoundException e){
			new ClientProfileListModel().Serialize();
		}catch(Exception e){
			Logger.global.log(Level.WARNING,e.getMessage(),e);
		}
		
		if(!(sessions.size() > 0)){
			newSession();
		}
		
		/** Draw Gui **/
		main.drawWindow();
	}

	@Override
	protected void doStop() throws Exception {
		//do nothing
	}
}
