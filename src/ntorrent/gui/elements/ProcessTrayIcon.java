package ntorrent.gui.elements;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;

import javax.swing.JPopupMenu;

import ntorrent.controller.Controller;
import ntorrent.settings.Constants;

public class ProcessTrayIcon {
	TrayIcon trayIcon;
	JPopupMenu popup;
	public ProcessTrayIcon() {
		if (SystemTray.isSupported()) {
			popup = new TrayIconPopUpMenu().getPopup();
		    SystemTray tray = SystemTray.getSystemTray();
		    Image image = Toolkit.getDefaultToolkit().getImage("icons/ntorrent48.png");
		    trayIcon = new TrayIcon(image,Constants.getReleaseName(),null);
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
	
	public TrayIcon getTrayIcon() {
		return trayIcon;
	}
	
	public JPopupMenu getPopup() {
		return popup;
	}
}
