package ntorrent.gui.trayicon;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import ntorrent.Main;
import ntorrent.env.Environment;

import org.java.plugin.Plugin;

public class TrayIconHandler extends Plugin {
	
	TrayIcon trayIcon;
	
	@Override
	protected void doStart() throws Exception {
		if (java.awt.SystemTray.isSupported()) {

			java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
		    Image image = Toolkit.getDefaultToolkit().getImage("plugins/ntorrent/icons/ntorrent48.png");


		    trayIcon = new TrayIcon(image, Environment.getAppName());
		    trayIcon.setImageAutoSize(true);


		    try {
		        tray.add(trayIcon);
		    } catch (AWTException e) {
		        System.err.println("TrayIcon could not be added.");
		    }

		} else {

		    //  System Tray is not supported

		}

	}

	@Override
	protected void doStop() throws Exception {
		System.out.println("tray icon");
	}


}
