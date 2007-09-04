package ntorrent.gui.elements;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import ntorrent.controller.Controller;
import ntorrent.settings.Constants;

public class ProcessTrayIcon {
	public ProcessTrayIcon() {
		final TrayIcon trayIcon;
		if (SystemTray.isSupported()) {
			TrayIconPopUpMenu popup = new TrayIconPopUpMenu();
		    SystemTray tray = SystemTray.getSystemTray();
		    Image image = Toolkit.getDefaultToolkit().getImage("icons/ntorrent48.png");
		    trayIcon = new TrayIcon(image,Constants.getReleaseName(),popup.getPopup());
		    trayIcon.addActionListener(Constants.trayListener);
		    trayIcon.addMouseListener(Constants.trayListener);
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
