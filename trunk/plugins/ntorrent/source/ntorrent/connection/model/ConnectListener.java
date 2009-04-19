package ntorrent.connection.model;

public interface ConnectListener {
	public void connect(final ConnectionProfileExtension connectionProfile) throws Exception;
}
