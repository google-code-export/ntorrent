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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ntorrent.gui.MainWindow;
import ntorrent.gui.window.Window;
import ntorrent.io.logging.SystemLog;
import ntorrent.io.settings.Constants;
import ntorrent.io.settings.LocalSettings;
import ntorrent.io.settings.Serializer;
import ntorrent.io.socket.Client;
import ntorrent.io.socket.Server;

public class Main implements Constants {

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

		/** License **/
		System.out.println(messages.getString("lic"));
		
		/** Loading environment **/
		if(!(ntorrent.isDirectory() || ntorrent.mkdir()))
			Logger.global.log(Level.WARNING,messages.getString("ntdir")+ntorrent);
		
		/** Load logging **/
		//log = new SystemLog();

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
			System.exit(0);
		}
		
		/** Draw Gui **/
		Window main = new MainWindow();
		main.drawWindow();
		
	}
	
	public static void clientSoConn(String line){
		
	}

}
