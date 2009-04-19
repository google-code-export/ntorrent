package ntorrent.connection;

import java.awt.Container;

import ntorrent.Session;
import ntorrent.connection.model.ConnectListener;
import ntorrent.connection.model.ConnectionProfileExtension;
import ntorrent.connection.view.ConnectionProfileView;
import ntorrent.io.xmlrpc.XmlRpcConnection;

public class ConnectionController implements ConnectListener  {
	private static ConnectionProfileView display = null;
	private Session session = null;

	public Container getDisplay() {
		if(display == null){
			display = new ConnectionProfileView(this);
		}
		return display;
	}

	@Override
	public void connect(final ConnectionProfileExtension connectionProfile) throws Exception {
		try{
			new Thread(){
				public void run() {
					XmlRpcConnection connection = connectionProfile.getConnection();
					connection.connect();
					session = new Session(connectionProfile.getName(),connection);
					session.run();
					display.reset();
				};
			}.start();
		}catch (Exception e) {
			throw e;
		}
	}

}
