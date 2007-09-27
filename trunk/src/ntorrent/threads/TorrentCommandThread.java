package ntorrent.threads;

import ntorrent.Controller;
import ntorrent.settings.Constants.Commands;

/**
 * @deprecated
 */
public class TorrentCommandThread implements Runnable {

	String command;
	int[] rows;
	
	public TorrentCommandThread(String c,int[] r) {
		command = c;
		rows = r;
	}

	public void run() {
		switch(Commands.getFromString(command)){
			case START:
				Controller.writeToLog("Starting torrent(s)");
				Controller.getTorrents().start(rows);
				break;
			case STOP:
				Controller.writeToLog("Stopping torrent(s)");
				Controller.getTorrents().stop(rows);
				break;
			case OPEN:
				Controller.writeToLog("Setting torrent(s) open");
				Controller.getTorrents().open(rows);
				break;
			case CHECK_HASH:
				Controller.writeToLog("Hash checking torrent(s)");
				Controller.getTorrents().checkHash(rows);
				break;
			case CLOSE:
				Controller.writeToLog("Setting torrent(s) closed");
				Controller.getTorrents().close(rows);
				break;
			case REMOVE:
				Controller.writeToLog("Removing torrent(s)");
				Controller.getTorrents().erase(rows);
				break;
		}
	}

}
