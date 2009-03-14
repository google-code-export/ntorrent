package ntorrent.connection.socket;

import java.awt.Component;

import ntorrent.connection.model.ConnectionProfileExtension;
import ntorrent.connection.model.ProxyProfile;
import ntorrent.connection.socket.model.SocketConnectionProfile;
import ntorrent.connection.socket.view.SocketConnectionView;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import ntorrent.locale.ResourcePool;

import org.apache.log4j.Logger;
import org.java.plugin.Plugin;

public class SocketConnectionController extends Plugin implements ConnectionProfileExtension {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SocketConnectionProfile connectionProfile = new SocketConnectionProfile();
	private transient SocketConnectionView display;
	private String name = null;
	private transient SocketConnection connection;
	private ProxyProfile proxyProfile;
	
	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(SocketConnectionController.class);

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

	public ConnectionProfileExtension getClonedInstance() throws CloneNotSupportedException {
		SocketConnectionController newObj = (SocketConnectionController) this.clone();
		newObj.connectionProfile = connectionProfile.getClonedInstance();
		newObj.display = new SocketConnectionView(newObj.connectionProfile);
		return newObj;
	}

	@Override
	public void saveEvent() {
		updateAndValidate();
	}

	@Override
	public void connectEvent() {
		updateAndValidate();
		this.connection = new SocketConnection(this.connectionProfile,this.proxyProfile);
	}
	
	private void updateAndValidate(){
		try{
			 display.updateModel();
		}catch (Exception e) {
			throw new IllegalArgumentException(
					ResourcePool.getString("validate.error", this),
					e
				);
		}
	}

	@Override
	public XmlRpcConnection getConnection() {
		return this.connection;
	}

	@Override
	public void setProxyConnectionInfo(ProxyProfile profile) {
		this.proxyProfile = profile;
	}

}
