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
package org.heldig.ntorrent.gui.dialogue;

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

import org.heldig.ntorrent.Controller;
import org.heldig.ntorrent.gui.Window;
import org.heldig.ntorrent.model.ClientProfile;
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
	private transient Controller C;
	private transient Window window = new Window("Profiles");
	private Vector<ClientProfile> vprofiles = new Vector<ClientProfile>();
	private transient JList profiles;
	private transient String[] labels = {
			"Protocol",
			"Host",
			"Connection port",
			"Socket port",
			"Mountpoint",
			"Username",
			"Password",
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
			new JCheckBox("Remember password?")
	};
	
	private transient String[] buttons = {
			"Connect",
			"Save profile",
			"Delete profile"
	};
	
	private transient ClientProfile selected;
	
	public PromptProfile(Window parent, Controller controller) {
		C = controller;
		try {
			deserialize(Constants.profile,this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		window.setLayout(new BorderLayout(10,20));
		window.setAlwaysOnTop(true);
		
		JComboBox box = new JComboBox(ClientProfile.Protocol.values());
		box.addItemListener(this);
		box.setSelectedIndex(-1);
		comps[0] = box;
		
		profiles = new JList(vprofiles);
		profiles.setFixedCellWidth(300);
		profiles.setFixedCellHeight(40);
		profiles.addMouseListener(this);
		window.add(new JScrollPane(profiles),BorderLayout.EAST);

		JPanel input = new JPanel(new GridLayout(0,1));
		for(int x = 0; x < labels.length; x++){
			input.add(new JLabel(labels[x]),BorderLayout.WEST);
			input.add(comps[x],BorderLayout.WEST);
		}
		window.add(input);
		
		JPanel buttonpanel = new JPanel();
		for(String s : buttons){
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
	
	@SuppressWarnings("deprecation")
	private ClientProfile toClientProfile(){
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
			if(((JCheckBox)comps[7]).isSelected())
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
		String cmd = e.getActionCommand();
		ClientProfile profile = toClientProfile();
		if(cmd.equals(buttons[0])){
			connect(profile);
		}else if(cmd.equals(buttons[1])){
			vprofiles.add(profile);
			profiles.setListData(vprofiles);
		}else if(cmd.equals(buttons[2])){
			if(JOptionPane.showConfirmDialog(window, "Are you sure?","Removing profile",JOptionPane.YES_NO_OPTION) == 0){
				vprofiles.remove(selected);
				profiles.setListData(vprofiles);
			}
		}
		try {
			serialize(Constants.profile, this);
		} catch (Exception z) {
			// TODO Auto-generated catch block
			z.printStackTrace();
		}
		
	}


	private void connect(ClientProfile profile) {
		if(C.connect(profile))
			window.closeWindow();
	}
	

	public void itemStateChanged(ItemEvent e) {
		System.out.println(e);
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



	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		mousePressed(e);
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
