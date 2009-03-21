package ntorrent.io.socket;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import ntorrent.NtorrentApplication;

import org.apache.log4j.Logger;



public class Client {
	
	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(Client.class);
	
	public Client(String[] args) throws IOException {
		Socket link = new Socket(InetAddress.getLocalHost(), NtorrentApplication.SETTINGS.getIntSocketPort());
		PrintWriter out = new PrintWriter(link.getOutputStream(), true);
		for(String s : args){
			out.println(s);
		}
		try {
			if (link != null) {
				link.close();
			}
		} catch (IOException ioEx) {
			log.warn(ioEx.getMessage(), ioEx);
		}
	}
}