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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import ntorrent.io.logging.SystemLog;
import ntorrent.io.settings.LocalSettings;
import ntorrent.io.settings.Serializer;
import ntorrent.io.socket.Client;
import ntorrent.io.socket.Server;

public class Main {
	
	/** the users home dir **/
	public static File home = new File(System.getProperty("user.home"));
	
	/** the users ntorrent dir **/
	public static File ntorrent = new File(home,".ntorrent");
	
	/** the users language **/
	public static String language = System.getProperty("user.language");
	
	/** the users country **/
	public static String country = System.getProperty("user.country");
	
	/** the java vm version **/
	public static String javaSpec = System.getProperty("java.specification.version");

	/** Locale specification **/
	public static Locale locale;
	
	/** Translations **/
	public static ResourceBundle messages;
	
	/** Local settings **/
	public static LocalSettings settings = new LocalSettings();
	
	/** Logging system **/
	public static SystemLog log;
	
	/**
	 * The main program.
	 * @param args list of torrent files
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws IllegalArgumentException, IOException, IllegalAccessException, ClassNotFoundException {
				
		/** Load locale **/
		Logger.global.log(Level.INFO,"Locale: "+language+"_"+country);
		locale = new Locale(language,country);
		try{
			messages = ResourceBundle.getBundle("ntorrent", locale);
		}catch (MissingResourceException e) {
			Logger.global.log(Level.WARNING,e.getMessage());
		}
		
		/** License **/
		System.out.println(messages.getString("lic"));
		
		/** Loading environment **/
		if(!(ntorrent.isDirectory() || ntorrent.mkdir()))
			Logger.global.log(Level.WARNING,messages.getString("ntdir")+ntorrent);
		
		/** Load logging **/
		log = new SystemLog();

		/** Load settings**/
		try{
		settings = (LocalSettings)Serializer.deserialize(LocalSettings.class);
		} catch(FileNotFoundException e){
			Logger.global.log(Level.WARNING,messages.getString("F404")+e.getMessage());
			Serializer.serialize(settings);
		}
		
		/** Start socket server **/
		try{
			new Server().start();
		}catch(IOException e){
			Logger.global.info(e.getMessage());
			new Client(args);
		}
		
		/** Draw Gui **/
	}
	
	public static void clientSoConn(String line){
		
	}

}
