package ntorrent.io.socket;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ntorrent.NtorrentApplication;

import org.apache.log4j.Logger;


/**
 * @author  Kim Eik
 */
public class Server extends Thread{
	private static ServerSocket servSocket;
	
	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(Server.class);
	
	public Server() throws IOException {
		servSocket = new ServerSocket(NtorrentApplication.SETTINGS.getIntSocketPort());
		//Logger.global.info(ResourcePool.getString("soopen","exceptions",this));
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
				log.warn(e.getMessage(),e);
			}
		} while (true);
	}
}