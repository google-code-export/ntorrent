package ntorrent.io.socket;


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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import ntorrent.Main;
import ntorrent.locale.ResourcePool;




/**
 * @author  Kim Eik
 */
class ThreadedClientHandler extends Thread {

	private static Socket client;

	private BufferedReader in;

	public ThreadedClientHandler(Socket socket) {
		client = socket;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e) {
			Logger.global.log(Level.WARNING, e.getMessage());;
		}
	}

	public void run() {
		try {
			do {
				try {
					String line = in.readLine();
					if(line != null){
						Main.clientSoConn(line);
					}
				}catch(Exception x){
					Logger.global.log(Level.WARNING, x.getMessage());
				}
			} while (in.ready());
		} catch (IOException e) {
			Logger.global.log(Level.WARNING, e.getMessage());
		} finally {
			try {
				if (client != null) {
					Logger.global.info(ResourcePool.getString("soclose","exceptions",this));
					client.close();
				}
			} catch (IOException e) {
				Logger.global.log(Level.WARNING, e.getMessage());
			}
		}
	}
}
