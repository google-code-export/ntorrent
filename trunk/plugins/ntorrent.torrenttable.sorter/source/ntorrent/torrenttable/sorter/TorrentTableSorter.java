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
package ntorrent.torrenttable.sorter;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTable;

import org.java.plugin.Plugin;

import ntorrent.torrenttable.TorrentTableController;
import ntorrent.torrenttable.TorrentTableExtension;
import ntorrent.torrenttable.TorrentTableInterface;
import ntorrent.torrenttable.model.TorrentTableModel;
import ntorrent.torrenttable.sorter.model.TorrentTableRowFilter;
import ntorrent.torrenttable.sorter.model.TorrentTableRowSorter;
import ntorrent.torrenttable.sorter.view.TorrentTableFinder;
import ntorrent.torrenttable.view.TorrentTable;

public class TorrentTableSorter extends Plugin implements TorrentTableExtension{
	
	private TorrentTable table;
	private JPanel panel;
	private TorrentTableRowSorter sorter;
	private TorrentTableRowFilter filter;
	private TorrentTableFinder gui;
	private boolean init = false;
	
	@Override
	protected void doStart() throws Exception {
		if(init){
			table.setRowSorter(sorter);
			panel.add(gui,BorderLayout.SOUTH);
			panel.revalidate();
			panel.repaint();
		}
	}

	@Override
	protected void doStop() throws Exception {
		if(init){
			table.setRowSorter(null);
			panel.remove(gui);
			panel.revalidate();
			panel.repaint();
		}
	}

	public void init(TorrentTableInterface controller) {
		init = true;

		table = controller.getTable();
		panel = table.getDisplay();
		
		sorter = new TorrentTableRowSorter(table.getModel());
		filter = new TorrentTableRowFilter(sorter);
		gui = new TorrentTableFinder(filter);
		
		if(getManager().isPluginActivated(getDescriptor()))
			try {
				doStart();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}


}
