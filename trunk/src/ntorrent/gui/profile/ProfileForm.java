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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Field;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ntorrent.gui.profile.ClientProfile.Protocol;
import ntorrent.io.settings.Constants;

public class ProfileForm extends JPanel implements ListSelectionListener, ItemListener  {
	private static final long serialVersionUID = 1L;
	private HashMap<String,JComponent> components = new HashMap<String,JComponent>();

	@SuppressWarnings("unchecked")
	public ProfileForm() {
    	setLayout(new GridLayout(0,1));
    	
    	for(Field f : ClientProfile.class.getDeclaredFields()){
    		ClientProfile.metadata labelObj = f.getAnnotation(ClientProfile.metadata.class);
    		if(labelObj != null){
        		Class type = f.getType();
        		String label = Constants.messages.getString(labelObj.label());
        		JComponent c = null;
        		
        		if(type.equals(String.class) || type.equals(int.class)){
            		add(new JLabel(label+":"));
        			c = new JTextField(10);
        		}else if(type.equals(Protocol.class)){
        			add(new JLabel(label+":"));
        			JComboBox t = new JComboBox(Protocol.values());
        			t.setSelectedIndex(-1);
        			t.addItemListener(this);
        			c = t;
        		}else if(type.equals(boolean.class)){
        			c = new JCheckBox(label);
        		}
        		add(c);
        		components.put(labelObj.label(), c);
    		}
    	}
   	}

	public void valueChanged(ListSelectionEvent e) {
		
	}

	public void getProfile(String name) {
		ClientProfile profile = new ClientProfile(name);
		
	}

	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == ItemEvent.SELECTED){
			for(Field f : ClientProfile.class.getDeclaredFields()){
				ClientProfile.metadata annotation = f.getAnnotation(ClientProfile.metadata.class);
				if(annotation != null){
					JComponent component = components.get(annotation.label());
					boolean exists = false;
					for(Protocol p : annotation.protocols()){
						if(exists = p.equals(e.getItem())){
							break;
						}
					}
				
					
					String value = annotation.value()[((Protocol)e.getItem()).ordinal()];	
					if(component instanceof JTextField){
						((JTextField)component).setText(value);
					}else if(component instanceof JCheckBox){
						boolean b = !value.equals("0");
						((JCheckBox)component).setSelected(b);
					}
					
					if(!exists){
						component.setEnabled(false);
					}else{
						component.setEnabled(true);
					}

				}
			}
		}
	}
}
