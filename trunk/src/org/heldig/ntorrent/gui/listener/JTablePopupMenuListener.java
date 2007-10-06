package org.heldig.ntorrent.gui.listener;

import java.awt.event.ActionEvent;

import org.heldig.ntorrent.Controller;
import org.heldig.ntorrent.settings.Constants.Commands;


public abstract class JTablePopupMenuListener extends JPopupMenuListener {
	protected int[] selectedRows;
	
	public JTablePopupMenuListener(Controller c, Object[] menuItems) {
		super(c,menuItems);
	}
	
	public void actionPerformed(ActionEvent e) {
		Commands cmd = Commands.getFromString(e.getActionCommand());
		if(cmd != null)
			switch(cmd){
			case START:
				System.out.println("Starting torrent(s)");
				C.getMC().getTorrentPool().start(selectedRows);
				break;
			case STOP:
				System.out.println("Stopping torrent(s)");
				C.getMC().getTorrentPool().stop(selectedRows);
				break;
			case OPEN:
				System.out.println("Setting torrent(s) open");
				C.getMC().getTorrentPool().open(selectedRows);
				break;
			case CHECK_HASH:
				System.out.println("Hash checking torrent(s)");
				C.getMC().getTorrentPool().checkHash(selectedRows);
				break;
			case CLOSE:
				System.out.println("Setting torrent(s) closed");
				C.getMC().getTorrentPool().close(selectedRows);
				break;
			case REMOVE:
				System.out.println("Removing torrent(s)");
				C.getMC().getTorrentPool().erase(selectedRows);
				break;
			}
		}
	
}
