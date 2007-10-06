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

package org.heldig.ntorrent.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.heldig.ntorrent.Controller;
import org.heldig.ntorrent.gui.dialogue.PromptProfile;
import org.heldig.ntorrent.gui.tray.ProcessTrayIcon;
import org.heldig.ntorrent.gui.view.MainTableComponent;
import org.heldig.ntorrent.model.TorrentJTableModel;


/**
 * @author  Kim Eik
 */
public class GUIController{
	private Window rootWin = new Window();
	private StatusBarComponent statusBar;
	private MenuBarComponent menuBar;
	private FileTabComponent fileTab; 
	private MainTableComponent table;
	private ViewTabComponent viewTab;
	private Controller parent;
	
	public GUIController(Controller parent){
		this.parent = parent;
		menuBar = new MenuBarComponent(parent);
		table = new MainTableComponent(parent); 
		try {
			fileTab = new FileTabComponent(parent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		viewTab = new ViewTabComponent(parent,table.getTable());
		statusBar = new StatusBarComponent(parent);
		new PromptProfile(rootWin,parent);
	}
	
	public void drawMainWindow(){
		rootWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rootWin.setLayout(new BorderLayout());
		rootWin.setPreferredSize(new Dimension(1024,768));
		rootWin.setMenuBar(menuBar.getMenubar());
		rootWin.setContentPane(getContentPane());
		rootWin.validate();
		rootWin.pack();
		rootWin.setLocationRelativeTo(null);
		rootWin.setVisible(true);
		new ProcessTrayIcon(parent,rootWin);
	}
	
	
	private JPanel getContentPane(){
		JPanel content = new JPanel(new BorderLayout());
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				viewTab.getViewPane(), fileTab.getFilePane());
		splitPane.setDividerLocation(400);
		content.setBackground(Color.LIGHT_GRAY);
		content.add(splitPane,BorderLayout.CENTER);
		content.add(statusBar.getStatusBar(),BorderLayout.SOUTH);
		return content;
	}
	
	public void showError(String message){
		JOptionPane.showMessageDialog(getRootWin(), 
				message,
				"Error",
				JOptionPane.ERROR_MESSAGE);
	}
	
	public void showError(Throwable e){
		showError(e.getLocalizedMessage());
	}
	
	public TorrentJTableModel getTorrentTableModel(){
		return (TorrentJTableModel)table.getTable().getModel();
	}
	 
	//Not in use atm.
	/**
	 * @return
	 */
	public FileTabComponent getFileTab() {
		return fileTab;
	}
	
	
	/**
	 * @return
	 */
	public MenuBarComponent getMenuBar() {
		return menuBar;
	}
	
	/**
	 * @return
	 */
	public Window getRootWin() {
		return rootWin;
	}
	
	/**
	 * @return
	 */
	public StatusBarComponent getStatusBar() {
		return statusBar;
	}
	
	/**
	 * @return
	 */
	public MainTableComponent getTable() {
		return table;
	}
	
	/**
	 * @return
	 */
	public ViewTabComponent getViewTab() {
		return viewTab;
	}


}