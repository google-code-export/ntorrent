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
package ntorrent.jpf;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import ntorrent.env.Environment;

import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.PluginRegistry;

public class PluginHandlerMenuBar implements ItemListener {
	
	PluginManager manager = Environment.getPluginManager();
	PluginRegistry reg = manager.getRegistry();
	
	public PluginHandlerMenuBar(JMenuBar menuBar){
		JMenu plugin = new JMenu(Environment.getString("plugin"));
		menuBar.add(plugin);			
			for(Extension e : reg.getExtensionPoint("ntorrent.jpf","HandledPlugin").getConnectedExtensions()){
				JCheckBox c = new JCheckBox(e.getDeclaringPluginDescriptor().getId());
				double jversion = Double.parseDouble(
						System.getProperty("java.specification.version"));
				double pversion = e.getParameter("java-spec").valueAsNumber().doubleValue();
				
				if(!(jversion >= pversion && manager.isPluginEnabled(e.getDeclaringPluginDescriptor()))){
					c.setEnabled(false);
				}
				c.setSelected(manager.isPluginActivated(e.getDeclaringPluginDescriptor()));
				c.setName(e.getDeclaringPluginDescriptor().getId());
				c.addItemListener(this);
				plugin.add(c);
			}

		
	}

	public void itemStateChanged(ItemEvent e) {
		JCheckBox c = ((JCheckBox)e.getItem());
		String id  = c.getName();
		boolean selected = e.getStateChange() == ItemEvent.SELECTED;
		if(selected)
			try {
				manager.activatePlugin(id);
			} catch (PluginLifecycleException e1) {
				Logger.global.log(Level.WARNING,e1.getMessage(),e1);
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Plugin error", JOptionPane.ERROR_MESSAGE);
			}
		else
			manager.deactivatePlugin(id);
		c.setSelected(manager.isPluginActivated(reg.getPluginDescriptor(id)));
	}

}
