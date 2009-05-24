package ntorrent.connection.model;

import java.lang.Thread.UncaughtExceptionHandler;

public interface ConnectListener {
	public void connect(final ConnectionProfileExtension connectionProfile, final UncaughtExceptionHandler exceptionHandler) throws Exception;
}
