/**
 *   nTorrent - A GUI client to administer a rtorrent process 
 *   over a network connection.
 *   
 *   Copyright (C) 2007  Kim Eik
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ntorrent.gui.profile;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.swing.JPasswordField;

/**
 * @author Kim Eik
 *
 */
public class ClientProfile implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	public enum Protocol implements Serializable {
		SSH,HTTP,LOCAL
	}
	
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	public @interface metadata{
		String label();
		Protocol[] protocols() default {Protocol.SSH,Protocol.HTTP,Protocol.LOCAL};
		String[] value() default {"","",""};
	}

	
	@metadata(label = "profile.protocol")
	private Protocol protocol;
	
	@metadata(
			label = "profile.host",
			protocols = {Protocol.SSH,Protocol.HTTP}, 
			value = {"","","127.0.0.1"})
	private String host;
	
	@metadata(
			label = "profile.connectionport",
			protocols = {Protocol.SSH,Protocol.HTTP} ,
			value = {"22","80",""}
			)
	private int port;
	
	@metadata(label = "profile.socketport")
	private int socketPort;
	
	@metadata(
			label = "profile.mountpoint", 
			protocols={Protocol.HTTP}, 
			value={"","/RPC2",""}
			)
	private String mountPoint;
	
	@metadata(
			label = "profile.username",
			protocols = {Protocol.SSH,Protocol.HTTP}
			)
	private String username;
	
	@metadata(
			label = "profile.password",
			protocols = {Protocol.SSH,Protocol.HTTP}
			)
	private String password;
	
	@metadata(
			label = "profile.rememberpwd",
			protocols = {Protocol.SSH,Protocol.HTTP},
			value = {"0","0","0"}
			)
	private boolean rememberPassword;
	
	@metadata(
			label = "profile.autoconnect",
			value = {"0","0","0"}
			)
	private boolean autoConnect;
	
	private String name;
	
	public ClientProfile(String name) {
		this.name = name;
	}
	
	public final Protocol getProtocol() {
		return protocol;
	}
	public final void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}
	public final String getHost() {
		return host;
	}
	public final void setHost(String host) {
		this.host = host;
	}
	public final int getPort() {
		return port;
	}
	public final void setPort(int port) {
		this.port = port;
	}
	public final int getSocketPort() {
		return socketPort;
	}
	public final void setSocketPort(int socketPort) {
		this.socketPort = socketPort;
	}
	public final String getMountPoint() {
		return mountPoint;
	}
	public final void setMountPoint(String mountPoint) {
		this.mountPoint = mountPoint;
	}
	public final String getUsername() {
		return username;
	}
	public final void setUsername(String username) {
		this.username = username;
	}
	public final String getPassword() {
		return this.password;
	}
	public final void setPassword(String password) {
		this.password = password;
	}
	public final boolean isRememberPassword() {
		return rememberPassword;
	}
	public final void setRememberPassword(boolean rememberPassword) {
		this.rememberPassword = rememberPassword;
	}
	public final boolean isAutoConnect() {
		return autoConnect;
	}
	public final void setAutoConnect(boolean autoConnect) {
		this.autoConnect = autoConnect;
	}
	public static final long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	public String toString() {
		return name;
	}
	

}
