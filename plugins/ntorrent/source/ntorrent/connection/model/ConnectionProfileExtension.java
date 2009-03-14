package ntorrent.connection.model;

import java.awt.Component;
import java.io.Serializable;

import ntorrent.io.xmlrpc.XmlRpcConnection;

public interface ConnectionProfileExtension extends Serializable,Cloneable {	
	
	/**
	 * This method should return a Component that is to be displayed
	 * in as the input form on the profile connection profile setup.
	 * 
	 * Since this interface is subject to serialization, a good implementation
	 * of this method could be as follows:
	 * 
	 * 	private transient Component display;
	 *  ... 
	 * 	if(display == null){
	 *		display = new ConnectionView(connectionProfile);
	 *	}
	 *	return display;
	 *
	 * @return Component
	 */
	public Component getDisplay();

	/**
	 * Returns a human friendly name of this object.
	 * This method should return by default a name that describes
	 * this connection profile.
	 * @return
	 */
	public String getName();
	
	/**
	 * Sets a human friendly name of this object.
	 * This method will be called upon saving a connection profile
	 * to set a user specified name.
	 */
	public void setName(String name);
	
	/**
	 * Attaches proxy information to the connection.
	 * @param profile
	 */
	public void setProxyConnectionInfo(ProxyProfile profile);
	
	/**
	 * Returns a cloned object of this instance,
	 * the implementation should include cloning of object references.
	 * @return ConnectionProfileExtension
	 * @throws CloneNotSupportedException, should never throw this exception.
	 */
	public ConnectionProfileExtension getClonedInstance() throws CloneNotSupportedException;
	
	/**
	 * This method is executed on when the save button is clicked. 
	 * The recieving class should update the ConnectionProfile model 
	 * and validate input fields on this event.
	 * @throws IllegalArgumentException when incorrect input is specified.
	 */
	public void saveEvent() throws IllegalArgumentException;

	/**
	 * This method is executed when the connect button is clicked.
	 * The recieving class should initialize the connection 
	 * object and validate input fields.
	 * @throws IllegalArgumentException when incorrect input is specified.
	 */
	public void connectEvent() throws IllegalArgumentException;
	
	/**
	 * Returns the connection object.
	 */
	public XmlRpcConnection getConnection();
	
}
