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
package ntorrent.torrenttable.sorter.view;

import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TorrentTableFinder extends JPanel {
	private static final long serialVersionUID = 1L;
	
	final JTextField searchBox = new JTextField(10);
	final static ImageIcon searchIcon = new ImageIcon("plugins/ntorrent.torrenttable.sorter/icons/system-search.png");
	
	public TorrentTableFinder(KeyListener k) {
		super(new FlowLayout(FlowLayout.RIGHT));
		
		searchBox.addKeyListener(k);
		
		add(new JLabel(searchIcon));
		add(searchBox);
	}

}
