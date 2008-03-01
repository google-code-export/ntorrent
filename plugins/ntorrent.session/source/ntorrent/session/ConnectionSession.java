package ntorrent.session;

import java.util.Collection;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.java.plugin.Plugin;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.PluginManager.EventListener;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;

import ntorrent.Main;
import ntorrent.io.xmlrpc.XmlRpcConnection;

import ntorrent.session.view.SessionFrame;
import ntorrent.torrenttable.TorrentTableController;
import ntorrent.torrenttable.TorrentTableInterface;
import ntorrent.viewmenu.ViewMenuController;
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

public class ConnectionSession implements EventListener {
	
	private final XmlRpcConnection connection;
	private final TorrentTableInterface ttc;
	private final ViewMenuController vmc;
	
	private final SessionFrame session;
	
	private final PluginManager manager = Main.getPluginManager();
	private final PluginRegistry registry = manager.getRegistry();
	private final ExtensionPoint ext = registry.getExtensionPoint("ntorrent.session", "SessionExtension");
	
	private final HashMap<PluginDescriptor,Collection<PluginDescriptor>> dependencies = 
		new HashMap<PluginDescriptor, Collection<PluginDescriptor>>();
	
	public ConnectionSession(final XmlRpcConnection c) {
		connection = c;
		ttc = new TorrentTableController(connection);
		vmc = new ViewMenuController(ttc);
		session = new SessionFrame(
				new JComponent[] {
						vmc.getDisplay(),
						ttc.getTable().getDisplay(),
						new JPanel(),
						new JLabel("tab components"),
						new JLabel("statusbar")
						}
		);
				
		manager.registerListener(this);
		
		for(Extension e : ext.getConnectedExtensions()){
			PluginDescriptor owner = e.getDeclaringPluginDescriptor();
			dependencies.put(owner, registry.getDependingPlugins(owner));
			//System.out.println(owner+" <-- "+dependencies.get(owner));
		}
		
		while(!dependencies.isEmpty())
			for(PluginDescriptor owner : dependencies.keySet()){
				if(isExtensionSafeToLoad(owner)){
					System.out.println(owner+" this extension is safe to load");
					loadExtension(owner);
					dependencies.remove(owner);
					break;
				}else{
					System.out.println(owner+" this extension is not safe to load at the moment");
				}
			}	
	}
	
	private boolean isExtensionSafeToLoad(PluginDescriptor p){
		boolean safe = true;
		for(PluginDescriptor owner : dependencies.keySet()){
			for(PluginDescriptor dep : dependencies.get(owner)){
				if(p.equals(dep))
					safe = false;
			}
		}
		return safe;
	}
	
	private void loadExtension(PluginDescriptor p){
		if (manager.isPluginActivated(p)){
			try {
				pluginActivated(manager.getPlugin(p.getId()));
			} catch (PluginLifecycleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public SessionFrame getDisplay(){
		return session;
	}
	
	public XmlRpcConnection getConnection() {
		return connection;
	}
	
	public TorrentTableInterface getTorrentTableController() {
		return ttc;
	}

	public void pluginActivated(Plugin plugin) {
		if(plugin instanceof SessionExtension){
			((SessionExtension)plugin).init(this);
		}
	}
	public void pluginDeactivated(Plugin plugin) {}
	public void pluginDisabled(PluginDescriptor descriptor) {}
	public void pluginEnabled(PluginDescriptor descriptor) {}
}
