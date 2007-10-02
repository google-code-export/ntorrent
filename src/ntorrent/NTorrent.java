package ntorrent;


import java.io.File;
import java.io.IOException;
import java.net.BindException;

import javax.swing.UIManager;

import ntorrent.io.socket.Client;
import ntorrent.io.socket.Server;
import ntorrent.settings.Constants;
import ntorrent.settings.LocalSettings;

public class NTorrent{
	static Controller C;
	public static LocalSettings settings = new LocalSettings();
	
	/**
	 * @param args
	 */
	public static void main(String[] args){
		//load settings
		settings.deserialize();
		//start socket server
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			new Server().start();
			//print license
			System.out.println(Constants.getLicense());
			//start process
			C = new Controller(args);
		} catch(BindException e) {
			System.out.println("Server already started");
			//Server already started.
			//connect to existing process
			try {
				new Client(args);
			} catch (IOException x) {
				x.printStackTrace();
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean loadTorrent(File file) {
		try {
			return C.getIO().loadTorrent(file);
		} catch (Exception x){
			x.printStackTrace();
		}
		return false;
	}
}
