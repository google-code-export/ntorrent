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
package ntorrent.profile.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ntorrent.mvc.AbstractView;
import ntorrent.profile.model.ClientProfileInterface;

public abstract class AbstractClientProfileView extends AbstractView implements FocusListener, ItemListener{
	protected static final String guiBundle = "locale";
	protected ClientProfileInterface model;
	
	JPanel p = new JPanel(new GridLayout(0,2));
	
	/**Gui elements**/
	protected JLabel hostLabel,portLabel,mountpointLabel,usernameLabel,
			passwordLabel,rememberPwdLabel,autoConnLabel, socketLabel;
	protected JTextField host,port,mountpoint,username,socket;
	protected JPasswordField password;
	protected JCheckBox autoConn,rememberpwd;
	
	
	public AbstractClientProfileView(ClientProfileInterface model) {
		this.model = model;
	}
	
	protected void add(JLabel label, JComponent c){
		JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
		right.add(label);
		left.add(c);
		this.p.add(right);
		this.p.add(left);
	}
	
	public ClientProfileInterface getModel() {
		return model;
	}
	
	public void setModel(ClientProfileInterface model) {
		this.model = model;
		modelChange();
	}
	
	public String toString() {
		return model.getProtocol().name();
	}
		
}
