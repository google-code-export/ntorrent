package ntorrent.connection.model;

import java.io.Serializable;


public interface ConnectionProfile extends Serializable {
	public boolean isConnectOnStartup();
	public void setConnectOnStartup(boolean connectOnStartup);
}
