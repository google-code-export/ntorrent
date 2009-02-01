package tests.ntorrent.io.rtorrent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.SocketFactory;

public class SocketConnection extends RtorrentService {
	
	
	public static void main(String[] args) throws IOException {
		ServerSocket socket = new ServerSocket(5000);
		while(true){
			final Socket client = socket.accept();
			new Thread(){
				public void run() {
					try {
						PrintWriter out = new PrintWriter(client.getOutputStream(), true);
						BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
						String inputLine, outputLine;
						while(client.isConnected()){
							while ((inputLine = in.readLine()) != null) {
								System.out.println(inputLine);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.run();
		}
	}
}
