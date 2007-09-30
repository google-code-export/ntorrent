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
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import ntorrent.Controller;
import ntorrent.gui.dialogue.PromptEnv;
import ntorrent.gui.main.file.FileTabComponent;
import ntorrent.gui.main.view.MainTableComponent;
import ntorrent.gui.main.view.ViewTabComponent;
import ntorrent.gui.menu.MenuBarComponent;
import ntorrent.gui.status.StatusBarComponent;
import ntorrent.gui.tray.ProcessTrayIcon;
import ntorrent.model.TorrentTableModel;
import ntorrent.settings.Constants;

/**
 * @author  Kim Eik
 */
public class GUIController{
	private JFrame rootWin = new JFrame(Constants.getReleaseName());
	private StatusBarComponent statusBar = new StatusBarComponent();
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
		Image icon = Toolkit.getDefaultToolkit().getImage("icons/ntorrent48.png");
		rootWin.setIconImage(icon);
		new PromptEnv(rootWin,parent);
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
	
	public TorrentTableModel getTorrentTableModel(){
		return (TorrentTableModel)table.getTable().getModel();
	}
	 
	//Not in use atm.
	/**
	 * @return
	 * @uml.property  name="fileTab"
	 */
	public FileTabComponent getFileTab() {
		return fileTab;
	}
	
	
	/**
	 * @return
	 * @uml.property  name="menuBar"
	 */
	public MenuBarComponent getMenuBar() {
		return menuBar;
	}
	
	/**
	 * @return
	 * @uml.property  name="rootWin"
	 */
	public JFrame getRootWin() {
		return rootWin;
	}
	
	/**
	 * @return
	 * @uml.property  name="statusBar"
	 */
	public StatusBarComponent getStatusBar() {
		return statusBar;
	}
	
	/**
	 * @return
	 * @uml.property  name="table"
	 */
	public MainTableComponent getTable() {
		return table;
	}
	
	/**
	 * @return
	 * @uml.property  name="viewTab"
	 */
	public ViewTabComponent getViewTab() {
		return viewTab;
	}


}