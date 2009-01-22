package ntorrent.connection.socket;

import java.awt.Component;

import org.apache.log4j.Logger;
import org.java.plugin.Plugin;

import ntorrent.connection.model.ConnectionProfileExtension;
import ntorrent.connection.socket.SocketConnectionController;
import ntorrent.connection.socket.model.SocketConnectionProfile;
import ntorrent.connection.socket.view.SocketConnectionView;
import ntorrent.locale.ResourcePool;

public class SocketConnectionController extends Plugin implements ConnectionProfileExtension<SocketConnectionProfile> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final SocketConnectionProfile connectionProfile = new SocketConnectionProfile();
	private transient SocketConnectionView display;
	private String name = null;
	
	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(SocketConnectionController.class);
	
	
	@Override
	public SocketConnectionProfile getConnectionProfile() {
		return connectionProfile;
	}

	@Override
	public Component getDisplay() {
		if(display == null){
			display = new SocketConnectionView(connectionProfile);
		}
		return display;
	}

	@Override
	protected void doStart() throws Exception {
		log.info("doStart() called");
	}

	@Override
	protected void doStop() throws Exception {
		log.info("doStop() called");
	}
	
	public String getName() {
		if(name == null)
			return ResourcePool.getString("connection.socket", this);
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	

}
