package ntorrent.connection.ssh.model;

import ntorrent.connection.model.ConnectionProfile;
import ntorrent.io.xmlrpc.XmlRpcConnection;

public class SSHConnectionProfile implements ConnectionProfile {

	public boolean isConnectOnStartup() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setConnectOnStartup(boolean connectOnStartup) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SSHConnectionProfile getClonedInstance() throws CloneNotSupportedException {
		return this.getClonedInstance();
	}
}
