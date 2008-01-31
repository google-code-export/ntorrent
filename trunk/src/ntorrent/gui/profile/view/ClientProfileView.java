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
package ntorrent.gui.profile.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ntorrent.gui.profile.ClientProfileController;
import ntorrent.gui.profile.model.ClientProfileInterface;
import ntorrent.gui.profile.model.ClientProfileListModel;
import ntorrent.gui.profile.model.HttpProfileModel;
import ntorrent.gui.profile.model.LocalProfileModel;
import ntorrent.gui.profile.model.SshProfileModel;
import ntorrent.io.settings.Constants;
import ntorrent.mvc.AbstractView;

public class ClientProfileView extends AbstractView implements ItemListener, ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;
	
	CardLayout layout = new CardLayout();
	JComboBox box;
	JPanel cards = new JPanel(layout);
	JPanel pane = new JPanel(new BorderLayout());
	JButton connect,save,delete;
	JList profilesList = new JList();
	ClientProfileListModel listModel;
	ClientProfileController controller;
	AbstractClientProfileView[] views;
	
	public ClientProfileView(AbstractClientProfileView[] views, ClientProfileController c) {
		
		controller = c;
		this.views = views;
		
		JPanel boxPane = new JPanel();
	
		listModel = new ClientProfileListModel();
		profilesList.setModel(listModel);
		profilesList.addListSelectionListener(this);
		profilesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		profilesList.setPreferredSize(new Dimension(200,0));
		box = new JComboBox(views);
		box.addItemListener(this);
		boxPane.add(box);
		pane.add(boxPane,BorderLayout.NORTH);
		pane.add(new JScrollPane(profilesList),BorderLayout.EAST);
		pane.add(cards);
		
		for(AbstractClientProfileView v : views){
			JPanel panel = new JPanel();
			panel.add(v.getDisplay());
			cards.add(panel,v.toString());
		}
		
		connect = new JButton(Constants.messages.getString("connect"));
		save = new JButton(Constants.messages.getString("profile.save"));
		delete = new JButton(Constants.messages.getString("profile.delete"));
		
		connect.addActionListener(this);
		save.addActionListener(this);
		delete.addActionListener(this);
		
		JPanel buttonPane = new JPanel();

		buttonPane.add(connect);
		buttonPane.add(save);
		buttonPane.add(delete);
		
		pane.add(buttonPane,BorderLayout.SOUTH);
	}
	
	public JComponent getDisplay() {
		return pane;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
	}

	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == ItemEvent.SELECTED){
			layout.show(cards, e.getItem().toString());
		}
	}

	public void actionPerformed(ActionEvent e) {
		AbstractClientProfileView view = (AbstractClientProfileView) box.getSelectedItem();
		if(e.getSource().equals(connect)){
			
			/** Connect with current model **/
			controller.connectEvent(view.getModel());
			
		}else if(e.getSource().equals(save)){
			
			/** get the model **/
			ClientProfileInterface model = (ClientProfileInterface) view.getModel().clone();
			
			/**prompt for model name**/
			String name = JOptionPane.showInputDialog("give it a name");
			model.setName(name);
			
			/**add model to list**/
			listModel.add(model);
			
			/**connect a new model to the gui**/
			ClientProfileInterface newModel = null;
			switch(model.getProtocol()){
				case HTTP:
					newModel = new HttpProfileModel();
					break;
				case LOCAL:
					newModel = new LocalProfileModel();
					break;
				case SSH:
					newModel = new SshProfileModel();
					break;
				}
			view.setModel(newModel);
			
		}else if(e.getSource().equals(delete)){
			
			/** remove a model from the list **/
			listModel.remove((ClientProfileInterface)profilesList.getSelectedValue());
			
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		try{
			if(!e.getValueIsAdjusting()){
				ClientProfileInterface model = listModel.get(profilesList.getSelectedIndex());
				for(AbstractClientProfileView v : views){
					if(v.getModel().getClass().equals(model.getClass())){
						box.setSelectedItem(v);
						v.setModel((ClientProfileInterface) model.clone());
					}
				}
			}
		}catch(ArrayIndexOutOfBoundsException x){
			Logger.global.log(Level.WARNING, x.getMessage(),x);
		}
	}

	public void modelChange() {
		// TODO Auto-generated method stub
		
	}
}
