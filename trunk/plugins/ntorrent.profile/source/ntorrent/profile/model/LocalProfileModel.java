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
package ntorrent.profile.model;

import ntorrent.mvc.AbstractModel;

public class LocalProfileModel extends AbstractModel implements ClientProfileInterface {
	private static final long serialVersionUID = 1L;

	private String host = "127.0.0.1";
	
	private int socketport = 5000;
	
	private boolean autoConnect = false;

	private String name = "";
	
	public String getHost() {
		return host;
	}

	public boolean isAutoConnect() {
		return autoConnect;
	}

	public void setAutoConnect(boolean autoConnect) {
		this.autoConnect = autoConnect;
	}
	
	public int getSocketport() {
		return socketport;
	}
	
	public void setSocketport(int socketport) {
		this.socketport = socketport;
	}

	public Protocol getProtocol() {
		return Protocol.LOCAL;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		if(name.length() > 0)
			return name;
		else
			return host+":"+socketport;
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
