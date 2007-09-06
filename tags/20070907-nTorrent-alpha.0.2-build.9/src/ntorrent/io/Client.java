package ntorrent.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import ntorrent.settings.Constants;

public class Client {
	public Client(String[] args) throws IOException {
		Socket link = new Socket(InetAddress.getLocalHost(), Constants.getCommPort());
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
			ioEx.printStackTrace();
		}
	}
}