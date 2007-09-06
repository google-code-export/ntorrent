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

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;

import javax.swing.UIManager;

import ntorrent.controller.Controller;
import ntorrent.io.Client;
import ntorrent.io.Server;
import ntorrent.settings.Constants;

public class NTorrent{
	public static void init(){

	}
	
	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException {
		System.out.println(Constants.getLicense());
		//connect to existing process
		try {
			new Client(args);
		} catch(ConnectException x) {
			//start process
			try {
		        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				Controller.drawMainGui();
				Controller.setStartupFiles(args);
		    } 
		    catch (Exception e) {
		    	e.printStackTrace();
		    }
			//start socket server
			try {
				new Server().start();
			} catch (IOException e) {
				Controller.writeToLog(e);
			}
		} catch (IOException x) {
			x.printStackTrace();
		}	
	}
}
