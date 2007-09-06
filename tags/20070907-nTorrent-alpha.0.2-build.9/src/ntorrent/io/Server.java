package ntorrent.io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ntorrent.controller.Controller;
import ntorrent.settings.Constants;

public class Server extends Thread{
	private static ServerSocket servSocket;
	
	public Server() throws IOException {
		servSocket = new ServerSocket(Constants.getCommPort());
		Controller.writeToLog("Setting up socket server.");
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
				Controller.writeToLog(e);
			}
		} while (true);
	}
}