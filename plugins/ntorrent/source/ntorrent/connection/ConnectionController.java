package ntorrent.connection;

import java.awt.Component;

import javax.swing.JComponent;

import ntorrent.Session;
import ntorrent.connection.view.ConnectionProfileView;
import ntorrent.io.xmlrpc.XmlRpcConnection;

public class ConnectionController {
	private final Component display;
	private Session session = null;

	public ConnectionController() {
		display = new ConnectionProfileView(this);
	}

	public Component getDisplay() {
		return display;
	}

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
