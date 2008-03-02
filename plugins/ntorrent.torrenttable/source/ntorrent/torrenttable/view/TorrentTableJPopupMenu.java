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

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ntorrent.locale.ResourcePool;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentSelectionListener;
import ntorrent.torrenttable.model.TorrentTableActionListener;

import org.java.plugin.Plugin;
import org.java.plugin.registry.PluginDescriptor;

public class TorrentTableJPopupMenu extends JPopupMenu implements ActionListener, TorrentSelectionListener{
	private static final long serialVersionUID = 1L;
	private static final String bundle = "locale";
	
	Vector<TorrentTableActionListener> listeners = new Vector<TorrentTableActionListener>();
	Vector<String> extensions = new Vector<String>();
	Map<String,JMenuItem> menuItems = new HashMap<String,JMenuItem>();
	
	private Torrent[] selection = new Torrent[]{};
	
	public final static String[] mitems = {
		"start",
		"stop",
		"remove",
		"checkhash"
		};
	
	public final static String[] priorityMenu = {
		"setpriority",
		"setpriority.high",
		"setpriority.normal",
		"setpriority.low",
		"setpriority.off"
		};
	
	public TorrentTableJPopupMenu() {
		for(String s : mitems){
			JMenuItem item = new JMenuItem(ResourcePool.getString(s,bundle,this));
			item.setActionCommand(s);
			item.addActionListener(this);
			super.add(item);
		}
		
		JMenu priority = new JMenu(ResourcePool.getString(priorityMenu[0],bundle,this));
		super.add(priority);
		
		for(int x = 1; x < priorityMenu.length; x++){
			JMenuItem item = priority.add(ResourcePool.getString(priorityMenu[x],bundle,this));
			item.setActionCommand(priorityMenu[x]);
			item.addActionListener(this);
		}

	}
	
	@Override
	public JMenuItem add(JMenuItem menuItem) {
		JMenuItem item = super.add(menuItem);
		item.addActionListener(this);
		return item;
	}
	
	@Override
	public JMenuItem add(String s) {
		JMenuItem item = super.add(s);
		item.addActionListener(this);
		return item;
	}
	
	
	public void show(TorrentTable table, int x, int y) {
		if(!table.getSelectionModel().isSelectionEmpty())
			super.show(table, x, y);
	}
	
	public void addTorrentTableActionListener(TorrentTableActionListener listener){
		if(!listeners.contains(listener))
			listeners.add(listener);
	}
	

	public void actionPerformed(ActionEvent e) {
		for(TorrentTableActionListener x : listeners)
			x.torrentActionPerformed(selection, e.getActionCommand());
	}


	public void pluginDeactivated(Plugin plugin) {}
	public void pluginDisabled(PluginDescriptor descriptor) {}
	public void pluginEnabled(PluginDescriptor descriptor) {}

	public void torrentsSelected(Torrent[] tor) {
		selection = tor;
	}
	
}
