package ntorrent.connection.http;

import java.awt.Component;

import org.apache.log4j.Logger;
import org.java.plugin.Plugin;

import ntorrent.connection.http.model.HTTPConnectionProfile;
import ntorrent.connection.http.view.HTTPConnectionView;
import ntorrent.connection.model.ConnectionProfileExtension;
import ntorrent.connection.model.ProxyProfile;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import ntorrent.locale.ResourcePool;

public class HTTPConnectionController extends Plugin implements ConnectionProfileExtension {

	private static final long serialVersionUID = 1L;
	private final HTTPConnectionView display = new HTTPConnectionView();
	private final HTTPConnectionProfile connectionProfile = new HTTPConnectionProfile();
	private String name;
	
	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(HTTPConnectionController.class);
	
	public HTTPConnectionController() {
		// TODO Auto-generated constructor stub
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

	public String getName() {
		if(name == null)
			return ResourcePool.getString("connection.http", this);
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public HTTPConnectionController getClonedInstance() throws CloneNotSupportedException {
		return (HTTPConnectionController) this.clone();
	}

	public void saveEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectEvent() {
		// TODO Auto-generated method stub
	}

	@Override
	public XmlRpcConnection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProxyConnectionInfo(ProxyProfile profile) {
		// TODO Auto-generated method stub
		
	}
}
