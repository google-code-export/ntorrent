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
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Main session gui component.
 */
public class SessionFrame extends JPanel{
	
	final private JSplitPane vsplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	final private JSplitPane hsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	final private JSplitPane menu = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

	
	public SessionFrame(JComponent[] p) {
		super(new BorderLayout());
		hsplit.add(vsplit);
		add(hsplit);
		
		menu.add(p[0]);
		menu.add(new JLabel("label"));
		
		vsplit.add(menu);
		vsplit.add(p[1]);
		hsplit.add(p[2]);
		add(p[3], BorderLayout.SOUTH);
	}
	
	public JSplitPane getHsplit() {
		return hsplit;
	}
	
	public JSplitPane getVsplit() {
		return vsplit;
	}

}
