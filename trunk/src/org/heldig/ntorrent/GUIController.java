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

package org.heldig.ntorrent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.heldig.ntorrent.gui.FileTabComponent;
import org.heldig.ntorrent.gui.StatusBarComponent;
import org.heldig.ntorrent.gui.label.LabelListComponent;
import org.heldig.ntorrent.gui.profile.PromptProfile;
import org.heldig.ntorrent.gui.torrent.TorrentJTableComponent;
import org.heldig.ntorrent.gui.torrent.TorrentJTableModel;
import org.heldig.ntorrent.gui.viewlist.ViewListComponent;
import org.heldig.ntorrent.gui.window.AboutGui;
import org.heldig.ntorrent.gui.window.SettingsGui;
import org.heldig.ntorrent.language.Language;


/**
 * @author  Kim Eik
 */
public class GUIController implements ActionListener{
	public final FileTabComponent fileTab;
	private final LabelListComponent labelList;
	private final StatusBarComponent statusBar = new StatusBarComponent();
	private final TorrentJTableComponent table;
	private final ViewListComponent viewList;
	private Controller C;
	
	public GUIController(Controller c){
		C = c;
		table = new TorrentJTableComponent(c);
		labelList = new LabelListComponent(c);
		labelList.getModel().addListDataListener(table);
		viewList = new ViewListComponent(c);
		fileTab = new FileTabComponent(c);
		fileTab.setLog(c.getLog());
		System.out.println("Drawing gui");
		promptProfile();
	}
	
	
	public JComponent getContentPane(){
		JPanel content = new JPanel(new BorderLayout());
		JPanel leftBar = new JPanel(new BorderLayout(0,1));
		leftBar.setBackground(Color.lightGray);
		leftBar.add(viewList.getViewList(),BorderLayout.NORTH);
		leftBar.add(labelList);
		JSplitPane hsplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				new JScrollPane(leftBar),new JScrollPane(table.getTable()));
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				hsplitPane, fileTab.getFilePane());
		splitPane.setDividerLocation(500);
		content.setBackground(Color.LIGHT_GRAY);
		content.add(splitPane,BorderLayout.CENTER);
		content.add(statusBar.getStatusBar(),BorderLayout.SOUTH);
		return content;
	}
	
	public final static void showError(String message){
		JOptionPane.showMessageDialog(null, 
				message,
				"Error",
				JOptionPane.ERROR_MESSAGE);
	}
	
	public void showError(Throwable e){
		showError(e.getLocalizedMessage());
	}
	
	public void promptProfile(){
		new PromptProfile(C);
	}
	
	public TorrentJTableModel getTableModel() {
		return table.getModel();
	}

	public StatusBarComponent getStatusBarComponent() {
		return statusBar;
	}
	
	public LabelListComponent getLabelList() {
		return labelList;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (Language.getFromString(e.getActionCommand())){
		case Menu_File_add_torrent:
			JFileChooser jf = new JFileChooser();
			if(jf.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				C.loadTorrent(jf.getSelectedFile());
			break;
		case Menu_File_add_url:
			C.loadTorrent((String)JOptionPane.showInputDialog(null));
			break;
		case Menu_File_start_all:
		case Menu_File_stop_all:
			C.actionPerformed(e);
			break;
		case Menu_File_quit:
			System.exit(0);
			break;
		case Menu_Help_settings:
			new SettingsGui();
			break;
		case Menu_Help_about:
			new AboutGui();
			break;
		}
	}

}