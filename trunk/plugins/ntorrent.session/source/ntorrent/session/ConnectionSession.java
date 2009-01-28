package ntorrent.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import ntorrent.data.Environment;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import ntorrent.session.view.SessionFrame;
import ntorrent.torrenttable.TorrentTableController;
import ntorrent.torrenttable.TorrentTableInterface;
import ntorrent.torrenttable.viewmenu.ViewMenuController;

import org.apache.log4j.Logger;
import org.java.plugin.Plugin;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.PluginManager.EventListener;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;

/**
 * This class handles a the gui and model of all elements within one connected session of nTorrent.
 * @author Kim Eik
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
	
	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(ConnectionSession.class);
	
	public ConnectionSession(final XmlRpcConnection c) {
		connection = c;
		ttc = new TorrentTableController(connection);
		vmc = new ViewMenuController(ttc);
		session = new SessionFrame(ttc,vmc);				
		manager.registerListener(this);
		loadExtensions();
	}
	
	/**
	 * Loads the plugins that is connected to this extension point in a correct order, where
	 * no plugins with dependencies are loaded after the depended plugins.
	 */
	private void loadExtensions(){
		for(Extension e : ext.getConnectedExtensions()){
			PluginDescriptor owner = e.getDeclaringPluginDescriptor();
			dependencies.put(owner, registry.getDependingPlugins(owner));
		}
		
		while(!dependencies.isEmpty())
			for(PluginDescriptor owner : dependencies.keySet()){
				if(isExtensionSafeToLoad(owner)){
					log.debug(owner+" this extension is safe to load");
					loadExtension(owner);
					dependencies.remove(owner);
					break;
				}else{
					log.debug(owner+" this extension is not safe to load at the moment");
				}
			}
	}
	
	/**
	 * Checks to see if a plugin is safe to load.
	 * @param p
	 * @return true if it is, false if not.
	 */
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
	
	/**
	 * Load the plugin
	 * @param p
	 */
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
	
	/**
	 * Returns the gui of this session.
	 * @return SessionFrame
	 */
	public SessionFrame getDisplay(){
		return session;
	}
	
	/**
	 * Returns the xmlrpc connection controller of this session.
	 * @return XmlRpcConnection
	 */
	public XmlRpcConnection getConnection() {
		return connection;
	}
	
	/**
	 * Returns the TorrentTableController in this session.
	 * @return
	 */
	public TorrentTableInterface getTorrentTableController() {
		return ttc;
	}
	
	/**
	 * This session is started if it is the selected pane.
	 * @return
	 */
	public boolean isStarted() {
		return started;
	}
	
	/**
	 * Returns boolean value on whether this session has been closed.
	 * @return
	 */
	public boolean isShutdown() {
		return shutdown;
	}
	
	/**
	 * Stops this session, causing background processes to pause.
	 */
	public void stop(){
		started = false;
		log.info("Stopping: "+connection + " ["+ttc+"]");
		ttc.pause();
		notifySessionStateListeners();
	}
	
	/**
	 * Starts this session, causing background processes to unpause.
	 */
	public void start() {
		started = true;
		log.info("Starting: "+connection + " ["+ttc+"]");
		ttc.unpause();
		notifySessionStateListeners();
	}
	
	/**
	 * Closes this session, freeing and unloading resources, stopping processes and so forth.
	 */
	public void shutdown(){
		shutdown = true;
		stop();
		log.info("Shutting down: "+connection + " ["+ttc+"]");
		ttc.shutdown();
	}
	
	/**
	 * Notifies SessionStateListener's to changes in this sessions state. (started,stopped,shutdown)
	 */
	private void notifySessionStateListeners(){
		for(SessionStateListener l : sessionStateListener)
			l.sessionStateChanged();
	}
	
	/**
	 * Adds a SessionStateListener.
	 * @param l
	 */
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
