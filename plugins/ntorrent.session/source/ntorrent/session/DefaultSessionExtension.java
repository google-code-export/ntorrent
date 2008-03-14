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
package ntorrent.session;

import java.util.HashMap;

import org.java.plugin.Plugin;

/**
 * This is a default implementation of SessionExtension interface.
 * @author Kim Eik
 *
 */
public abstract class DefaultSessionExtension<T extends SessionInstance> extends Plugin implements SessionExtension {

	HashMap<ConnectionSession, T> sessions = 
		new HashMap<ConnectionSession,T>();
	
	public void init(ConnectionSession session) {
		if(!sessions.containsKey(session)){
			sessions.put(session, getNewSessionInstance(session));
		}
		
		try{
			doStart();
		}catch(Exception x){
			x.printStackTrace();
		}
	}
	
	protected abstract T getNewSessionInstance(ConnectionSession session);
	
	@Override
	protected void doStart() throws Exception {
		for(T instance : sessions.values())
			if(!instance.isStarted())
				instance.start();
	}

	@Override
	protected void doStop() throws Exception {
		for(T instance : sessions.values())
			if(instance.isStarted())
				instance.stop();
	}

}
