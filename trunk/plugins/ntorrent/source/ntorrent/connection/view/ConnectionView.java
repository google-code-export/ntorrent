package ntorrent.connection.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.Proxy;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ntorrent.NtorrentApplication;
import ntorrent.connection.ConnectionController;
import ntorrent.connection.model.ConnectListener;
import ntorrent.connection.model.ConnectionProfileExtension;
import ntorrent.connection.model.ProxyProfile;
import ntorrent.connection.model.ProxyProfileImpl;
import ntorrent.locale.ResourcePool;
import ntorrent.tools.Serializer;

import org.apache.log4j.Logger;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;

public class ConnectionView extends JPanel implements ItemListener, ListSelectionListener, ActionListener {
	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(ConnectionView.class);
	
	public final static String PROFILE_SERIALIZE_NAME = "ntorrent.profiles";
	
	private static final long serialVersionUID = 1L;
	private final ConnectListener connectionListener;
	private final static DefaultComboBoxModel boxModel = new DefaultComboBoxModel();
	private final static DefaultListModel listModel = ConnectionView.restoreProfileModel();
	private final static ConnectionProfileRenderer renderer = new ConnectionProfileRenderer();
	private final JPanel mainContainer = new JPanel(new BorderLayout(10,10));
	private final JPanel profileContainer = new JPanel(new BorderLayout(0,10));
	private final JPanel connectContainer = new JPanel(new BorderLayout());
	private final JComboBox box = new JComboBox(boxModel);
	private final JList profiles = new JList(listModel);
	private final JButton connect = new JButton();
	private final JButton save = new JButton();
	private final JButton delete = new JButton();
	private final JButton proxySettings = new JButton();
	private final JProgressBar connectBar = new JProgressBar();
	private ConnectionProfileExtension focusedComponent = null;

	private ProxyProfile proxyProfile = new ProxyProfileImpl();
	
	public ConnectionView(ConnectListener connectionListener,ActionListener actionListener) {
		this.connectionListener = connectionListener;
		
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
		
		mainContainer.add(boxPanel,BorderLayout.NORTH);
		JPanel profileFlowPanel = new JPanel();
		profileFlowPanel.add(profileContainer);
		mainContainer.add(profileFlowPanel);
		profileContainer.add(new JScrollPane(profiles),BorderLayout.EAST);
		
		//buttons
		JPanel buttonPanel = new JPanel(new GridLayout(2,2,1,1));
		
		save.setText(ResourcePool.getString("profile.connection.save", this));
		delete.setText(ResourcePool.getString("profile.connection.delete", this));
		connect.setText(ResourcePool.getString("profile.connection.connect", this));
		proxySettings.setText(ResourcePool.getString("profile.proxy.settings", this));
		
		buttonPanel.add(connect);
		buttonPanel.add(save);
		buttonPanel.add(proxySettings);
		buttonPanel.add(delete);
		
		connect.addActionListener(this);
		save.addActionListener(this);
		delete.addActionListener(this);
		proxySettings.addActionListener(actionListener);
		
		mainContainer.add(buttonPanel,BorderLayout.SOUTH);
		
		//connecting progress bar
		connectBar.setStringPainted(true);
		connectBar.setString(ResourcePool.getString("connecting", this));
		connectBar.setIndeterminate(true);
		connectContainer.add(connectBar);
	
		
		//adding to container
		add(mainContainer);
		//connectContainer.setVisible(false);
		//add(connectContainer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void itemStateChanged(ItemEvent e) {
		log.trace(e);
		revalidate();
		ConnectionProfileExtension connectionProfileExtension = (ConnectionProfileExtension)e.getItem();
		if(e.getStateChange() == ItemEvent.SELECTED){
			setFocusedComponent(getClonedInstance(connectionProfileExtension));
			//clear profile list selection
			if(!profiles.isSelectionEmpty())
				profiles.clearSelection();
		}
	}
	
	private void setFocusedComponent(ConnectionProfileExtension profile){
		if(focusedComponent != null)
			profileContainer.remove(focusedComponent.getDisplay());
		
		profileContainer.add(profile.getDisplay());
		focusedComponent = profile;
		profileContainer.validate();
		profileContainer.repaint();
	}
	
	private ConnectionProfileExtension getClonedInstance(ConnectionProfileExtension obj){
		try {
			return obj.getClonedInstance();
		} catch (CloneNotSupportedException e) {
			log.fatal(e);
			JOptionPane.showMessageDialog(this, e.getMessage(),null,JOptionPane.ERROR_MESSAGE);
		}
		return obj;
	}

	public void valueChanged(ListSelectionEvent e) {
		log.trace(e);
		if(!e.getValueIsAdjusting()){
			if(e.getSource() == profiles){
				Object selectedObj = profiles.getSelectedValue();
				if(profiles.getSelectedIndices().length == 1)
					if(profiles.getSelectedValue() instanceof ConnectionProfileExtension){
						ConnectionProfileExtension selectedVal = (ConnectionProfileExtension)selectedObj;
						//set connection type selection to current selected profile
						for(int x = (boxModel.getSize()-1); x >= 0; x--){
							Object value = boxModel.getElementAt(x);
							if(value.getClass().equals(selectedVal.getClass())){
								boxModel.setSelectedItem(value);
								break;
							}
						}
						setFocusedComponent(getClonedInstance(selectedVal));
					}
			}
		}
		
	}
	
	private boolean isConnectionExtensionEqual(ConnectionProfileExtension obj1, ConnectionProfileExtension obj2){
		return obj1.getName().equals(obj2.getName());
	}
	
	private void saveProfile(){
		String name = JOptionPane.showInputDialog(this,ResourcePool.getString("profile.name", this),focusedComponent.getName());
		if(name != null)
			if(name.length() > 0){
				focusedComponent.saveEvent();
				profiles.clearSelection();
				ConnectionProfileExtension profileToSave = getClonedInstance(focusedComponent);
				profileToSave.setName(name);
				boolean saveProfile = true;
				for(int x = (listModel.getSize()-1); x >= 0 ; x--){
					ConnectionProfileExtension listedProfile = (ConnectionProfileExtension) listModel.get(x);
					if(isConnectionExtensionEqual(profileToSave, listedProfile)){
						int result = JOptionPane.showConfirmDialog(
								this, 
								ResourcePool.getString("profile.confirm.overwrite.message", this),
								ResourcePool.getString("profile.confirm.overwrite.title", this),
								JOptionPane.YES_NO_OPTION
								);
						if(result == JOptionPane.YES_OPTION){
							listModel.removeElement(listedProfile);
						}else{
							saveProfile = false;
						}
						break;
					}
				}
				if(saveProfile){
					listModel.addElement(profileToSave);
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
				}
			}else{
				//Save error.
				JOptionPane.showMessageDialog(
						this, 
						ResourcePool.getString("profile.error.name.message",this),
						ResourcePool.getString("profile.error.name.title",this),
						JOptionPane.ERROR_MESSAGE);
			}		
	}
	
	private void deleteProfile(){
		if(profiles.isSelectionEmpty()){
			JOptionPane.showMessageDialog(
					this,
					ResourcePool.getString("profile.error.delete.message", this),
					ResourcePool.getString("profile.error.delete.title", this),
					JOptionPane.WARNING_MESSAGE
					);
		}else{
			int result = JOptionPane.showConfirmDialog(
					this, 
					ResourcePool.getString("profile.confirm.delete.message", this),
					ResourcePool.getString("profile.confirm.delete.title", this),
					JOptionPane.YES_NO_OPTION
					);
			if(result == JOptionPane.YES_OPTION){
				for(Object selected : profiles.getSelectedValues()){
					listModel.removeElement(selected);
				}
				profiles.clearSelection();
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
			}
		}		
	}
	
	private void connect(){
		focusedComponent.setProxyConnectionInfo(proxyProfile);
		focusedComponent.connectEvent();
		try{
			connectionListener.connect(focusedComponent.getConnection());
			mainContainer.setVisible(false);
			connectContainer.setVisible(true);
		}catch(Exception x){
			log.fatal(x.getMessage(),x);
			JOptionPane.showMessageDialog(
					this, 
					x.getMessage(),
					x.getMessage(),
					JOptionPane.ERROR_MESSAGE
			);
			connectContainer.setVisible(false);
			mainContainer.setVisible(true);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		try{
			Object src = e.getSource();
			if(src == save){
				saveProfile();
			}else if(src == delete){
				deleteProfile();
			}else if(src == connect){
				connect();
			}
		}catch (Exception x) {
			log.fatal(x);
			JOptionPane.showMessageDialog(
					this, 
					x.getMessage(),
					x.getMessage(),
					JOptionPane.ERROR_MESSAGE
					);
		}
	}
	
	public void setProxyProfile(ProxyProfile profile){
		this.proxyProfile = profile;
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

	public ProxyProfile getProxyProfile() {
		return this.proxyProfile;
	}
}
