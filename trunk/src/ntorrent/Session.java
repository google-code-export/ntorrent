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

import ntorrent.gui.SessionFrame;
import ntorrent.gui.TabbedPaneHolder;
import ntorrent.gui.profile.ClientProfile;
import ntorrent.gui.profile.Profile;
import ntorrent.gui.profile.ProfileRequester;
import ntorrent.io.settings.Constants;
import ntorrent.io.xmlrpc.XmRpcConnection;

/**
 * A ntorrent session
 */
public class Session extends Thread implements ProfileRequester{
	private static final long serialVersionUID = 1L;
	JComponent session;
	XmRpcConnection connection;
	ClientProfile profile;
	TabbedPaneHolder jtab;

	public Session(TabbedPaneHolder tph) {
		/**
		 * 1.Open profile menu
		 * 2.Start xmlrpc connection
		 * 3.Open main gui.
		 * 4.Start session threads.
		 */
		jtab = tph;
		session = new Profile(this);
		jtab.addTab(Constants.messages.getString("profile"), session);
	
	}

	public void sendProfile(ClientProfile p) {
		profile = p;
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		try {
			connection = new XmRpcConnection(profile);
			jtab.removeTab(session);
			session = new SessionFrame();
			jtab.addTab(profile.toString(), session);
		} catch (Exception e) {
			Logger.global.log(Level.WARNING, e.getMessage(), e);
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public JComponent getComponent() {
		return session;
	}
}
