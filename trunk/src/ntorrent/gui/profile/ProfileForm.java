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
package ntorrent.gui.profile;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import ntorrent.io.settings.Constants;

public class ProfileForm extends JPanel {
	
	
	/** set up labels **/
    String[] labels = {
    		Constants.messages.getString("profile.protocol"),
    		Constants.messages.getString("profile.host"),
    		Constants.messages.getString("profile.connectionport"), 
    		Constants.messages.getString("profile.socketport"),
    		Constants.messages.getString("profile.mountpoint"),
    		Constants.messages.getString("profile.username"),
    		Constants.messages.getString("profile.password"),
    		Constants.messages.getString("profile.rememberpwd"),
    		Constants.messages.getString("profile.autoconnect")
    };
    
    public ProfileForm() {
    	setLayout(new GridLayout(labels.length,2));
    	for(int x = 0; x < labels.length; x++){
    		JLabel label = new JLabel(labels[x]+":");
    		JTextField txt = new JTextField(15);
    		JPanel panelabel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    		JPanel panetxt = new JPanel(new FlowLayout(FlowLayout.LEFT));
    		label.setHorizontalAlignment(SwingConstants.RIGHT);
    		label.setVerticalAlignment(SwingConstants.TOP);
    		label.setLabelFor(txt);
    		add(panelabel);
    		add(panetxt);
    		panetxt.add(txt);
    		panelabel.add(label);
    	}
	}
}
