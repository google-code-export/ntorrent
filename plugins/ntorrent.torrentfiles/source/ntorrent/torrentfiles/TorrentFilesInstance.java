package ntorrent.torrentfiles;

import javax.swing.JTabbedPane;

import ntorrent.locale.ResourcePool;
import ntorrent.session.ConnectionSession;
/**
 *   nTorrent - A GUI client to administer a rtorrent process 
 *   over a network connection.
 *   
 *   Copyright (C) 2007  Kim Eik
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import ntorrent.session.view.SessionFrame;
import ntorrent.torrentfiles.model.TorrentFilesTreeTableModel;
import ntorrent.torrentfiles.view.TorrentFilesTreeTable;
import ntorrent.torrenttable.TorrentTableInterface;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentSelectionListener;

/**
 * @author Kim Eik
 *
 */
public class TorrentFilesInstance implements TorrentSelectionListener {

	final private TorrentFilesTreeTableModel treeModel = new TorrentFilesTreeTableModel();
	final private TorrentFilesTreeTable treeTable = new TorrentFilesTreeTable(treeModel);
	
	final private JTabbedPane container;
	final private TorrentTableInterface tableController;
	
	
	private boolean started;
	
	public TorrentFilesInstance(ConnectionSession session) {
		SessionFrame frame = session.getDisplay();
		
		//init needed variables
		container = frame.getTabbedPane();
		tableController = session.getTorrentTableController();
		
		//add this as a selection listener
		tableController.addTorrentSelectionListener(this);
	}
	
	public void start(){
		started = true;
		container.addTab(ResourcePool.getString("tabname", "locale", this), treeTable);
	}
	
	public void stop(){
		started = false;
		int index = container.indexOfComponent(treeTable);
		container.removeTabAt(index);
	}

	public void torrentsSelected(Torrent[] tor) {
		System.out.println(tor);
	}
	
	public boolean isStarted() {
		return started;
	}

}
