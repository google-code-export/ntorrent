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

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.RowFilter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import ntorrent.env.Environment;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import ntorrent.session.ConnectionSession;
import ntorrent.session.SessionExtension;
import ntorrent.torrentlabels.view.LabelList;
import ntorrent.torrenttable.TorrentTableExtension;
import ntorrent.torrenttable.TorrentTableInterface;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentTableActionListener;
import ntorrent.torrenttable.model.TorrentTableModel;
import ntorrent.torrenttable.sorter.model.TorrentTableRowFilter;
import ntorrent.torrenttable.sorter.model.TorrentTableRowSorter;
import ntorrent.torrenttable.view.TorrentTable;
import ntorrent.torrenttable.view.TorrentTableJPopupMenu;

import org.java.plugin.Plugin;

public class LabelController extends Plugin implements TorrentTableActionListener, SessionExtension,TorrentTableExtension, TableModelListener {

	private final static String[] mitems = {
		"torrentlabel.menu.none",
		"torrentlabel.menu.new"
		};
	
	Vector<String> download_variable;
	TorrentTableRowFilter filter;
	RowFilter<TorrentTableModel, Torrent> labelFilter;
	XmlRpcConnection connection;
	private TorrentTableInterface controller;
	private final LabelList labelList = new LabelList();
	private final Map<String,JMenuItem> labelItems = new HashMap<String, JMenuItem>();
	
	private TorrentTableJPopupMenu menu;
	private JMenu label;
	
	private boolean tableInit = false;
	private boolean sessionInit = false;

	private TorrentTable table;

	private TorrentTableModel tablemodel;
	private final static String property = "d.get_custom1=";

	private JSplitPane menuPane;
	private Component oldComponent;
	
	@Override
	protected void doStart() throws Exception {
		if(tableInit){
			menu.add(label);
			if(!download_variable.contains(property)){
				download_variable.add(property);
			}
			filter.addFilter(labelFilter);
		}
		
		if(sessionInit){
			menuPane.setBottomComponent(labelList);
		}
	}

	@Override
	protected void doStop() throws Exception {
		if(tableInit){
			menu.remove(label);
			download_variable.remove(property);
			filter.removeFilter(labelFilter);
		}
		
		if(sessionInit){
			menuPane.setBottomComponent(oldComponent);
		}
	}	

	public void torrentActionPerformed(Torrent[] tor, String command) {
		if(command.equals(mitems[0])){
			setLabel(null,tor);
		}else if(command.equals(mitems[1])){
			String label = JOptionPane.showInputDialog("label me babe,");
			setLabel(label,tor);
		}else{
			if(command.startsWith("label:")){
				setLabel(command.split(":")[1], tor);
			}
		}
	}

	public void init(TorrentTableInterface controller) {
		tableInit = true;
		this.controller = controller;
		this.table = controller.getTable();
		this.tablemodel = table.getModel();
		tablemodel.addTableModelListener(this);
		
		download_variable = controller.getDownloadVariable();
		TorrentTableRowSorter sorter = (TorrentTableRowSorter) table.getRowSorter();
		filter = (TorrentTableRowFilter) sorter.getRowFilter();
		
		initFilter();
		initMenu(controller.getTable().getTablePopup());
		
		if(getManager().isPluginActivated(getDescriptor())){
			try {
				doStart();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	

	public void init(ConnectionSession session) {
		sessionInit = true;
		connection = session.getConnection();
		menuPane = session.getDisplay().getMenu();
		oldComponent = menuPane.getBottomComponent();
		menuPane.setBottomComponent(labelList);
		
		if(getManager().isPluginActivated(getDescriptor())){
			try {
				doStart();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private void initFilter(){
		 labelFilter = new RowFilter<TorrentTableModel, Torrent>(){
			
			private String label = "";
			
			@Override
			public boolean include(
					javax.swing.RowFilter.Entry<? extends TorrentTableModel, ? extends Torrent> entry) {
				System.out.println(entry.getIdentifier().getProperty(property).toString().length());
				return entry.getIdentifier().getProperty(property).equals(label); 
			}
			
		};
		
	}
	
	private void initMenu(TorrentTableJPopupMenu menu){
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
	}

	public void tableChanged(TableModelEvent event) {
		if(event.getType() != TableModelEvent.DELETE){
			for(int x = event.getFirstRow(); x <= event.getLastRow(); x++){
				Torrent t = tablemodel.getRow(x);
				String label = (String)t.getProperty(property);
				if(label != null && label.length() > 0 && !labelItems.containsKey(label)){
					JMenuItem item = new JMenuItem(label);
					item.addActionListener(menu);
					item.setActionCommand("label:"+label);
					labelItems.put(label, item);
					this.label.add(item);
				}
			}
		}else{
			//remove labels or just keep them?
		}
	}
	
	
	private void setLabel(String label, Torrent[] tor) {
		for(Torrent t : tor){
			System.out.println("stub: set label \""+label+"\"");
		}
	}


}
