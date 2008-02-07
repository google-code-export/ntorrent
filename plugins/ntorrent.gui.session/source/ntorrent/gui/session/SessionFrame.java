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
package ntorrent.gui.session;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 * Main session gui component.
 */
public class SessionFrame extends JPanel{
	public SessionFrame() {
		super(new BorderLayout());
		JSplitPane vsplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		vsplit.add(new JLabel("selection/label"));
		vsplit.add(new JLabel("table"));
		
		JSplitPane hsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		hsplit.add(vsplit);
		hsplit.add(new JLabel("JTAB"));
		
		add(hsplit);
		/**
		 * Left bar
		 * 		^--- torrent selection
		 * 		^--- torrent labels
		 * torrent list table north
		 * torrent info tabbed pane south
		 * 		^--- tab, general torrent info
		 * 		^--- tab, torrent files info
		 * 		^--- tab, torrent tracker info
		 */
	}

}
