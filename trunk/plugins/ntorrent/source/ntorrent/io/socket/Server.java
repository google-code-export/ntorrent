package ntorrent.io.socket;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import ntorrent.data.Environment;
import ntorrent.locale.ResourcePool;


/**
 * @author  Kim Eik
 */
public class Server extends Thread{
	private static ServerSocket servSocket;
	
	public Server() throws IOException {
		servSocket = new ServerSocket(Environment.getIntSocketPort());
		Logger.global.info(ResourcePool.getString("soopen","exceptions",this));
	}

	public void run() {
		/**
		 * For each incoming connection, thread the connection into
		 * ThreadedClientHandler.
		 */
		do {

			Socket client;
			try {
				client = servSocket.accept();
				ThreadedClientHandler clienthandler = new ThreadedClientHandler(client);
				clienthandler.start();
			} catch (IOException e) {
				Logger.global.log(Level.WARNING, e.getMessage());
			}
		} while (true);
	}
}