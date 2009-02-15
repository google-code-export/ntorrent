package ntorrent.connection.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.net.Proxy;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ntorrent.locale.ResourcePool;

public class ProxyView extends JPanel {
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
	private final JButton proxyCancel = new JButton(ResourcePool.getString("profile.proxy.cancel", this));
	
	public ProxyView() {
		super(new BorderLayout());
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
		
		//buttons
		buttonPanel.add(proxyOK);
		buttonPanel.add(proxyCancel);
		
		//add to containers
		add(comboContainer,BorderLayout.NORTH);
		add(rootContainer,BorderLayout.CENTER);
		add(buttonPanel,BorderLayout.SOUTH);
	}
}
