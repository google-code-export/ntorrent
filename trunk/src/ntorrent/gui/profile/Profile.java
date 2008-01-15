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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;

import ntorrent.gui.GuiAction;
import ntorrent.io.settings.Constants;

public class Profile extends JPanel implements ActionListener {

	public Profile() {
		JPanel buttonpanel = new JPanel();
		
		/** buttons **/
		Action[] buttons = {
			new GuiAction("profile.host",this),
			new GuiAction("profile.host",this),
			new GuiAction("profile.host",this)
		};
		
		/** setting the layout mgr**/
        setLayout(new BorderLayout());
        add(new ProfileForm(),BorderLayout.CENTER);
        add(new JScrollPane(new ProfileList()),BorderLayout.EAST);
        add(buttonpanel,BorderLayout.SOUTH);
        
        for(int x = 0; x < buttons.length; x++)
        	buttonpanel.add(new JButton(buttons[x]));
	}

    public static void main(String[] args) {
    	JFrame f = new JFrame();
    	f.setContentPane(new JPanel());
    	f.getContentPane().add(new Profile());
    	f.pack();
    	f.setVisible(true);
    }

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
