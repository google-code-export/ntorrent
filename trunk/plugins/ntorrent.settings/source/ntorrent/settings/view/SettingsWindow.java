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
package ntorrent.settings.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import tests.TestModel;

import ntorrent.locale.ResourcePool;
import ntorrent.settings.model.SettingsExtension;

/**
 * @author Kim Eik
 *
 */
public class SettingsWindow extends JFrame implements ListSelectionListener, ActionListener {
	private static final long serialVersionUID = 1L;
	private final SettingsExtension[] elements;
	private final JPanel content = new JPanel(new BorderLayout());
	private final JPanel buttons = new JPanel();
	private final JButton save,close;
	private final JSplitPane split;
	private final JScrollPane rightComponent = new JScrollPane();
	private final JList pluginList;
	
	public SettingsWindow(final SettingsExtension[] plugins) {
		setTitle(ResourcePool.getString("title","locale",this));
		
		//set variables
		this.elements = plugins;
		
		//init jlist
		pluginList = new JList(plugins);
		pluginList.addListSelectionListener(this);
		
		//init splitpane
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,new JScrollPane(pluginList),rightComponent);
		
		//init buttons
		save = new JButton(ResourcePool.getString("save", "locale", this));
		close = new JButton(ResourcePool.getString("close", "locale", this));
		save.addActionListener(this);
		close.addActionListener(this);
		buttons.add(save);
		buttons.add(close);
		
		//add content
		content.add(split);
		content.add(buttons,BorderLayout.SOUTH);
		
		//set first list item as selected.
		if(plugins.length > 0)
			pluginList.setSelectedIndex(0);
		
		//set content
		setContentPane(content);
	}
	
	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting()){
			SettingsExtension element = (SettingsExtension)pluginList.getSelectedValue();
			rightComponent.setViewportView(element.getDisplay());
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(save)){
			for(SettingsExtension element: elements)
				try {
					element.saveActionPerformed();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(
							this, 
							ResourcePool.getString("error-on-save", "locale", this)+
							" ("+element+")"+
							" : "+ex.toString());
					ex.printStackTrace();
				}
		}
		setVisible(false);
		dispose();
	}
	
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException{
		JFrame window = new SettingsWindow(new SettingsExtension[]{new SettingsExtension(){

			public Component getDisplay() {
				return new JLabel("halloen");
			}

			public void saveActionPerformed() {
				System.out.println("save this state");
			}
			
			@Override
			public String toString() {
				return "plugin1";
			}
			
		},
		new SettingsExtension(){
			final TestModel t = new TestModel();
			final SettingsComponentFactory s = new SettingsComponentFactory(t);
			public Component getDisplay() {
				try {
					
					return s.getDisplay();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			public void saveActionPerformed() throws Exception {
				s.restoreToModel();
				System.out.println(t.getCharacter()+"\n" +
						t.getDuble()+"\n" +
						t.getFl0at()+"\n" +
						t.getIint()+"\n" +
						t.getL0ng()+"\n" +
						t.getSh0rt()+"\n" +
						t.getString()+"\n" +
						t.getByt3()+"\n" +
						t.getE()+"\n");
			}
			
			@Override
			public String toString() {
				return "plugin2";
			}
			
		}
			
		});
		window.pack();
		window.validate();
		window.setVisible(true);
	}
}
