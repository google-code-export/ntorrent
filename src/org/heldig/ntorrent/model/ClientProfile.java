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
package org.heldig.ntorrent.model;

import java.io.Serializable;

/**
 * @author Kim Eik
 *
 */
public class ClientProfile implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static enum Protocol {
		HTTP,SSH,LOCAL;
		@Override
		public String toString() {
			return this.name().toLowerCase()+"://";
		}
	}
	
	private Protocol prot;
	private String host;
	private String mount;
	private String username = "";
	private String password = "";
	private int port;
	
	public ClientProfile(Protocol p, String host,int port ,String mount) {
		prot = p;
		this.host = host;
		this.port = port;
		this.mount = mount;
	}
	
	public boolean isLocalHost(){
		return host == "127.0.0.1";
	}
	
	public String getHost() {
		return host;
	}
	
	public Protocol getProt() {
		return prot;
	}
	
	public String getMount() {
		return mount;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public boolean hasPassword(){
		return password.length() > 0;
	}
	
	@Override
	public String toString() {
		return prot+host+":"+port+mount;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ClientProfile))
			return false;
		ClientProfile profile = (ClientProfile)obj;
		return profile == this;
		
	}
	
}
