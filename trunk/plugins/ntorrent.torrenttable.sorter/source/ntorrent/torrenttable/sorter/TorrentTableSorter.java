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
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.TableRowSorter;
import javax.swing.text.PlainDocument;

import ntorrent.session.ConnectionSession;
import ntorrent.session.SessionExtension;
import ntorrent.torrenttable.SelectionValueInterface;
import ntorrent.torrenttable.TorrentTableInterface;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentTableModel;
import ntorrent.torrenttable.sorter.model.TorrentRowFilter;
import ntorrent.torrenttable.sorter.view.TorrentTableFinder;
import ntorrent.torrenttable.view.TorrentTable;

import org.java.plugin.Plugin;

public class TorrentTableSorter extends Plugin implements SessionExtension{
	
	private HashMap<ConnectionSession,JComponent> sessions = new HashMap<ConnectionSession,JComponent>();
	private TableRowSorter<TorrentTableModel> sorter;
	private TorrentRowFilter filter;
	private final static PlainDocument document = new PlainDocument();
	private boolean init = false;
	
	@Override
	protected void doStart() throws Exception {
		if(init){
			for(ConnectionSession s : sessions.keySet()){
				final TorrentTableInterface controller = s.getTorrentTableController();
				final TorrentTable table = controller.getTable();
				final JPanel p = table.getDisplay();
				
				final SelectionValueInterface selectionMethod = new SelectionValueInterface(){

					public Torrent getTorrentFromView(int index) {
						return table.getModel().getRow(sorter.convertRowIndexToModel(index));
					}
					
					
				};
				
				controller.setSelectionMethod(selectionMethod);
				table.setRowSorter(sorter);
				p.add(sessions.get(s),BorderLayout.SOUTH);
				p.revalidate();
				p.repaint();
			}
			
		}
	}

	@Override
	protected void doStop() throws Exception {
		if(init){
			for(ConnectionSession s : sessions.keySet()){
				final TorrentTableInterface controller = s.getTorrentTableController();
				final TorrentTable table = controller.getTable();
				final JPanel p = table.getDisplay();
				
				controller.setSelectionMethod(null);
				table.setRowSorter(null);
				p.remove(sessions.get(s));
				p.revalidate();
				p.repaint();
			}
		}
	}


	public void init(ConnectionSession session) {
					
			if(!init){
				init = true;
				sorter = new TableRowSorter<TorrentTableModel>(session.getTorrentTableController().getTable().getModel());
				filter = new TorrentRowFilter(sorter);
			}
			
			if(!sessions.containsKey(session)){
				TorrentTableFinder finder = new TorrentTableFinder(filter);
				JTextField searchBox = finder.getSearchBox();
				searchBox.setDocument(document);
				sessions.put(session,finder);
			}
			
			if(getManager().isPluginActivated(getDescriptor()))
				try {
					doStart();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}


}
