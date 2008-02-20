package ntorrent.session;

import javax.swing.JComponent;
import javax.swing.JLabel;

import ntorrent.io.xmlrpc.XmlRpcConnection;

import ntorrent.session.view.SessionFrame;
import ntorrent.torrenttable.TorrentTableController;
import ntorrent.viewmenu.ViewMenuController;
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

public class ConnectionSession {
	
	private final XmlRpcConnection connection;
	private final TorrentTableController ttc;
	private final ViewMenuController vmc;
	
	private final SessionFrame session = new SessionFrame();
	
	public ConnectionSession(XmlRpcConnection c) {
		connection = c;
		ttc = new TorrentTableController(connection);
		vmc = new ViewMenuController(ttc);
		session.getVsplit().add(vmc.getDisplay());
		session.getVsplit().add(ttc.getDisplay());
		session.getHsplit().add(new JLabel("tab components"));
	}
	
	public JComponent getDisplay(){
		return session;
		
	}
}
