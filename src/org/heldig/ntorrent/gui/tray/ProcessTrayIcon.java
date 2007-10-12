package org.heldig.ntorrent.gui.tray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import org.heldig.ntorrent.gui.core.JPopupMenuImplementation;
import org.heldig.ntorrent.language.Language;
import org.heldig.ntorrent.settings.Constants;


/**
 * @author  Kim Eik
 */
public class ProcessTrayIcon extends JPopupMenuImplementation {
	TrayIcon trayIcon;
	Window rootWin;
	ActionListener action;
	public ProcessTrayIcon(Window root, ActionListener a) {
		createMenuItems(popup,menuItems, this);
		action = a;
		rootWin = root;
		if (SystemTray.isSupported()) {
		    SystemTray tray = SystemTray.getSystemTray();
		    Image image = Toolkit.getDefaultToolkit().getImage("icons/ntorrent48.png");
		    trayIcon = new TrayIcon(image,Constants.getReleaseName(),null);
		    trayIcon.setImageAutoSize(true);
		    trayIcon.addMouseListener(this);
		    //trayIcon.addActionListener(this);

		    try {
		        tray.add(trayIcon);
		    } catch (AWTException e) {
		    	e.printStackTrace();
		    }

		} else {
		    //  System Tray is not supported
		}
	}
	
	final static Object[] menuItems = {
		Language.Menu_File_add_torrent,
		Language.Menu_File_add_url,
		null,
		Language.Menu_File_start_all,
		Language.Menu_File_stop_all,
		null,
		Language.Menu_Help_settings,
		Language.Menu_Help_about,
		null,
		Language.Menu_File_quit
		};

	@Override
	protected void maybeShowPopup(MouseEvent e) {
		switch (e.getButton()){
		case MouseEvent.BUTTON1:
			if(!rootWin.isFocused()){
				rootWin.requestFocus();
				rootWin.toFront();
			}
			break;
		case MouseEvent.BUTTON3:
			popup.setLocation(e.getX(), e.getY());
			popup.setInvoker(rootWin);
			popup.setVisible(true);
			break;
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		maybeShowPopup(e);
	}
	
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		action.actionPerformed(e);	
	}
}
