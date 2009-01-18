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

	private final SocketConnectionView display = new SocketConnectionView();
	private final SocketConnectionProfile connectionProfile = new SocketConnectionProfile();
	
	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(SocketConnectionController.class);
	
	public SocketConnectionController() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public SocketConnectionProfile getConnectionProfile() {
		return connectionProfile;
	}

	@Override
	public Component getDisplay() {
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
	
	@Override
	public String toString() {
		return ResourcePool.getString("connection.socket", this);
	}

}
