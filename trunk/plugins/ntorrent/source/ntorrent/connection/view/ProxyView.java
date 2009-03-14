package ntorrent.connection.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.Proxy;
import java.net.Proxy.Type;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import ntorrent.connection.model.ProxyProfile;
import ntorrent.connection.model.ProxyProfileImpl;
import ntorrent.locale.ResourcePool;

public class ProxyView extends JPanel implements ItemListener, ActionListener {
	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(ProxyView.class);
	
	private final JPanel comboContainer = new JPanel();
	private final JPanel rootContainer = new JPanel();
	private final JPanel proxyContainer = new JPanel(new GridLayout(0,2));
	private final JPanel buttonPanel = new JPanel();
	private final JComboBox proxyType = new JComboBox(Proxy.Type.values());
	private final JTextField proxyHost = new JTextField();
	private final JTextField proxyPort = new JTextField();
	private final JCheckBox proxyUseAuth = new JCheckBox();
	private final JTextField proxyUsername = new JTextField();
	private final JPasswordField proxyPassword = new JPasswordField();
	private final JButton proxyOK = new JButton(ResourcePool.getString("profile.proxy.ok", this));
	private ProxyProfileImpl proxyProfile;
	private final ActionListener parentListener;
	
	public ProxyView(ActionListener parentListener) {
		super(new BorderLayout());
		this.proxyType.setSelectedIndex(-1);
		this.parentListener = parentListener;
		
		//Combobox
		comboContainer.add(proxyType);
		
		//Proxy details
		proxyContainer.add(new JLabel(ResourcePool.getString("profile.proxy.host", this)));
		proxyContainer.add(proxyHost);
		proxyContainer.add(new JLabel(ResourcePool.getString("profile.proxy.port", this)));
		proxyContainer.add(proxyPort);
		proxyContainer.add(new JLabel(ResourcePool.getString("profile.proxy.useAuth", this)));
		proxyContainer.add(proxyUseAuth);
		proxyContainer.add(new JLabel(ResourcePool.getString("profile.proxy.username", this)));
		proxyContainer.add(proxyUsername);
		proxyContainer.add(new JLabel(ResourcePool.getString("profile.proxy.password", this)));
		proxyContainer.add(proxyPassword);
		rootContainer.add(proxyContainer);
		
		//listeners
		proxyType.addItemListener(this);
		proxyOK.addActionListener(this);
		proxyUseAuth.addItemListener(this);
		
		//set initial state
		this.proxyType.setSelectedIndex(0);
		
		//buttons
		buttonPanel.add(proxyOK);
		
		//add to containers
		add(comboContainer,BorderLayout.NORTH);
		add(rootContainer,BorderLayout.CENTER);
		add(buttonPanel,BorderLayout.SOUTH);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == this.proxyType){
			clearForm();
			switch((Type)e.getItem()){
				case DIRECT:
					this.proxyHost.setEnabled(false);
					this.proxyPort.setEnabled(false);
					this.proxyUseAuth.setEnabled(false);
					this.proxyUseAuth.setSelected(false);
					this.proxyUsername.setEnabled(false);
					this.proxyPassword.setEnabled(false);
					break;
				case HTTP:
				case SOCKS:
					this.proxyHost.setEnabled(true);
					this.proxyPort.setEnabled(true);
					this.proxyUseAuth.setEnabled(true);
					break;
			}
		}else if(e.getSource() == proxyUseAuth){
			this.proxyUsername.setEnabled(!this.proxyUsername.isEnabled());
			this.proxyPassword.setEnabled(!this.proxyPassword.isEnabled());
		}
	}

	private void clearForm() {
		this.proxyHost.setText("");
		this.proxyPort.setText("");
		this.proxyUsername.setText("");
		this.proxyPassword.setText("");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try{
			updateModel();
			e.setSource(this);
			this.parentListener.actionPerformed(e);
		}catch(Exception x){
			log.info(x.getMessage(),x);
			JOptionPane.showMessageDialog(this, ResourcePool.getString("profile.proxy.error", this),null,JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void updateView(){
		Type proxyType = this.proxyProfile.getProxyType();
		String host = this.proxyProfile.getHost();
		Integer port = this.proxyProfile.getPort();
		Boolean usingAuth = this.proxyProfile.usingAuthentication();
		String username = this.proxyProfile.getUsername();
		String password = this.proxyProfile.getPassword();
		
		this.proxyType.setSelectedItem(proxyType);
		this.proxyHost.setText(host);
		this.proxyPort.setText(port != null ? port.toString() : "");
		this.proxyUseAuth.setSelected(usingAuth);
		this.proxyUsername.setText(username);
		this.proxyPassword.setText(password);

	}
	
	private void updateModel() throws Exception {
		Type proxyType = (Type) this.proxyType.getSelectedItem();
		String host = null;
		Integer port = null;
		boolean usingAuth = false;
		String username = null;
		String password = null;
		
		if(this.proxyHost.isEnabled())
			host = proxyHost.getText();
		if(this.proxyPort.isEnabled())
			port = Integer.parseInt(proxyPort.getText());
		if(this.proxyUseAuth.isEnabled())
			usingAuth = proxyUseAuth.isSelected();
		if(this.proxyUsername.isEnabled())
			username = proxyUsername.getText();
		if(this.proxyPassword.isEnabled())
			password = String.valueOf(proxyPassword.getPassword());
		
		this.proxyProfile.setHost(host);
		this.proxyProfile.setPort(port);
		this.proxyProfile.setProxyType(proxyType);
		this.proxyProfile.setUsingAuthentication(usingAuth);
		this.proxyProfile.setUsername(username);
		this.proxyProfile.setPassword(password);
	}

	public ProxyProfile getProxyProfile() {
		return proxyProfile;
	}

	public void setProxyProfile(ProxyProfile proxyProfile) {
		if(proxyProfile instanceof ProxyProfileImpl)
			this.proxyProfile = (ProxyProfileImpl)proxyProfile;
		else{
			this.proxyProfile.setHost(proxyProfile.getHost());
			this.proxyProfile.setPassword(proxyProfile.getPassword());
			this.proxyProfile.setPort(proxyProfile.getPort());
			this.proxyProfile.setProxyType(proxyProfile.getProxyType());
			this.proxyProfile.setUsername(proxyProfile.getUsername());
			this.proxyProfile.setUsingAuthentication(proxyProfile.usingAuthentication());
		}
		updateView();
	}
}
