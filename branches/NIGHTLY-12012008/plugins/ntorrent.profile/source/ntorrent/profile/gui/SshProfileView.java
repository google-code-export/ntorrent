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

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ntorrent.locale.ResourcePool;
import ntorrent.profile.model.ClientProfileInterface;
import ntorrent.profile.model.ProxyProfileModel;
import ntorrent.profile.model.SshProfileModel;

public class SshProfileView extends AbstractClientProfileView {
	private static final long serialVersionUID = 1L;
	
	SshProfileModel model;
	final ProxyProfileView proxy;
	
	
	public SshProfileView(SshProfileModel model) {
		super(model);
		this.model = model;
		this.proxy = new ProxyProfileView(model.getProxy());
		model.addPropertyChangeListener(this);
		initDisplay();
		initListeners();
	}
	
	private void initListeners() {
		host.addFocusListener(this);
		port.addFocusListener(this);
		socket.addFocusListener(this);
		username.addFocusListener(this);
		password.addFocusListener(this);
		rememberpwd.addItemListener(this);
		autoConn.addItemListener(this);
	}

	private void initDisplay(){
		hostLabel = new JLabel(ResourcePool.getString("host",this));
		portLabel = new JLabel(ResourcePool.getString("connectionport",this));
		usernameLabel = new JLabel(ResourcePool.getString("username",this));
		passwordLabel = new JLabel(ResourcePool.getString("password",this));
		rememberPwdLabel = new JLabel(ResourcePool.getString("rememberpwd",this));
		autoConnLabel = new JLabel(ResourcePool.getString("autoconnect",this));
		socketLabel = new JLabel(ResourcePool.getString("socketport",this));
		
		socket = new JTextField(10);
		host = new JTextField(10);
		port = new JTextField(10);
		username = new JTextField(10);
		password = new JPasswordField(10);
		rememberpwd = new JCheckBox();
		autoConn = new JCheckBox();
		
		socket.setText(""+model.getSocketport());
		port.setText(""+model.getPort());
	
		add(hostLabel,host);
		add(portLabel, port);
		add(socketLabel, socket);
		add(usernameLabel, username);
		add(passwordLabel, password);
		add(rememberPwdLabel, rememberpwd);
		add(autoConnLabel, autoConn);
	}
	
	public JComponent getDisplay() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(p);
		panel.add(proxy.getDisplay(),BorderLayout.SOUTH);
		return panel;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		//we dont have to care about property change events atm.
	}

	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void focusLost(FocusEvent e) {
		Object src = e.getSource();
		try{
			if(src.equals(host)){
				model.setHost(host.getText());
			}else if(src.equals(port)){
				model.setPort(Integer.parseInt(port.getText()));
			}else if(src.equals(socket)){
				model.setSocketport(Integer.parseInt(socket.getText()));
			}else if(src.equals(username)){
				model.setUsername(username.getText());
			}else if(src.equals(password)){
				model.setPassword(password.getPassword());
			}
		}catch(Exception x){
			errorOccured(x);
		}
	}

	public void itemStateChanged(ItemEvent e) {
		Object src = e.getSource();
		if(src.equals(autoConn)){
			model.setAutoConnect(autoConn.isSelected());
			//model.firePropertyChangeEvent(this, "profile.autoconnect", !model.isAutoConnect(), model.isAutoConnect());
		} else if(src.equals(rememberpwd)){
			model.setRememberpwd(rememberpwd.isSelected());
			//model.firePropertyChangeEvent(this, "profile.rememberpwd", !model.isRememberpwd(), model.isRememberpwd());
		}
	}

	@Override
	public void setModel(ClientProfileInterface model) {
		this.model = (SshProfileModel)model;
		ProxyProfileModel proxyModel = (ProxyProfileModel) this.model.getProxy().clone();
		this.proxy.setModel(proxyModel);
		super.setModel(model);
	}

	public void modelChange() {
		socket.setText(""+model.getSocketport());
		host.setText(model.getHost());
		port.setText(""+model.getPort());
		username.setText(model.getUsername());
		password.setText(model.getPassword());
		rememberpwd.setSelected(model.isRememberpwd());
		autoConn.setSelected(model.isAutoConnect());
	}

}
