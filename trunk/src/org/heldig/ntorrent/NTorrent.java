package org.heldig.ntorrent;


import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.BindException;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.heldig.ntorrent.gui.MenuBarComponent;
import org.heldig.ntorrent.gui.Window;
import org.heldig.ntorrent.gui.tray.ProcessTrayIcon;
import org.heldig.ntorrent.io.socket.Client;
import org.heldig.ntorrent.io.socket.Server;
import org.heldig.ntorrent.settings.Constants;
import org.heldig.ntorrent.settings.LocalSettings;



public class NTorrent {
	final static Window rootWin = new Window();
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
			C = new Controller(args,rootWin);
			draw();
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
			System.out.println(NTorrent.class);
			//return C.IO.loadTorrent(file);
		} catch (Exception x){
			x.printStackTrace();
		}
		return false;
	}
	
	private static void draw(){
		final MenuBarComponent menuBar = new MenuBarComponent();
		menuBar.addActionListener(C.getGC());
		new ProcessTrayIcon(rootWin,menuBar);
		rootWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rootWin.setPreferredSize(new Dimension(1024,768));
		rootWin.setJMenuBar(menuBar);
		rootWin.validate();
		rootWin.pack();
		rootWin.setLocationRelativeTo(null);
		rootWin.setVisible(true);
	}
}
