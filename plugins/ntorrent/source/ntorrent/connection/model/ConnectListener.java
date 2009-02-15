package ntorrent.connection.model;

import ntorrent.io.xmlrpc.XmlRpcConnection;

public interface ConnectListener {
	public void connect(final XmlRpcConnection connection) throws Exception;
}
