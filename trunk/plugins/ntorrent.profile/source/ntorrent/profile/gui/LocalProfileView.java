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

import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ntorrent.env.Environment;
import ntorrent.profile.model.ClientProfileInterface;
import ntorrent.profile.model.LocalProfileModel;

public class LocalProfileView extends AbstractClientProfileView {
	private static final long serialVersionUID = 1L;
	
	LocalProfileModel model;
		
	public LocalProfileView(LocalProfileModel model) {
		super(model);
		this.model = model;
		model.addPropertyChangeListener(this);
		initDisplay();
		initListeners();
	}
	
	private void initListeners() {
		socket.addFocusListener(this);
		autoConn.addItemListener(this);
	}

	private void initDisplay(){
		socketLabel = new JLabel(Environment.getString("profile.socketport"));
		autoConnLabel = new JLabel(Environment.getString("profile.autoconnect"));
		socket = new JTextField(10);
		autoConn = new JCheckBox();
		
		socket.setText(""+model.getSocketport());
		autoConn.setSelected(model.isAutoConnect());

		add(socketLabel,socket);
		add(autoConnLabel,autoConn);
	}
	
	public JComponent getDisplay() {
		return p;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		//we dont have to care about property change events atm.
		/*
		String p = evt.getPropertyName();
		if(!evt.getSource().equals(this)){
			if(p.equals("profile.socketport")){
				socket.setText(""+evt.getNewValue());
			}else if(p.equals("profile.autoconnect")){
				autoConn.setSelected((Boolean) evt.getNewValue());
			}
		}*/
	}

	public void focusLost(FocusEvent e) {
		try{
			if(e.getComponent().equals(socket)){
				model.setSocketport(Integer.parseInt(socket.getText()));
				//model.firePropertyChangeEvent(this, "profile.socketport", null, model.getSocketport());
			}
		}catch(Exception x){
			errorOccured(x);
		}
	}

	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
	}

	public void itemStateChanged(ItemEvent e) {
		Object src = e.getSource();
		if(src.equals(autoConn)){
			model.setAutoConnect(autoConn.isSelected());
			//model.firePropertyChangeEvent(this, "profile.autoconnect", !model.isAutoConnect(), model.isAutoConnect());
		}
	}

	@Override
	public void setModel(ClientProfileInterface model) {
		this.model = (LocalProfileModel)model;
		super.setModel(model);
	}

	public void modelChange() {
		socket.setText(""+model.getSocketport());
		autoConn.setSelected(model.isAutoConnect());
	}
	
	
}
