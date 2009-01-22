package ntorrent.connection.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ntorrent.NtorrentApplication;
import ntorrent.connection.model.ConnectionProfile;
import ntorrent.connection.model.ConnectionProfileExtension;
import ntorrent.connection.socket.SocketConnectionController;
import ntorrent.connection.socket.model.SocketConnectionProfile;
import ntorrent.locale.ResourcePool;
import ntorrent.tools.Serializer;

import org.apache.log4j.Logger;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;

public class ConnectionProfileView extends JPanel implements ItemListener, ListSelectionListener, ActionListener{
	
	public final static String PROFILE_SERIALIZE_NAME = "ntorrent.profiles";
	
	private static final long serialVersionUID = 1L;
	private final static DefaultComboBoxModel boxModel = new DefaultComboBoxModel();
	private final static DefaultListModel listModel = ConnectionProfileView.restoreProfileModel();
	private final static ConnectionProfileRenderer renderer = new ConnectionProfileRenderer();
	private final JPanel container = new JPanel(new BorderLayout());
	private final JComboBox box = new JComboBox(boxModel);
	private final JList profiles = new JList(listModel);
	private final JButton connect = new JButton();
	private final JButton save = new JButton();
	private final JButton delete = new JButton();
	private ConnectionProfileExtension focusedComponent = null;
	
	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(ConnectionProfileView.class);
	
	public ConnectionProfileView() {
		box.addItemListener(this);
		box.setRenderer(renderer);
		
		profiles.addListSelectionListener(this);
		profiles.setCellRenderer(renderer);
		
		if(boxModel.getSize() == 0){
			/** Get extensions but only if boxModel is empty**/
			PluginManager manager = NtorrentApplication.MANAGER;
			PluginRegistry registry = manager.getRegistry();
			ExtensionPoint extension = registry.getExtensionPoint("ntorrent@ConnectionProfileExtension");
			for(Extension ext : extension.getAvailableExtensions()){
				PluginDescriptor descr = ext.getDeclaringPluginDescriptor();
				log.info("Adding: "+descr.getId());
				try {
					boxModel.addElement(manager.getPlugin(descr.getId()));
				} catch (PluginLifecycleException e) {
					log.warn("could not add "+descr.getId()+" to connection types", e);
				}
			}
		}
		
		JPanel boxPanel = new JPanel();
		boxPanel.add(box);
		
		container.add(boxPanel,BorderLayout.NORTH);
		container.add(profiles,BorderLayout.EAST);
		
		//buttons
		JPanel buttonPanel = new JPanel();
		
		save.setText(ResourcePool.getString("save", this));
		delete.setText(ResourcePool.getString("delete", this));
		connect.setText(ResourcePool.getString("connect", this));
		
		buttonPanel.add(connect);
		buttonPanel.add(save);
		buttonPanel.add(delete);
		
		connect.addActionListener(this);
		save.addActionListener(this);
		delete.addActionListener(this);
		
		container.add(buttonPanel,BorderLayout.SOUTH);
		
		add(container);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void itemStateChanged(ItemEvent e) {
		log.trace(e);
		ConnectionProfileExtension<ConnectionProfile> connectionProfileExtension = (ConnectionProfileExtension<ConnectionProfile>)e.getItem();
		if(e.getStateChange() == ItemEvent.SELECTED){
			setFocusedComponent(connectionProfileExtension);
			//clear profile list selection
			if(!profiles.isSelectionEmpty())
				profiles.clearSelection();
		}
	}
	
	private void setFocusedComponent(ConnectionProfileExtension profile){
		if(focusedComponent != null)
			container.remove(focusedComponent.getDisplay());
		
		container.add(profile.getDisplay());
		focusedComponent = profile;
		container.validate();
		container.repaint();
	}

	public void valueChanged(ListSelectionEvent e) {
		log.trace(e);
		if(!e.getValueIsAdjusting()){
			if(e.getSource() == profiles){
				Object selectedObj = profiles.getSelectedValue();
				if(profiles.getSelectedIndices().length == 1)
					if(profiles.getSelectedValue() instanceof ConnectionProfileExtension<?>){
						ConnectionProfileExtension<ConnectionProfile> selectedVal = (ConnectionProfileExtension<ConnectionProfile>)selectedObj;
						setFocusedComponent(selectedVal);

						//set connection type selection to current selected profile
						for(int x = (boxModel.getSize()-1); x >= 0; x--){
							Object value = boxModel.getElementAt(x);
							if(value.getClass().equals(selectedVal.getClass())){
								box.setSelectedIndex(x);
								break;
							}
						}
					}
			}
		}
		
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == save){
			String name = JOptionPane.showInputDialog(this,ResourcePool.getString("profile.name", this),focusedComponent.getName());
			if(name.length() > 0){
				focusedComponent.setName(name);
				if(!listModel.contains(focusedComponent))
					listModel.addElement(focusedComponent);
				profiles.updateUI();
				try {
					Serializer.serialize(listModel,PROFILE_SERIALIZE_NAME);
				} catch (IOException x) {
					//Save error.
					JOptionPane.showMessageDialog(
							this, 
							ResourcePool.getString("profile.error.ioexception.message",this)+"\n"+x.getMessage(),
							ResourcePool.getString("profile.error.ioexception.title",this),
							JOptionPane.ERROR_MESSAGE);
				}
			}else{
				//Save error.
				JOptionPane.showMessageDialog(
						this, 
						ResourcePool.getString("profile.error.name.message",this),
						ResourcePool.getString("profile.error.name.title",this),
						JOptionPane.ERROR_MESSAGE);
			}
		}else if(e.getSource() == delete){
			
		}else if(e.getSource() == connect){
			
		}
	}
	
	private final static DefaultListModel restoreProfileModel(){
		DefaultListModel model = null;
		try{
			model = Serializer.deserialize(DefaultListModel.class,PROFILE_SERIALIZE_NAME);
		}catch (Exception e) {
			log.warn(e);
		}
		
		if(model == null)
			model = new DefaultListModel();
		return model;
	}
}
