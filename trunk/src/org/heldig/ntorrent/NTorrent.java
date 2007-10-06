package org.heldig.ntorrent;


import java.io.File;
import java.io.IOException;
import java.net.BindException;

import javax.swing.UIManager;

import org.heldig.ntorrent.gui.tray.ProcessTrayIcon;
import org.heldig.ntorrent.io.socket.Client;
import org.heldig.ntorrent.io.socket.Server;
import org.heldig.ntorrent.settings.Constants;
import org.heldig.ntorrent.settings.LocalSettings;



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
			new ProcessTrayIcon(C,C.getGC().getRootWin());
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
