package ntorrent.connection.ssh;

import java.awt.Component;

import org.apache.log4j.Logger;
import org.java.plugin.Plugin;

import ntorrent.connection.model.ConnectionProfileExtension;
import ntorrent.connection.ssh.SSHConnectionController;
import ntorrent.connection.ssh.model.SSHConnectionProfile;
import ntorrent.connection.ssh.view.SSHConnectionView;
import ntorrent.locale.ResourcePool;

public class SSHConnectionController extends Plugin implements ConnectionProfileExtension<SSHConnectionProfile> {

	private final SSHConnectionView display = new SSHConnectionView();
	private final SSHConnectionProfile connectionProfile = new SSHConnectionProfile();
	
	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(SSHConnectionController.class);
	
	public SSHConnectionController() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public SSHConnectionProfile getConnectionProfile() {
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
		return ResourcePool.getString("connection.ssh", this);
	}

}
