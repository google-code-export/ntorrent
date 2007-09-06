package ntorrent.io;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.xmlrpc.XmlRpcException;

import ntorrent.controller.Controller;

class ThreadedClientHandler extends Thread {

	private static Socket client;

	private BufferedReader in;

	public ThreadedClientHandler(Socket socket) {
		client = socket;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e) {
			Controller.writeToLog(e);
		}
	}

	public void run() {
		try {
			do {
				try {
					String line = in.readLine();
					Controller.writeToLog("Client socket reported: "+line);
					if(!Controller.loadTorrent(new File(line)))
						Controller.writeToLog("loadTorrent returned false.");
				} catch (XmlRpcException e) {
					Controller.writeToLog(e);
				} catch(NullPointerException e){
					Controller.writeToLog(e);
				}
			} while (in.ready());
		} catch (IOException e) {
			Controller.writeToLog(e);
		} finally {
			try {
				if (client != null) {
					Controller.writeToLog("Closing client socket.");
					client.close();
				}
			} catch (IOException e) {
				Controller.writeToLog(e);
			}
		}
	}
}
