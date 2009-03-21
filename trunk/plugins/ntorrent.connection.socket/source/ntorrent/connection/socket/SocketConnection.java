package ntorrent.connection.socket;

import org.apache.log4j.Logger;

import ntorrent.connection.model.ProxyProfile;
import ntorrent.connection.socket.model.SocketConnectionProfile;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcSocketClient;

public class SocketConnection extends XmlRpcConnection {

	private SocketConnectionProfile profile;
	private ProxyProfile proxy;

	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(SocketConnection.class);
	
	public SocketConnection(SocketConnectionProfile profile, ProxyProfile proxy) throws XmlRpcException {
		super(profile);
		this.profile = profile;
		this.proxy = proxy;
	}

	@Override
	public void connect(){
		log.info("connecting to "+profile+" using "+proxy);
		//TODO supply proxy information to connection
		client = new XmlRpcSocketClient(
				profile.getHost(), 
				profile.getPort()
				);
	}

	@Override
	public void disconnect() {
		log.info("disconnecting");
		//TODO actually disconnect?
		client = null;
	}

	@Override
	public void reconnect() {
		connect();
	}

	@Override
	public boolean isConnected() {
		return ((XmlRpcSocketClient)client).getConnection().isConnected();
	}

}
