package ntorrent.connection.model;

import ntorrent.io.xmlrpc.XmlRpcConnection;

public interface ConnectionProfile {
	
	
	/**
	 * Returns the connection
	 * TODO soon to be deprecated, as were implementing an extra layer between
	 * with the ntorrent protocol.
	 * @return
	 */
	public XmlRpcConnection getConnection();	
}
