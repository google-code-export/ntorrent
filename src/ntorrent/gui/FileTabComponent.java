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

import java.io.IOException;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import ntorrent.Controller;
import ntorrent.gui.file.FileList;
import ntorrent.gui.file.InfoPanel;
import ntorrent.gui.listener.GuiEventListener;

/**
 * @author  Kim Eik
 */
public class FileTabComponent extends GuiEventListener  {
	JTabbedPane filePane = new JTabbedPane();
	InfoPanel infoPanel = new InfoPanel();
	FileList fileList;

	
	public FileTabComponent(Controller c) throws IOException{
		super(c);
		fileList = new FileList(c);
		filePane.setName("file");
		//filePane.addTab("peer list", new JLabel("not supported by rtorrent"));
		filePane.addTab("info", infoPanel.getInfoPanel());
		filePane.addTab("file list", fileList.getFileList());
		filePane.addTab("log",new JScrollPane(null));
		filePane.setSelectedIndex(2);
		//filePane.addTab("tracker list", new JLabel("not supported by rtorrent"));
		//filePane.addTab("chunk list", new JLabel("not supported by rtorrent"));
		//filePane.addTab("chunks seen", new JLabel("not supported by rtorrent"));
		filePane.addChangeListener(this);
		filePane.setVisible(true);
	}
	
	/**
	 * @return
	 */
	public JTabbedPane getFilePane() {
		return filePane;
	}
	
	/**
	 * @return
	 */
	public InfoPanel getInfoPanel() {
		return infoPanel;
	}
	
	/**
	 * @return
	 */
	public FileList getFileList() {
		return fileList;
	}
}
