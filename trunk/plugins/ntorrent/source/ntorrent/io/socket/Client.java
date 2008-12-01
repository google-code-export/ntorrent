package ntorrent.io.socket;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.log4j.Logger;

import ntorrent.data.Environment;



public class Client {
	
	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(Client.class);
	
	public Client(String[] args) throws IOException {
		Socket link = new Socket(InetAddress.getLocalHost(), Environment.getIntSocketPort());
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