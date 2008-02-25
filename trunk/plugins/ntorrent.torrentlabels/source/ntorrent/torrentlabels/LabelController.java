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
package ntorrent.torrentlabels;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.RowFilter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import ntorrent.env.Environment;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentTableActionListener;
import ntorrent.torrenttable.model.TorrentTableJPopupMenuExtension;
import ntorrent.torrenttable.model.TorrentTableModel;
import ntorrent.torrenttable.sorter.model.TorrentTableFilterExtension;
import ntorrent.torrenttable.sorter.model.TorrentTableFilterExtensionPoint;
import ntorrent.torrenttable.view.TorrentTableJPopupMenu;

import org.java.plugin.Plugin;

public class LabelController extends Plugin implements TorrentTableFilterExtension, TorrentTableJPopupMenuExtension, TorrentTableActionListener {

	private final static String[] mitems = {
		"torrentlabel.menu.none",
		"torrentlabel.menu.new"
		};
	private TorrentTableJPopupMenu menu;
	private JMenu label;
	
	
	@Override
	protected void doStart() throws Exception {
		if(menu != null)
			menu.add(label);
	}

	@Override
	protected void doStop() throws Exception {
		if(menu != null)
			menu.remove(label);
	}

	public void init(TorrentTableFilterExtensionPoint p) {
			/*final RowFilter<TorrentTableModel, Torrent> filter = new RowFilter<TorrentTableModel, Torrent>(){
	
				@Override
				public boolean include(
						javax.swing.RowFilter.Entry<? extends TorrentTableModel, ? extends Torrent> entry) {
					return entry.getIdentifier().getProperty("d.get_custom1") == null; 
				}
				
			};
			p.addFilter(filter);
			p.updateFilter();*/
		
	}

	public void init(TorrentTableJPopupMenu menu) {
		this.menu = menu;
		menu.addTorrentTableActionListener(this);
		label = new JMenu(Environment.getString("torrentlabel.menu"));
		
		for(int x = 0; x < mitems.length; x++){
			JMenuItem item = new JMenuItem(Environment.getString(mitems[x]));
			item.setActionCommand(mitems[x]);
			item.addActionListener(menu);
			label.add(item);
		}
		
		label.addSeparator();
		//existing labels
		
		
		menu.add(label);
	}

	public void torrentActionPerformed(Torrent[] tor, String command) {
		System.out.println("command peformed: "+command);
	}

}
