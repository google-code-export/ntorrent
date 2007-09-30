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
package ntorrent.settings;

import java.io.IOException;



/**
 * @author   netbrain
 */
public class ProfileSettings extends Settings{
	private static final long serialVersionUID = 1L;
	public String host;
	public String username;
	
	public ProfileSettings() {
		try {
			if(!deserialize(Constants.profile,this)){
				this.host = "http://";
				this.username = "";
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	/**
	 * @return
	 */
	public String getHost() {
		return host;
	}
	
	/**
	 * @return
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * @param  host
	 */
	public void setHost(String host) {
		this.host = host;
	}
	
	/**
	 * @param  username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void saveSettings(){
		System.out.println("Saving profile.");
		try {
			serialize(Constants.profile,this);
		} catch (Exception e ){
			e.printStackTrace();
		}
	}
	
}
