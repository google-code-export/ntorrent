package ntorrent.connection.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.Proxy;

import javax.print.attribute.standard.JobHoldUntil;
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
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ntorrent.NtorrentApplication;
import ntorrent.connection.ConnectionController;
import ntorrent.connection.model.ConnectListener;
import ntorrent.connection.model.ConnectionProfileExtension;
import ntorrent.locale.ResourcePool;
import ntorrent.tools.Serializer;

import org.apache.log4j.Logger;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;

public class ConnectionProfileView extends JPanel implements ActionListener {
	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(ConnectionProfileView.class);
	
	private final ConnectionView connectionView;
	private final ProxyView proxyView;
	
	public ConnectionProfileView(ConnectListener listener) {
		connectionView  = new ConnectionView(listener,this);
		proxyView = new ProxyView(this);
		add(connectionView);
		add(proxyView);
		reset();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			connectionView.setVisible(!connectionView.isVisible());
			proxyView.setVisible(!proxyView.isVisible());
			if(e.getSource() == proxyView){
				connectionView.setProxyProfile(proxyView.getProxyProfile().getClonedInstance());
			}else{
				proxyView.setProxyProfile(connectionView.getProxyProfile().getClonedInstance());
			}
		} catch (CloneNotSupportedException x) {
			log.error(x.getMessage(),x);
		}
	}

	public void reset() {
		connectionView.setVisible(true);
		proxyView.setVisible(false);
	}
	
}
