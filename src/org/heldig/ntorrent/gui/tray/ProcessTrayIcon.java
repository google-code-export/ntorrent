package org.heldig.ntorrent.gui.tray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import org.heldig.ntorrent.language.Language;
import org.heldig.ntorrent.settings.Constants;


/**
 * @author  Kim Eik
 */
public class ProcessTrayIcon extends JPopupMenu implements MouseListener{
	private static final long serialVersionUID = 1L;
	private static TrayIcon trayIcon;
	private static Window rootWin;
	
	public ProcessTrayIcon(Window root, ActionListener a) {
		rootWin = root;
		if (SystemTray.isSupported()) {
		    SystemTray tray = SystemTray.getSystemTray();
		    Image image = Toolkit.getDefaultToolkit().getImage("icons/ntorrent48.png");
		    trayIcon = new TrayIcon(image,Constants.getReleaseName(),null);
		    trayIcon.setImageAutoSize(true);
		    trayIcon.addMouseListener(this);
		    trayIcon.addActionListener(a);

		    try {
		        tray.add(trayIcon);
		    } catch (AWTException e) {
		    	e.printStackTrace();
		    }
		    
		    add(Language.Menu_File_add_torrent).addActionListener(a);
		    add(Language.Menu_File_add_url).addActionListener(a);
		    add(new JSeparator());
		    add(Language.Menu_File_start_all).addActionListener(a);
		    add(Language.Menu_File_stop_all).addActionListener(a);
		    add(new JSeparator());
		    add(Language.Menu_Help_settings).addActionListener(a);
		    add(Language.Menu_Help_about).addActionListener(a);
		    add(new JSeparator());
		    add(Language.Menu_File_quit).addActionListener(a);

		} else {
		    //  System Tray is not supported
		}
	}
	
	protected void maybeShowPopup(MouseEvent e) {
		switch (e.getButton()){
		case MouseEvent.BUTTON1:
			if(!rootWin.isFocused()){
				rootWin.requestFocus();
				rootWin.toFront();
			}
			break;
		case MouseEvent.BUTTON3:
			setLocation(e.getX(), e.getY());
			setInvoker(rootWin);
			setVisible(true);
			break;
		}
	}
	
	public void mouseClicked(MouseEvent e) {

	}
	
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.isPopupTrigger())
			maybeShowPopup(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.isPopupTrigger())
			maybeShowPopup(e);
	}
	
	
}
