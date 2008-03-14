package ntorrent.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Logger;

import ntorrent.data.Environment;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import ntorrent.session.view.SessionFrame;
import ntorrent.torrenttable.TorrentTableController;
import ntorrent.torrenttable.TorrentTableInterface;
import ntorrent.torrenttable.viewmenu.ViewMenuController;

import org.java.plugin.Plugin;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.PluginManager.EventListener;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;
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
	private final TorrentTableController ttc;
	private final ViewMenuController vmc;
	
	private final SessionFrame session;
	
	private final PluginManager manager = Environment.getPluginManager();
	private final PluginRegistry registry = manager.getRegistry();
	private final ExtensionPoint ext = registry.getExtensionPoint("ntorrent.session", "SessionExtension");
	
	private final HashMap<PluginDescriptor,Collection<PluginDescriptor>> dependencies = 
		new HashMap<PluginDescriptor, Collection<PluginDescriptor>>();
	
	private final Vector<SessionStateListener> sessionStateListener = new Vector<SessionStateListener>();
	
	private boolean started = true;
	private boolean shutdown = false;
	
	public ConnectionSession(final XmlRpcConnection c) {
		connection = c;
		ttc = new TorrentTableController(connection);
		vmc = new ViewMenuController(ttc);
		session = new SessionFrame(ttc,vmc);				
		manager.registerListener(this);
		loadExtensions();
	}
	
	private void loadExtensions(){
		for(Extension e : ext.getConnectedExtensions()){
			PluginDescriptor owner = e.getDeclaringPluginDescriptor();
			dependencies.put(owner, registry.getDependingPlugins(owner));
		}
		
		while(!dependencies.isEmpty())
			for(PluginDescriptor owner : dependencies.keySet()){
				if(isExtensionSafeToLoad(owner)){
					Logger.global.info(owner+" this extension is safe to load");
					loadExtension(owner);
					dependencies.remove(owner);
					break;
				}else{
					Logger.global.info(owner+" this extension is not safe to load at the moment");
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
	
	public boolean isStarted() {
		return started;
	}
	
	public boolean isShutdown() {
		return shutdown;
	}
	
	public void stop(){
		started = false;
		Logger.global.info("Stopping: "+connection.getProfile() + " ["+ttc+"]");
		ttc.pause();
		notifySessionStateListeners();
	}
	
	public void start() {
		started = true;
		Logger.global.info("Starting: "+connection.getProfile() + " ["+ttc+"]");
		ttc.unpause();
		notifySessionStateListeners();
	}
	
	public void shutdown(){
		shutdown = true;
		stop();
		Logger.global.info("Shutting down: "+connection.getProfile() + " ["+ttc+"]");
		ttc.shutdown();
	}
	
	private void notifySessionStateListeners(){
		for(SessionStateListener l : sessionStateListener)
			l.sessionStateChanged();
	}
	
	public void addSessionStateListener(SessionStateListener l){
		sessionStateListener.add(l);
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
