package ntorrent;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLDocument.Iterator;

import ntorrent.env.Environment;
import ntorrent.gui.MainWindow;
import ntorrent.gui.TabExtension;
import ntorrent.gui.menubar.MenuBarExtension;
import ntorrent.io.logging.SystemLog;
import ntorrent.io.settings.LocalSettings;
import ntorrent.io.socket.Client;
import ntorrent.io.socket.Server;
import ntorrent.profile.model.ClientProfileInterface;
import ntorrent.profile.model.ClientProfileListModel;

import org.java.plugin.ObjectFactory;
import org.java.plugin.PluginManager;
import org.java.plugin.boot.Application;
import org.java.plugin.boot.ApplicationPlugin;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.Extension.Parameter;
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

public class Main extends ApplicationPlugin implements Application {
	
	private static PluginManager pluginManager = ObjectFactory.newInstance().createManager();
	
	/** GUI **/
	public static MainWindow main;
	
	/** Sessions **/
	public static Vector<Session> sessions = new Vector<Session>();
	
	/** ntorrent dir **/
	File ntorrent;
	
	@Override
	protected Application initApplication(ExtendedProperties properties, String[] args)
			throws Exception {
		
		ntorrent = new File(properties.getProperty("userNtorrentDir"));
		
		Environment.setHome(new File(properties.getProperty("userHomeDir")));
		
		Environment.setNtorrentDir(ntorrent);
		
		Environment.setIntSocketPort(Integer.parseInt(properties.getProperty("internalCommPort")));
		
		Environment.setLocale(new Locale(properties.getProperty("userLanguage"),
				properties.getProperty("userCountry")));
		
		
		Environment.setMessages(ResourceBundle.getBundle("ntorrent", Environment.getLocale()));
		
		/** License **/
		System.out.println(Environment.getString("lic"));
		
		/** Loading environment **/
		if(!(ntorrent.isDirectory() || ntorrent.mkdir()))
			Logger.global.log(Level.WARNING,Environment.getString("ntdir")+ntorrent);
		
		/** Load logging **/
		//log = new SystemLog();

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
			new Client(args);
			System.exit(0);
		}
		
		/** UImanager, set look and feel **/
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		/** create gui **/
		main = new MainWindow();
		
		
		/** adding plugins to menubar **/
		ExtensionPoint toolExtPoint =
			getManager().getRegistry().getExtensionPoint(
				getDescriptor().getId(), "MenuBarExtension");

		for(Extension x : toolExtPoint.getAvailableExtensions()){
				getManager().activatePlugin(x.getId());
				ClassLoader classLoader = getManager().getPluginClassLoader(x.getDeclaringPluginDescriptor());
				Class cls = classLoader.loadClass(x.getParameter("class").valueAsString());
				MenuBarExtension t = (MenuBarExtension) cls.newInstance();
				t.init(main.getJMenuBar());
		}
		
		
		
		return this;
	}

	@Override
	protected void doStart() throws Exception {
		//do nothing
	}

	@Override
	protected void doStop() throws Exception {
		//do nothing
	}

	public void startApplication() throws Exception {

		/** autoconnect **/
		try{
			
			for(ClientProfileInterface p : ClientProfileListModel.Deserialize())
				if(p.isAutoConnect())
					newSession(p);
		} catch(FileNotFoundException e){
			new ClientProfileListModel().Serialize();
		}catch(InvalidClassException e){
			Logger.global.log(Level.WARNING,e.getMessage(),e);
		}
		
		if(!(sessions.size() > 0)){
			newSession();
		}
		
		/** Draw Gui **/
		main.drawWindow();
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
		System.out.println(line);
	}
	
	public static Vector<Session> getSessions() {
		return sessions;
	}
	
	public static MainWindow getMainWindow() {
		return main;
	}
}
