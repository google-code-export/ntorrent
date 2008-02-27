package ntorrent.session;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.java.plugin.Plugin;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;

import ntorrent.env.Environment;
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

public class ConnectionSession {
	
	private final XmlRpcConnection connection;
	private final TorrentTableInterface ttc;
	private final ViewMenuController vmc;
	
	private final SessionFrame session;
	
	public ConnectionSession(XmlRpcConnection c) {
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
		
		PluginManager manager = Environment.getPluginManager();
		PluginRegistry registry = manager.getRegistry();
		ExtensionPoint ext = registry.getExtensionPoint("ntorrent.session", "SessionExtension");
		for(Extension e : ext.getAvailableExtensions()){
			PluginDescriptor p = e.getDeclaringPluginDescriptor();
			if (manager.isPluginActivated(p)){
				try {
					Plugin plugin = manager.getPlugin(p.getId());
					if(plugin instanceof SessionExtension){
						((SessionExtension)plugin).init(this);
					}else{
						throw new Exception(p+" does not implement SessionExtension interface");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
	}
	
	public SessionFrame getDisplay(){
		return session;
	}
	
	public XmlRpcConnection getConnection() {
		return connection;
	}
}
