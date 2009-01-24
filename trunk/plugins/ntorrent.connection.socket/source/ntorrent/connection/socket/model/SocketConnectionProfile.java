package ntorrent.connection.socket.model;

import ntorrent.connection.model.ConnectionProfile;
import ntorrent.io.xmlrpc.XmlRpcConnection;

public class SocketConnectionProfile implements ConnectionProfile {
	private static final long serialVersionUID = 1L;
	private String host = "localhost";
	private String port;
	private boolean connectOnStartup;

	public String getHost() {
		return host;
	}
	
	public String getPort() {
		return port;
	}
	
	public boolean isConnectOnStartup() {
		return connectOnStartup;
	}
	
	public void setConnectOnStartup(boolean connectOnStartup) {
		this.connectOnStartup = connectOnStartup;
	}
	
	public void setPort(String port) {
		this.port = port;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public SocketConnectionProfile getClonedInstance() throws CloneNotSupportedException {
		return (SocketConnectionProfile) this.clone();
	}
}
