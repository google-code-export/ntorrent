package ntorrent.connection.http.model;

import ntorrent.connection.model.ConnectionProfile;
import ntorrent.io.xmlrpc.XmlRpcConnection;

public class HTTPConnectionProfile implements ConnectionProfile {

	public boolean isConnectOnStartup() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setConnectOnStartup(boolean connectOnStartup) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HTTPConnectionProfile getClonedInstance() throws CloneNotSupportedException {
		return this.getClonedInstance();
	}

}
