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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.net.Proxy;
import java.net.Proxy.Type;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ntorrent.env.Environment;
import ntorrent.mvc.AbstractView;
import ntorrent.profile.model.ProxyProfileModel;

public class ProxyProfileView extends AbstractView implements ItemListener, FocusListener {
	private static final long serialVersionUID = 1L;
	
	JPanel p = new JPanel(new GridLayout(3,2));
	ProxyProfileModel model;
	
	JComboBox types = new JComboBox(Proxy.Type.values());
	JTextField host,port;
	JLabel proxyLabel, hostLabel, portLabel;
	
	public ProxyProfileView(ProxyProfileModel model) {
		this.model = model;
		JLabel proxyLabel = new JLabel(Environment.getString("profile.proxy"));
		JLabel hostLabel = new JLabel(Environment.getString("profile.proxy.host"));
		JLabel portLabel = new JLabel(Environment.getString("profile.proxy.port"));
		
		host = new JTextField(10);
		port = new JTextField(10);
		
		host.setEnabled(false);
		port.setEnabled(false);
		
		add(proxyLabel,types);
		add(hostLabel,host);
		add(portLabel,port);
		
		types.addItemListener(this);
		host.addFocusListener(this);
		port.addFocusListener(this);
		
	}
	
	private void add(JLabel label, JComponent c){
		JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
		right.add(label);
		left.add(c);
		this.p.add(right);
		this.p.add(left);
	}
	
	public JComponent getDisplay() {
		return p;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		// not implemented atm.
		
	}

	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == ItemEvent.SELECTED){
			Type t = (Proxy.Type)e.getItem();
			model.setType(t);
			switch(t){
			case DIRECT:
				host.setEnabled(false);
				port.setEnabled(false);
				break;
			case HTTP:
			case SOCKS:
				host.setEnabled(true);
				port.setEnabled(true);
			}
		}
	}

	public void focusGained(FocusEvent e) {
		// not needed.
		
	}

	public void focusLost(FocusEvent e) {
		Object src = e.getSource();
		try{
			if(src.equals(host)){
				model.setHost(host.getText());
			}else if(src.equals(port)){
				model.setPort(Integer.parseInt(port.getText()));
			}
		}catch(Exception x){
			errorOccured(x);
		}
	}

	public void setModel(ProxyProfileModel model) {
		this.model = model;
	}
	
	public void modelChange() {
		types.setSelectedItem(model.getType());
		host.setText(model.getHost());
		port.setText(""+model.getPort());
	}


}