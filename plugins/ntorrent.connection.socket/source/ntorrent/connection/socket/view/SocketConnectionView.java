package ntorrent.connection.socket.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;

import ntorrent.connection.socket.model.SocketConnectionProfile;
import ntorrent.locale.ResourcePool;

public class SocketConnectionView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel container = new JPanel(new GridLayout(0,2));
	private final JTextField hostField = new JTextField();
	private final JTextField portField = new JTextField();
	private final JCheckBox connectOnStartup = new JCheckBox();
	
	private final static Logger log = Logger.getLogger(SocketConnectionView.class);
	
	private SocketConnectionProfile model = new SocketConnectionProfile();

	public SocketConnectionView() {
		//The host field
		container.add(new JLabel(ResourcePool.getString("host", this),JLabel.RIGHT));
		container.add(hostField);
		
		//The port field
		container.add(new JLabel(ResourcePool.getString("port", this),JLabel.RIGHT));
		container.add(portField);
		
		//The connect-on-startup field
		container.add(new JLabel(ResourcePool.getString("connectOnStartup", this),JLabel.RIGHT));
		container.add(connectOnStartup);
		
		add(container);
		updateView();
	}
	public SocketConnectionView(SocketConnectionProfile model) {
		this();
		//The model
		setModel(model);
	}

	public void setModel(SocketConnectionProfile model) {
		this.model = model;
		updateView();
	}
	
	public SocketConnectionProfile getModel() {
		return this.model;
	}
	
	public void updateView(){
		hostField.setText(model.getHost());
		portField.setText(model.getPort());
		connectOnStartup.setSelected(model.isConnectOnStartup());
	}
	
	public void updateModel(){
		model.setHost(hostField.getText());
		model.setPort(portField.getText());
		model.setConnectOnStartup(connectOnStartup.isSelected());
	}
}
