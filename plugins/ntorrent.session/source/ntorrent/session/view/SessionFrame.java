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
package ntorrent.session.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import ntorrent.torrenttable.TorrentTableInterface;
import ntorrent.torrenttable.viewmenu.ViewMenuController;

/**
 * Main session gui component.
 */
public class SessionFrame extends JPanel{
	private static final long serialVersionUID = 1L;
	
	final private JSplitPane vsplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	final private JSplitPane hsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	final private JSplitPane menu = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	final private JTabbedPane tabbedPane = new JTabbedPane();
	final private JComponent statusBar = new JLabel("statusbar");

	
	public SessionFrame(TorrentTableInterface ttc, ViewMenuController vmc) {
		super(new BorderLayout());
		hsplit.add(vsplit);
		add(hsplit);

		menu.add(new JScrollPane(vmc.getDisplay()));
		menu.add(new JPanel(new GridLayout(0,1)));
		
		vsplit.add(menu);
		vsplit.add(ttc.getTable().getDisplay());
		hsplit.add(tabbedPane);
		add(statusBar, BorderLayout.SOUTH);
	}
	
	public JSplitPane getHsplit() {
		return hsplit;
	}
	
	public JSplitPane getVsplit() {
		return vsplit;
	}
	
	public JSplitPane getMenu() {
		return menu;
	}
	
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}
	
	public JComponent getStatusBar() {
		return statusBar;
	}

}
