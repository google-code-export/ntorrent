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

package ntorrent;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ntorrent.core.view.component.util.Window;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import ntorrent.session.ConnectionSession;

/**
 * A ntorrent session
 */
public class Session implements Runnable{

	
	private final XmlRpcConnection connection;
	private ConnectionSession connectionSession;
	private final String name;

	public Session(String name, XmlRpcConnection connection) {
		this.name = name;
		this.connection = connection;
	}
	
	@Override
	public void run() {
		connectionSession = new ConnectionSession(this.connection);
		NtorrentApplication.MAIN_WINDOW.addToConnectionsTab(this.name, connectionSession.getDisplay());
		NtorrentApplication.MAIN_WINDOW.showConnectionsTab();
	}
	
	public XmlRpcConnection getConnection() {
		return this.connection;
	}

}
