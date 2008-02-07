package ntorrent.io.socket;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.java.plugin.boot.Application;

import ntorrent.Main;
import ntorrent.env.Environment;


/**
 * @author  Kim Eik
 */
public class Server extends Thread{
	private static ServerSocket servSocket;
	
	public Server() throws IOException {
		servSocket = new ServerSocket(Environment.getIntSocketPort());
		Logger.global.info(Environment.getString("soopen"));
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