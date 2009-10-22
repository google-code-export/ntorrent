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

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * @author Kim Eik
 *
 */
public class TorrentFileChooser extends JFileChooser {
	private static final long serialVersionUID = 1L;
	
	public TorrentFileChooser(String path) {
		super(path);
		init();
	}
	
	public TorrentFileChooser() {
		init();
	}
	
	private void init() {
		setMultiSelectionEnabled(true);
		setFileFilter(new FileFilter(){
			
			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith(".torrent");
			}
			
			@Override
			public String getDescription() {
				return ".torrent";
			}
		});
	}
	
}