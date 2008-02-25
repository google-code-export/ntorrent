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
package ntorrent.torrenttable.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import org.java.plugin.Plugin;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;

import ntorrent.env.Environment;
import ntorrent.torrenttable.TorrentTableController;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentTableActionListener;
import ntorrent.torrenttable.model.TorrentTableJPopupMenuExtension;

public class TorrentTableJPopupMenu extends JPopupMenu implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	Vector<TorrentTableActionListener> listeners = new Vector<TorrentTableActionListener>();
	Map<String,JMenuItem> menuItems = new HashMap<String,JMenuItem>();
	
	private TorrentTable table;
	
	public final static String[] mitems = {
		"torrenttable.menu.start",
		"torrenttable.menu.stop",
		"torrenttable.menu.remove",
		"torrenttable.menu.check"
		};
	
	public final static String[] priorityMenu = {
		"torrenttable.menu.priority",
		"torrenttable.menu.priority.high",
		"torrenttable.menu.priority.normal",
		"torrenttable.menu.priority.low",
		"torrenttable.menu.priority.off"
		};
	
	public TorrentTableJPopupMenu(TorrentTable table) {
		this.table = table;
		for(String s : mitems){
			JMenuItem item = add(Environment.getString(s));
			item.setActionCommand(s);
			item.addActionListener(this);
		}
		
		JMenu priority = new JMenu(Environment.getString(priorityMenu[0]));
		add(priority);
		
		for(int x = 1; x < priorityMenu.length; x++){
			JMenuItem item = priority.add(Environment.getString(priorityMenu[x]));
			item.setActionCommand(priorityMenu[x]);
			item.addActionListener(this);
		}
		
		PluginManager manager = Environment.getPluginManager();
		PluginRegistry reg = manager.getRegistry();
		ExtensionPoint ext = reg.getExtensionPoint("ntorrent.torrenttable", "TorrentTablePopupMenu");
		for(Extension e : ext.getAvailableExtensions()){
			PluginDescriptor p = e.getDeclaringPluginDescriptor();
			if(manager.isPluginActivated(p)){
				try {
					Plugin plugin = manager.getPlugin(p.getId());
					if (plugin instanceof TorrentTableJPopupMenuExtension)
						((TorrentTableJPopupMenuExtension)plugin).init(this);
				} catch (PluginLifecycleException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		

	}
	
	@Override
	public JMenuItem add(JMenuItem menuItem) {
		JMenuItem item = super.add(menuItem);
		item.addActionListener(this);
		return item;
	}
	
	
	public void show(TorrentTable table, int x, int y) {
		if(!table.getSelectionModel().isSelectionEmpty())
			super.show(table, x, y);
	}
	
	public void addTorrentTableActionListener(TorrentTableActionListener listener){
		listeners.add(listener);
	}
	

	public void actionPerformed(ActionEvent e) {
		int[] rows = table.getSelectedRows();
		Torrent[] tor = new Torrent[rows.length];
		
		for(int x = 0; x < rows.length; x++)
			tor[x] = table.getModel().getRow(rows[x]);
		
		for(TorrentTableActionListener x : listeners)
			x.torrentActionPerformed(tor, e.getActionCommand());
	}
	
}
