package ntorrent.gui.elements;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import ntorrent.controller.Controller;
import ntorrent.gui.listener.TrayListener;
import ntorrent.settings.Constants;

public class ProcessTrayIcon {
	public ProcessTrayIcon() {
		final TrayIcon trayIcon;
		if (SystemTray.isSupported()) {
			TrayListener listener = new TrayListener();
		    SystemTray tray = SystemTray.getSystemTray();
		    Image image = Toolkit.getDefaultToolkit().getImage("icons/ntorrent48.png");
		    trayIcon = new TrayIcon(image,Constants.getReleaseName());
		    trayIcon.addActionListener(listener);
		    trayIcon.addMouseListener(listener);
		    trayIcon.setImageAutoSize(true);

		    try {
		        tray.add(trayIcon);
		    } catch (AWTException e) {
		    	Controller.writeToLog(e);
		    }

		} else {
		    //  System Tray is not supported
		}
	}
}
