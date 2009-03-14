package ntorrent.connection;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import ntorrent.Session;
import ntorrent.connection.model.ConnectListener;
import ntorrent.connection.view.ConnectionProfileView;
import ntorrent.connection.view.ProxyView;
import ntorrent.io.xmlrpc.XmlRpcConnection;

public class ConnectionController implements ConnectListener {
	private static ConnectionProfileView display = null;
	private Session session = null;


	public Container getDisplay() {
		if(display == null){
			display = new ConnectionProfileView(this);
		}
		return display;
	}

	@Override
	public void connect(final XmlRpcConnection connection) throws Exception {
		try{
			new Thread(){
				public void run() {
					connection.connect();
					session = new Session(connection);
					session.run();
				};
			}.run();
		}catch (Exception e) {
			throw e;
		}
	}

}
