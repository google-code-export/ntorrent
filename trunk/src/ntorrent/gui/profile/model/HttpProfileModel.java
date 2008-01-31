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
package ntorrent.gui.profile.model;

import java.io.CharArrayWriter;
import java.io.IOException;

import ntorrent.mvc.AbstractModel;

public class HttpProfileModel extends AbstractModel implements ClientProfileInterface {
	private static final long serialVersionUID = 1L;
	
	private String host = "";
	
	private int port = 80;
	
	private String mountpoint = "/RPC2";
	
	private String username = "";
	
	private char[] password = {};
	
	private boolean rememberpwd = false;
	
	private boolean autoConnect = false;
	
	private ProxyProfileModel proxy = new ProxyProfileModel();

	private String name = "";

	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getMountpoint() {
		return mountpoint;
	}
	
	public void setMountpoint(String mountpoint) {
		this.mountpoint = mountpoint;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		String out = "";
		try{
			for(char b : password)
				out += b;
		}catch(NullPointerException x){
			//do nothing
		}
		return out;		
	}
	
	public void setPassword(String password) {
		CharArrayWriter ch = new CharArrayWriter();
		for(char c : password.toCharArray()){
			ch.append(c);
		}
		this.password = ch.toCharArray();
	}
	
	public void setPassword(char[] password){
		this.password = password;
	}
	
	public void setRememberpwd(boolean rememberpwd) {
		this.rememberpwd = rememberpwd;
	}
	
	public boolean isRememberpwd() {
		return rememberpwd;
	}

	public boolean isAutoConnect() {
		return autoConnect;
	}

	public void setAutoConnect(boolean autoConnect) {
		this.autoConnect = autoConnect;
	}
		
	public ProxyProfileModel getProxy() {
		return proxy;
	}
	
	public Protocol getProtocol() {
		return Protocol.HTTP;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	 private void writeObject(java.io.ObjectOutputStream out) throws IOException{
		 if(!isRememberpwd()){
			password = new char[]{};
		 }
		out.defaultWriteObject();
	 }
	 
	    public Object clone() {
	        try {
	            return super.clone();
	        }
	        catch (CloneNotSupportedException e) {
	            // This should never happen
	            throw new InternalError(e.toString());
	        }
	    }

}
