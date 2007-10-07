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
				C.MC.getTorrentPool().start(selectedRows);
				break;
			case STOP:
				System.out.println("Stopping torrent(s)");
				C.MC.getTorrentPool().stop(selectedRows);
				break;
			case OPEN:
				System.out.println("Setting torrent(s) open");
				C.MC.getTorrentPool().open(selectedRows);
				break;
			case CHECK_HASH:
				System.out.println("Hash checking torrent(s)");
				C.MC.getTorrentPool().checkHash(selectedRows);
				break;
			case CLOSE:
				System.out.println("Setting torrent(s) closed");
				C.MC.getTorrentPool().close(selectedRows);
				break;
			case REMOVE_TORRENT:
				System.out.println("Removing torrent(s)");
				C.MC.getTorrentPool().erase(selectedRows);
				break;
			}
		}
	
}
