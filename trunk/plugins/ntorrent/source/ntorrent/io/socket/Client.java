package ntorrent.io.socket;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import ntorrent.Main;



public class Client {
	public Client(String[] args) throws IOException {
		Socket link = new Socket(InetAddress.getLocalHost(), Main.getIntSocketPort());
		PrintWriter out = new PrintWriter(link.getOutputStream(), true);
		for(String s : args){
			File f = new File(s);
			if(f.exists() && f.isFile())
				out.println(s);
		}
		try {
			if (link != null) {
				link.close();
			}
		} catch (IOException ioEx) {
			Logger.global.log(Level.WARNING, ioEx.getMessage());
		}
	}
}