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

import org.apache.log4j.Logger;

import tests.ntorrent.settings.TestModel;

import ntorrent.NtorrentApplication;
import ntorrent.core.view.component.util.Window;
import ntorrent.locale.ResourcePool;
import ntorrent.settings.model.SettingsExtension;
import ntorrent.settings.model.SettingsPluginListItem;
import ntorrent.settings.model.SettingsPluginListModel;

/**
 * @author Kim Eik
 *
 */
public class SettingsWindow extends Window implements ListSelectionListener, ActionListener {
	private static final long serialVersionUID = 1L;
	private final SettingsExtension[] elements;
	private final JPanel content = new JPanel(new BorderLayout());
	private final JPanel buttons = new JPanel();
	private final JButton save,close;
	private final JSplitPane split;
	private final JScrollPane rightComponent = new JScrollPane();
	private final JList pluginList;
	
	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(NtorrentApplication.class);
	
	public SettingsWindow(final SettingsExtension[] plugins) {
		setTitle(ResourcePool.getString("title",this));
		
		//set variables
		this.elements = plugins;
		
		//init jlist
		pluginList = new JList(new SettingsPluginListModel(plugins));
		pluginList.addListSelectionListener(this);
		
		//init splitpane
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,new JScrollPane(pluginList),rightComponent);
		
		//init buttons
		save = new JButton(ResourcePool.getString("save", this));
		close = new JButton(ResourcePool.getString("close", this));
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
			SettingsExtension element = ((SettingsPluginListItem)pluginList.getSelectedValue()).getPlugin();
			rightComponent.setViewportView(element.getSettingsDisplay());
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(save)){
			log.info("Save event caught");
			for(SettingsExtension element: elements)
				try {
					element.saveActionPerformedOnSettings();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(
							this, 
							ResourcePool.getString("error-on-save", this)+
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

			public Component getSettingsDisplay() {
				return new JLabel("halloen");
			}

			public void saveActionPerformedOnSettings() {
				System.out.println("save this state");
			}

			@Override
			public String getSettingsDisplayName() {
				return "plugin1";
			}
			
		},
		new SettingsExtension(){
			final TestModel t = new TestModel();
			final SettingsComponentFactory s = new SettingsComponentFactory(t);
			public Component getSettingsDisplay() {
				try {
					
					return s.getDisplay();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			public void saveActionPerformedOnSettings() throws Exception {
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
			public String getSettingsDisplayName() {
				return "plugin2";
			}
			
		}
			
		});
		window.pack();
		window.validate();
		window.setVisible(true);
	}
}
