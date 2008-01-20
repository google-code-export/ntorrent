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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import ntorrent.gui.GuiAction;
import ntorrent.io.settings.Constants;

public class Profile extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	ProfileForm form;
	ProfileList list;
	ProfileRequester req;
	
	public Profile(ProfileRequester requester) {
		JPanel content = new JPanel(new BorderLayout());
		JPanel buttonpanel = new JPanel();
		req = requester;
		
		/** buttons **/
		Action[] buttons = {
			new GuiAction("connect",this),
			new GuiAction("profile.save",this),
			new GuiAction("profile.delete",this)
		};
        
        form = new ProfileForm();
        list = new ProfileList(form);
        
        content.add(form,BorderLayout.CENTER);
        content.add(new JScrollPane(list),BorderLayout.EAST);
        content.add(buttonpanel,BorderLayout.SOUTH);
        
        for(int x = 0; x < buttons.length; x++)
        	buttonpanel.add(new JButton(buttons[x]));
        
        add(content);
	}

    public static void main(String[] args) {
    	JFrame f = new JFrame();
    	f.setContentPane(new JPanel());
    	f.getContentPane().add(new Profile(null));
    	f.pack();
    	f.setVisible(true);
    	f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

	public void actionPerformed(ActionEvent e) {
		try {
			if(e.getActionCommand().equals("connect")){
				String name = null;
				if(list.getSelectedValue() != null)
					name = ((ClientProfile) list.getSelectedValue()).toString();
				req.sendProfile(form.getProfile(name));
			}else if(e.getActionCommand().equals("profile.save")){
				String name = JOptionPane.showInputDialog(this, Constants.messages.getString("profile.name"));
				list.add(form.getProfile(name));
			}else if(e.getActionCommand().equals("profile.delete")){
				list.deleteSelected();
			}
		} catch (Exception e1) {
			Logger.global.log(Level.SEVERE, e1.getMessage(), e1);
			JOptionPane.showMessageDialog(this, e1.getMessage());
		}
	}
}
