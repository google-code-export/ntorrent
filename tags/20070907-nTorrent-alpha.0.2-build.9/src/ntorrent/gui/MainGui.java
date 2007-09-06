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

package ntorrent.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import ntorrent.gui.elements.FileTabComponent;
import ntorrent.gui.elements.MainTableComponent;
import ntorrent.gui.elements.MenuBarComponent;
import ntorrent.gui.elements.StatusBarComponent;
import ntorrent.gui.elements.ViewTabComponent;
import ntorrent.gui.listener.MainGlassPane;
import ntorrent.model.TorrentTableModel;
import ntorrent.settings.Constants;

public class MainGui {
	private MainGlassPane listener = new MainGlassPane();
	private JFrame rootWin = new JFrame(Constants.getReleaseName());
	private StatusBarComponent statusBar = new StatusBarComponent();
	private MenuBarComponent menuBar = new MenuBarComponent();
	private FileTabComponent fileTab = new FileTabComponent(listener);
	private MainTableComponent table = new MainTableComponent(); 
	private ViewTabComponent viewTab;
	
	public MainGui(){
		//mainContent = getTableView();
		viewTab = new ViewTabComponent(listener,table.getTable());
		Image icon = Toolkit.getDefaultToolkit().getImage("icons/ntorrent48.png");
		rootWin.setIconImage(icon);
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
	
	public TorrentTableModel getTorrentTableModel(){
		return (TorrentTableModel)table.getTable().getModel();
	}
	 
	//Not in use atm.
	public FileTabComponent getFileTab() {
		return fileTab;
	}
	
	public MainGlassPane getListener() {
		return listener;
	}
	
	public MenuBarComponent getMenuBar() {
		return menuBar;
	}
	
	public JFrame getRootWin() {
		return rootWin;
	}
	
	public StatusBarComponent getStatusBar() {
		return statusBar;
	}
	
	public MainTableComponent getTable() {
		return table;
	}
	
	public ViewTabComponent getViewTab() {
		return viewTab;
	}
	
	public void writeToLog(String msg){
		fileTab.writeToLog(msg+"\n");
	}
	

}