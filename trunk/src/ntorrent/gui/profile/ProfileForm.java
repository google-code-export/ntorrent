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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Field;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ntorrent.gui.profile.ClientProfile.Protocol;
import ntorrent.io.settings.Constants;

public class ProfileForm extends JPanel implements ListSelectionListener, ItemListener  {
	private static final long serialVersionUID = 1L;
	private HashMap<Field,JComponent> components = new HashMap<Field,JComponent>();

	@SuppressWarnings("unchecked")
	public ProfileForm() {
    	setLayout(new GridLayout(0,1));
    	
    	for(Field f : ClientProfile.class.getDeclaredFields()){
    		ClientProfile.metadata meta = f.getAnnotation(ClientProfile.metadata.class);
    		if(meta != null){
        		Class type = f.getType();
        		String label = Constants.messages.getString(meta.label());
        		JComponent c = null;
        		if(meta.jclass() != Object.class){
        			if(meta.jclass().equals(JPasswordField.class)){
	            		add(new JLabel(label+":"));
	        			c = new JPasswordField(10);
        			}
        		}else{
        			
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
	    		}
        		add(c);
        		c.setName(meta.label());
        		components.put(f, c);
    		}
    	}
   	}

	@SuppressWarnings("unchecked")
	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting()){
			ClientProfile profile = (ClientProfile)((JList)e.getSource()).getSelectedValue();
			for(Field f : components.keySet()){
				Class type = f.getType();
    			try {
		    		if(type.equals(String.class) || type.equals(int.class)){
						((JTextField)components.get(f)).setText(f.get(profile).toString());
		    		}else if(type.equals(Protocol.class)){
		    			((JComboBox)components.get(f)).setSelectedItem(f.get(profile));
		    		}else if(type.equals(boolean.class)){
		    			((JCheckBox)components.get(f)).setSelected(f.getBoolean(profile));
		    		}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	public ClientProfile getProfile() throws IllegalArgumentException, IllegalAccessException{
		return getProfile(null);
	}
	
	@SuppressWarnings("unchecked")
	public ClientProfile getProfile(String name) throws IllegalArgumentException, IllegalAccessException {
		ClientProfile profile = new ClientProfile(name);
		for(Field f : components.keySet()){
			JComponent component = components.get(f);
			Class type = f.getType();
    		if(type.equals(String.class)){
    			f.set(profile, ((JTextField)component).getText());
    		}else if(type.equals(int.class)){
    			String value = ((JTextField)component).getText();
    			f.set(profile, Integer.parseInt(value.equals("") ? "0" : value));
    		}else if(type.equals(Protocol.class)){
    			f.set(profile, ((JComboBox)component).getSelectedItem());
    		}else if(type.equals(boolean.class)){
    			f.set(profile, ((JCheckBox)component).isSelected());
    		}
		}
		return profile;
	}

	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == ItemEvent.SELECTED){
			for(Field f : ClientProfile.class.getDeclaredFields()){
				ClientProfile.metadata annotation = f.getAnnotation(ClientProfile.metadata.class);
				if(annotation != null){
					JComponent component = components.get(f);
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
