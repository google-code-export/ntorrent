package ntorrent.controller.threads;

import java.util.Vector;

import ntorrent.controller.Controller;
import ntorrent.settings.Constants.Commands;

public class TorrentCommandThread implements Runnable {

	String command;
	Vector<Integer> rows;
	
	public TorrentCommandThread(String c,Vector<Integer> r) {
		command = c;
		rows = r;
	}

	public void run() {
		switch(Commands.getFromString(command)){
			case START:
				Controller.writeToLog("Starting torrent(s)");
				for(int i : rows)
					Controller.getTorrents().start(i);
				break;
			case STOP:
				Controller.writeToLog("Stopping torrent(s)");
				for(int i : rows)
					Controller.getTorrents().stop(i);
				break;
			case OPEN:
				Controller.writeToLog("Setting torrent(s) open");
				for(int i : rows)
					Controller.getTorrents().open(i);
				break;
			case CHECK_HASH:
				Controller.writeToLog("Hash checking torrent(s)");
				for(int i : rows)
					Controller.getTorrents().checkHash(i);
				break;
			case CLOSE:
				Controller.writeToLog("Setting torrent(s) closed");
				for(int i : rows)
					Controller.getTorrents().close(i);
				break;
			case REMOVE:
				Controller.writeToLog("Removing torrent(s)");
				for(int i : rows)
					Controller.getTorrents().erase(i);
				break;
		}
	}

}
