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

package org.heldig.ntorrent.socket;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.heldig.ntorrent.NTorrent;



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
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			do {
				try {
					String line = in.readLine();
					if(line != null){
						if(!NTorrent.loadTorrent(new File(line)))
							System.out.println("loadTorrent returned false.");
					}
				}catch(Exception x){
					x.printStackTrace();
				}
			} while (in.ready());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (client != null) {
					System.out.println("Closing client socket.");
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
