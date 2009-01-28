package ntorrent.connection.socket;

import ntorrent.connection.socket.model.SocketConnectionProfile;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcSocketClient;

public class SocketConnection extends XmlRpcConnection {

	private SocketConnectionProfile profile;

	public SocketConnection(SocketConnectionProfile profile) throws XmlRpcException {
		super(profile);
		this.profile = profile;
	}

	@Override
	public void connect(){
		client = new XmlRpcSocketClient(
				profile.getHost(), 
				profile.getPort()
				);
	}

	@Override
	public void disconnect() {
		client = null;
	}

	@Override
	public void reconnect() {
		connect();
	}

}
