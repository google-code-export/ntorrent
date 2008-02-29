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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableRowSorter;

import ntorrent.env.Environment;
import ntorrent.io.rtorrent.Download;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import ntorrent.session.ConnectionSession;
import ntorrent.session.SessionExtension;
import ntorrent.torrentlabels.model.LabelListModel;
import ntorrent.torrentlabels.model.TorrentTableFilter;
import ntorrent.torrentlabels.view.LabelList;
import ntorrent.torrenttable.TorrentTableInterface;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentTableActionListener;
import ntorrent.torrenttable.model.TorrentTableModel;
import ntorrent.torrenttable.sorter.TorrentTableSorter;
import ntorrent.torrenttable.view.TorrentTable;
import ntorrent.torrenttable.view.TorrentTableJPopupMenu;

import org.java.plugin.Plugin;

public class LabelController extends Plugin implements TorrentTableActionListener, SessionExtension, TableModelListener, ListSelectionListener {

	
	public final static String PROPERTY = "d.get_custom1=";
	private final static String[] mitems = {
		"torrentlabel.menu.none",
		"torrentlabel.menu.new"
		};
	
	Vector<String> download_variable;
	RowFilter filter;
	TorrentTableFilter labelFilter = new TorrentTableFilter();
	private XmlRpcConnection connection;
	private TorrentTableInterface controller;
	private final LabelListModel listModel = new LabelListModel();
	private final LabelList labelList = new LabelList(listModel);
	
	private TorrentTableJPopupMenu menu;
	private JMenu label;
	
	private boolean init = false;

	private TorrentTable table;
	private TableRowSorter sorter;

	private TorrentTableModel tablemodel;

	private JSplitPane menuPane;
	private Component oldComponent;
	
	private final Set<RowFilter> filterSet = new HashSet<RowFilter>(); 
	
	/**
	 * CLEAN THIS MESS UP!
	 */
	
	@Override
	protected void doStart() throws Exception {		
		if(init){
			menu.add(label);
			if(!download_variable.contains(PROPERTY)){
				download_variable.add(PROPERTY);
			}
			sorter.setRowFilter(filter.andFilter(filterSet));
			menuPane.setBottomComponent(labelList);
		}
	}

	@Override
	protected void doStop() throws Exception {
		if(init){
			menu.remove(label);
			download_variable.remove(PROPERTY);
			menuPane.setBottomComponent(oldComponent);
		}
		sorter.setRowFilter(filter);
	}	

	public void torrentActionPerformed(Torrent[] tor, String command) {
		if(command.equals(mitems[0])){
			setLabel(null,tor,connection);
		}else if(command.equals(mitems[1])){
			String label = JOptionPane.showInputDialog("label me babe,");
			setLabel(label,tor,connection);
		}else{
			if(command.startsWith("label:")){
				setLabel(command.split(":")[1], tor,connection);
			}
		}
	}

	public void init(ConnectionSession session) {
		
		this.controller = session.getTorrentTableController();
		this.table = controller.getTable();
		this.tablemodel = table.getModel();
		this.sorter = ((TableRowSorter)table.getRowSorter());
		tablemodel.addTableModelListener(this);
		
		download_variable = controller.getDownloadVariable();
		filter = sorter.getRowFilter();
		
		if(!init){
			filterSet.add(labelFilter);
			filterSet.add(filter);
			init = true;
		}
		
		initMenu(controller.getTable().getTablePopup());
		
		connection = session.getConnection();
		menuPane = session.getDisplay().getMenu();
		oldComponent = menuPane.getBottomComponent();
		menuPane.setBottomComponent(labelList);
		
		labelList.addListSelectionListener(this);
		

		
		if(getManager().isPluginActivated(getDescriptor())){
			try {
				doStart();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
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
		int type = event.getType();
		boolean updateOnce = true;

		if(type == TableModelEvent.INSERT){
			updateLabels();
		}else if(updateOnce && type == TableModelEvent.UPDATE){
			updateOnce = false;
			updateLabels();
		}
	}

	private void updateLabels(){
		for(Torrent t : controller.getTorrents().values()){
			String label = (String)t.getProperty(PROPERTY);
			if(label != null && label.length() > 0 && !listModel.containsKey(label)){
				int row = listModel.getSize()-1;
				JMenuItem item = new JMenuItem(label);
				item.addActionListener(menu);
				item.setActionCommand("label:"+label);
				listModel.put(label, item);
				this.label.add(item);
				// yes yes yes, im cheating i know..
				listModel.fireIntervallAdded(this, row, row+1);
			}
		}
	}
	
	private void setLabel(final String label, final Torrent[] tor, final XmlRpcConnection connection) {
		//set labels async
		new Thread(){
			public void run(){
				Download d = connection.getDownloadClient();
				for(Torrent t : tor){
					System.out.println("setting label to "+label+" on "+t.getHash()+" ("+t.getName()+")");
					d.set_custom1(t.getHash(),label);
				}
			}
		}.start();
	}

	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting()){
			if(labelList.getSelectedIndex() > 1){
				String label = (String)labelList.getSelectedValue();
				labelFilter.setLabel(label);
			}else{
				if(labelList.getSelectedIndex() == 0){
					labelFilter.allLabel();
				}else if(labelList.getSelectedIndex() == 1){
					labelFilter.noneLabel();
				}
			}
		}
		sorter.sort();
	}


}
