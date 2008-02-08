package ntorrent.gui.trayicon;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import ntorrent.Main;
import ntorrent.env.Environment;

import org.java.plugin.Plugin;

public class TrayIconHandler extends Plugin {
	
	TrayIcon trayIcon;
	SystemTray tray = SystemTray.getSystemTray();
	
	@Override
	protected void doStart() throws Exception {
		if (java.awt.SystemTray.isSupported()) {

		    Image image = Toolkit.getDefaultToolkit().getImage("plugins/ntorrent/icons/ntorrent48.png");


		    trayIcon = new TrayIcon(image, Environment.getAppName());
		    trayIcon.setImageAutoSize(true);
		    tray.add(trayIcon);

		} else {

		    //  System Tray is not supported

		}

	}

	@Override
	protected void doStop() throws Exception {
		tray.remove(trayIcon);
	}


}
