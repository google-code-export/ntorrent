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
package ntorrent.torrentfiles;

import java.util.HashMap;

import org.java.plugin.Plugin;

import ntorrent.session.ConnectionSession;
import ntorrent.session.SessionExtension;
import ntorrent.torrenttable.model.Torrent;

/**
 * @author Kim Eik
 *
 */
public class TorrentFilesController extends Plugin implements SessionExtension {

	HashMap<ConnectionSession,TorrentFilesInstance> sessions = new HashMap<ConnectionSession, TorrentFilesInstance>();
	
	public void init(ConnectionSession session) {
		if(!sessions.containsKey(session)){
			sessions.put(session, new TorrentFilesInstance(session));
		}
		
		try{
			doStart();
		}catch(Exception x){
			x.printStackTrace();
		}
	}

	@Override
	protected void doStart() throws Exception {
		for(TorrentFilesInstance instance : sessions.values())
			if(!instance.isStarted())
				instance.start();
	}

	@Override
	protected void doStop() throws Exception {
		for(TorrentFilesInstance instance : sessions.values())
			if(instance.isStarted())
				instance.stop();
	}

	public void torrentsSelected(Torrent[] tor) {
		System.out.println(tor);
	}

}
