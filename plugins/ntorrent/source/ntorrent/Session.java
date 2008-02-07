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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import ntorrent.env.Environment;
import ntorrent.gui.SessionFrame;
import ntorrent.gui.TabbedPaneHolder;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import ntorrent.profile.ClientProfileController;
import ntorrent.profile.ProfileRequester;
import ntorrent.profile.model.ClientProfileInterface;

/**
 * A ntorrent session
 */
public class Session extends Thread implements ProfileRequester{
	private static final long serialVersionUID = 1L;
	JComponent session;
	XmlRpcConnection connection = null;
	ClientProfileInterface profile;
	TabbedPaneHolder jtab;

	public Session(TabbedPaneHolder tph) {
		/**
		 * 1.Open profile menu
		 * 2.Start xmlrpc connection
		 * 3.Open main gui.
		 * 4.Start session threads.
		 */
		jtab = tph;
		session = new ClientProfileController(this).getDisplay();
		jtab.addTab(Environment.getString("profile"), session);
	
	}
	
	public Session(TabbedPaneHolder tph, ClientProfileInterface p) {
		/**
		 * 1.Open profile menu
		 * 2.Start xmlrpc connection
		 * 3.Open main gui.
		 * 4.Start session threads.
		 */
		jtab = tph;
		sendProfile(p);
	
	}

	public void sendProfile(ClientProfileInterface  p) {
		profile = p;
		jtab.removeTab(session);
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		try {
			connection = new XmlRpcConnection(profile);
			session = new SessionFrame();
			jtab.addTab(profile.toString(), session);
		} catch (Exception e) {
			Logger.global.log(Level.WARNING, e.getMessage(), e);
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
	public boolean isConnected(){
		return !(connection == null);
	}
	
	public XmlRpcConnection getConnection() {
		return connection;
	}
	
	public ClientProfileInterface getProfile() {
		return profile;
	}
}
