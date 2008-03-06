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

import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ntorrent.gui.ConnectionTab;
import ntorrent.gui.MainWindow;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import ntorrent.locale.ResourcePool;
import ntorrent.profile.ClientProfileController;
import ntorrent.profile.ProfileRequester;
import ntorrent.profile.model.ClientProfileInterface;
import ntorrent.session.ConnectionSession;

/**
 * A ntorrent session
 */
public class Session extends Thread implements ProfileRequester, ChangeListener{
	private static final long serialVersionUID = 1L;
	private JComponent sessionView;
	private ConnectionSession session;
	private XmlRpcConnection connection = null;
	private ClientProfileInterface profile;
	private ConnectionTab jtab;

	public Session(MainWindow window) {
		/**
		 * 1.Open profile menu
		 * 2.Start xmlrpc connection
		 * 3.Open main gui.
		 * 4.Start session threads.
		 */
		jtab = window.getConnectionsTab();
		sessionView = new ClientProfileController(this).getDisplay();
		
		int index = jtab.getTabCount();
		jtab.insertTab(ResourcePool.getString("profile","locale",this), null, sessionView, null, index);
		jtab.setSelectedIndex(index);
	}
	
	public Session(MainWindow window, ClientProfileInterface p) {
		/**
		 * 1.Open profile menu
		 * 2.Start xmlrpc connection
		 * 3.Open main gui.
		 * 4.Start session threads.
		 */
		jtab = window.getConnectionsTab();
		sendProfile(p);
	}

	public void sendProfile(ClientProfileInterface  p) {
		profile = p;
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		try {
			int tabIndex = jtab.getSelectedIndex();
			connection = new XmlRpcConnection(profile);
			session = new ConnectionSession(connection);
			sessionView = session.getDisplay();
			
			if(tabIndex >= 0){
				jtab.setSelectedIndex(-1);
				jtab.removeTabAt(tabIndex);
			}else
				tabIndex = 0;
			
			jtab.insertTab(profile.toString(), null, sessionView, null, tabIndex);
			jtab.setSelectedIndex(tabIndex);
			jtab.getModel().addChangeListener(this);
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
	
	public ConnectionSession getSession() {
		return session;
	}

	public void stateChanged(ChangeEvent e) {
		boolean removed = true;
		for(Component c :jtab.getComponents())
			if(c.equals(sessionView))
				removed = false;
		
		if(removed || jtab.getComponentCount() == 0)
			session.shutdown();
		
		if(jtab.getSelectedIndex() >= 0){
			if(jtab.getSelectedComponent().equals(sessionView)){
				session.start();
			}else{
				session.stop();
			}
		}
	}
	


}
