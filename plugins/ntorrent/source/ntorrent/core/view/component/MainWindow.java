/**
 *   nTorrent - A GUI client to administer a rtorrent process 
 *   over a network connection.
 *   
 *   Copyright (C) 2007  Kim Eik
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ntorrent.core.view.component;


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import ntorrent.NtorrentApplication;
import ntorrent.connection.ConnectionController;
import ntorrent.core.view.component.util.Window;
import ntorrent.locale.ResourcePool;
import ntorrent.settings.SettingsController;

/**
 * The main ntorrent window, consisting of menubar and jtabbedpane, 
 * where each tab holds its own session.
 */
public class MainWindow extends Window implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final static ConnectionTab connectionsTab = new ConnectionTab(JTabbedPane.TOP);
	private final static ConnectionController connectionController = new ConnectionController();
	private final MainMenuBar menuBar = new MainMenuBar(this);
	private final SettingsController settings;
	
	public MainWindow() {
		super();
		setPreferredSize(new Dimension(768,640));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setJMenuBar(menuBar);
		//setContentPane(connectionsTab);
		this.settings = new SettingsController();
	}
		
	public void actionPerformed(ActionEvent e) {
		String c = e.getActionCommand();
		String[] ids = MainMenuBar.identifiers;
		if(c.equals(ids[0])){
			TorrentFileChooser chooser = new TorrentFileChooser();
			int result = chooser.showOpenDialog(this);
			if(result == JFileChooser.APPROVE_OPTION){
				for(File f : chooser.getSelectedFiles()){
					NtorrentApplication.clientSoConn(f.getAbsolutePath());
				}
			}
		}else if(c.equals(ids[1])){
			String line = JOptionPane.showInputDialog(ResourcePool.getString("addurl", this));
			if (line != null && line.length() > 0){
				NtorrentApplication.clientSoConn(line);
			}
		}else if(c.equals(ids[2])){
			showConnectDialogue();
		}else if(c.equals(ids[3])){
			System.exit(0);
		}else if(c.equals(ids[4])){
			settings.drawWindow();
		}else if(c.equals(ids[5])){
			new AboutWindow().drawWindow();
		}
	}
	
	public void showConnectDialogue() {
		Container connectionView = connectionController.getDisplay();
		connectionView.setVisible(true);
		setContentPane(connectionView);
		validate();
		pack();
	}
	
	public void addToConnectionsTab(String title, Component c){
		connectionsTab.add(title, c);
	}

	public void showConnectionsTab() {
		setContentPane(connectionsTab);
		validate();
		pack();
	}
}
