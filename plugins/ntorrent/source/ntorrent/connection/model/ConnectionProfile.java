package ntorrent.connection.model;

import java.io.Serializable;


public interface ConnectionProfile extends Serializable,Cloneable {
	public boolean isConnectOnStartup();
	public void setConnectOnStartup(boolean connectOnStartup);
	public ConnectionProfile getClonedInstance() throws CloneNotSupportedException;
}
