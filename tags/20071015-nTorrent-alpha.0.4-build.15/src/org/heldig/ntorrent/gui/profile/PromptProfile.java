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
package org.heldig.ntorrent.gui.profile;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.heldig.ntorrent.event.ControllerEventListener;
import org.heldig.ntorrent.gui.Window;
import org.heldig.ntorrent.language.Language;
import org.heldig.ntorrent.settings.Constants;
import org.heldig.ntorrent.settings.Settings;

/**
 * @author Kim Eik
 *
 */
public class PromptProfile extends Settings implements ActionListener, ItemListener, MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient ControllerEventListener event;
	private transient Window window = new Window("Profiles");
	private Vector<ClientProfile> vprofiles = new Vector<ClientProfile>();
	private transient JList profiles;
	private transient Language[] labels = {
			Language.Profile_protocol,
			Language.Profile_host,
			Language.Profile_connection_port,
			Language.Profile_socket_port,
			Language.Profile_mountpoint,
			Language.Profile_username,
			Language.Profile_password,
			null
			};
	
	private transient Component[] comps = {
			new JComboBox(),
			new JTextField(),
			new JTextField(),
			new JTextField(),
			new JTextField(),
			new JTextField(),
			new JPasswordField(),
			new JCheckBox(Language.Profile_remember_password)
	};
	
	private transient Language[] buttons = {
			Language.Profile_connect,
			Language.Profile_save_profile,
			Language.Profile_delete_profile
	};
	
	private transient ClientProfile selected;
	
	public PromptProfile(ControllerEventListener e) {
		event = e;
		try {
			deserialize(Constants.profile,this);
		} catch (Exception x) {
			x.printStackTrace();
		}
		
		for(Component c : comps)
			c.setEnabled(false);
		
		window.setLayout(new BorderLayout(10,20));
		window.setAlwaysOnTop(true);
		
		JComboBox box = new JComboBox(ClientProfile.Protocol.values());
		box.setSelectedIndex(-1);
		box.addItemListener(this);
		comps[0] = box;
		
		profiles = new JList(vprofiles);
		profiles.setFixedCellWidth(300);
		profiles.setFixedCellHeight(40);
		profiles.addMouseListener(this);
		window.add(new JScrollPane(profiles),BorderLayout.EAST);

		JPanel input = new JPanel(new GridLayout(0,1));
		for(int x = 0; x < labels.length; x++){
			if(labels[x] != null)
				input.add(new JLabel(labels[x].toString()),BorderLayout.WEST);
			input.add(comps[x],BorderLayout.WEST);
		}
		window.add(input);
		
		JPanel buttonpanel = new JPanel();
		for(Language s : buttons){
			JButton button = new JButton(s);
			button.addActionListener(this);
			buttonpanel.add(button);
		}
		
		window.add(buttonpanel,BorderLayout.SOUTH);
		window.drawWindow();
	}
	

	
	@Override
	protected void restoreData(Object obj) {
		PromptProfile pf = (PromptProfile)obj;
		this.vprofiles = pf.vprofiles;
		
	}
	
	public ClientProfile getClientProfile(){
		return getClientProfile(false);
	}
	
	@SuppressWarnings("deprecation")
	private ClientProfile getClientProfile(boolean filter){
		try{
			ClientProfile.Protocol prot = (ClientProfile.Protocol)((JComboBox)comps[0]).getSelectedItem();
			String host = ((JTextField)comps[1]).getText();
			JTextField connport = ((JTextField)comps[2]);
			JTextField sockport = ((JTextField)comps[3]);
			JTextField mount = ((JTextField)comps[4]);
			JTextField username = ((JTextField)comps[5]);
			JTextField password = ((JPasswordField)comps[6]);
			ClientProfile profile = new ClientProfile(prot,host);
			profile.setConnectionPort(connport.isEnabled() ? Integer.parseInt(connport.getText()) : 0);
			profile.setSocketPort(sockport.isEnabled() ? Integer.parseInt(sockport.getText()) : 0);
			profile.setMount(mount.isEnabled() ? mount.getText() : "");
			profile.setUsername(username.isEnabled() ? username.getText() : "");
			if(!filter || ((JCheckBox)comps[7]).isSelected())
				profile.setPassword(password.isEnabled() ? password.getText() : "");
			return profile;
		}catch(Exception x){
			JOptionPane.showMessageDialog(window,
				    x.getLocalizedMessage(),
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			x.printStackTrace();
		}
		return null;
	}
		
	public void actionPerformed(ActionEvent e) {
		switch(Language.getFromString(e.getActionCommand())){
		case Profile_connect:
			window.closeWindow();
			event.connect(getClientProfile());
			break;
		case Profile_save_profile:
			vprofiles.add(getClientProfile(true));
			profiles.setListData(vprofiles);
			break;
		case Profile_delete_profile:
			if(JOptionPane.showConfirmDialog(window, 
					"Are you sure?",
					"Removing profile",
					JOptionPane.YES_NO_OPTION) == 0){
				vprofiles.remove(selected);
				profiles.setListData(vprofiles);
			}
			break;
		default:
			System.out.println(e);
		}
		
		try {
			serialize(Constants.profile, this);
		} catch (Exception z) {
			// TODO Auto-generated catch block
			z.printStackTrace();
		}		
	}

	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == ItemEvent.SELECTED){
			for(Component c : comps)
				c.setEnabled(true);
			switch((ClientProfile.Protocol)e.getItem()){
			case LOCAL:
				((JTextField)comps[1]).setText("127.0.0.1");
				((JTextField)comps[2]).setText("");
				comps[1].setEnabled(false);
				comps[2].setEnabled(false);
				comps[4].setEnabled(false);
				comps[5].setEnabled(false);
				comps[6].setEnabled(false);
				comps[7].setEnabled(false);
				break;
			case HTTP:
				((JTextField)comps[2]).setText("80");
				comps[3].setEnabled(false);
				break;
			case SSH:
				((JTextField)comps[2]).setText("22");
				comps[4].setEnabled(false);
				break;
			}	
		}
	}



	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {
		mousePressed(e);
	}

	public void mousePressed(MouseEvent e) {
		ClientProfile profile = ((ClientProfile)((JList)e.getSource()).getSelectedValue());
		if(profile != null){
			selected = profile;
			((JComboBox)comps[0]).setSelectedItem(profile.getProt());
			((JTextField)comps[1]).setText(profile.getHost());
			((JTextField)comps[2]).setText(""+profile.getConnectionPort());
			((JTextField)comps[3]).setText(""+profile.getSocketPort());
			((JTextField)comps[4]).setText(profile.getMount());
			((JTextField)comps[5]).setText(profile.getUsername());
			((JPasswordField)comps[6]).setText(profile.getPassword());
			((JCheckBox)comps[7]).setSelected(profile.hasPassword());
		}
		
	}

}
