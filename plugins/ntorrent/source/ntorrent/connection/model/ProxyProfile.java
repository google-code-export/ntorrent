package ntorrent.connection.model;

import java.io.Serializable;
import java.net.Proxy.Type;

public interface ProxyProfile extends Serializable,Cloneable {
	public Type getProxyType();
	public String getHost();
	public Integer getPort();
	public boolean usingAuthentication();
	public String getUsername();
	public String getPassword();
	public ProxyProfile getClonedInstance() throws CloneNotSupportedException;
}
