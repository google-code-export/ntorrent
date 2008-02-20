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

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 * Main session gui component.
 */
public class SessionFrame extends JPanel{
	
	final private JSplitPane vsplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	final private JSplitPane hsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

	
	public SessionFrame() {
		super(new BorderLayout());
		hsplit.add(vsplit);
		add(hsplit);
		add(new JLabel("statusbar!"), BorderLayout.SOUTH);
	}
	
	public JSplitPane getHsplit() {
		return hsplit;
	}
	
	public JSplitPane getVsplit() {
		return vsplit;
	}

}
