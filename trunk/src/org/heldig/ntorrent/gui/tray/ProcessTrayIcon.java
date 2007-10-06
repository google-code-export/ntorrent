package org.heldig.ntorrent.gui.tray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.Window;

import org.heldig.ntorrent.Controller;
import org.heldig.ntorrent.settings.Constants;


/**
 * @author  Kim Eik
 */
public class ProcessTrayIcon {
	TrayIcon trayIcon;
	TrayIconPopUpMenu popup;
	public ProcessTrayIcon(Controller c,Window root) {
		if (SystemTray.isSupported()) {
			popup = new TrayIconPopUpMenu(c,root);
		    SystemTray tray = SystemTray.getSystemTray();
		    Image image = Toolkit.getDefaultToolkit().getImage("icons/ntorrent48.png");
		    trayIcon = new TrayIcon(image,Constants.getReleaseName(),null);
		    trayIcon.setImageAutoSize(true);
		    trayIcon.addMouseListener(popup);
		    trayIcon.addActionListener(popup);

		    try {
		        tray.add(trayIcon);
		    } catch (AWTException e) {
		    	e.printStackTrace();
		    }

		} else {
		    //  System Tray is not supported
		}
	}
}
