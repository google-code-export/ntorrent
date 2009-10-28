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
package ntorrent.torrentinfo;

import java.text.NumberFormat;
import java.util.Date;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import ntorrent.io.rtorrent.Download;
import ntorrent.locale.ResourcePool;
import ntorrent.session.ConnectionSession;
import ntorrent.session.SessionInstance;
import ntorrent.torrenttable.TorrentTableInterface;
import ntorrent.torrenttable.model.Torrent;
import ntorrent.torrenttable.model.TorrentSelectionListener;

/**
 * @author Kim Eik
 *
 */
public class TorrentInfoInstance implements SessionInstance,TorrentSelectionListener {
	private final JTabbedPane tab;
	private final JTextPane textPane = new JTextPane();
	private final JScrollPane scrollpane = new JScrollPane(textPane);
	
	private final TorrentTableInterface tc;
	
	private final Download d;
	
	private final static String bundle = "locale";
	private boolean started = false;
	
	private final static SimpleAttributeSet BOLD = new SimpleAttributeSet();
	static {
		StyleConstants.setBold(BOLD, true);
		StyleConstants.setUnderline(BOLD, true);
	}
	
	public TorrentInfoInstance(ConnectionSession session) {
		//get the torrenttable
		tc = session.getTorrentTableController();
		
		//tabbedpane
		tab = session.getDisplay().getTabbedPane();
		
		//get the xmlrpc client for this session
		d = session.getConnection().getDownloadClient();
		
		//get and fire selection
		torrentsSelected(tc.getSelectedTorrents());
	}

	public boolean isStarted() {
		return started;
	}

	public void start() {
		started = true;
		int preferredIndex = 0;
		if (preferredIndex > tab.getTabCount())
			preferredIndex = tab.getTabCount();

		tab.insertTab(ResourcePool.getString("tabname", "locale", this), null, scrollpane, null,preferredIndex);
		
		//add this as a torrent selection listener
		tc.addTorrentSelectionListener(this);
	}

	public void stop() {
		started = false;
		tc.removeTorrentSelectionListener(this);
		tab.removeTabAt(tab.indexOfComponent(scrollpane));
	}

	public synchronized void torrentsSelected(Torrent[] tor) {
		//NO LONGER THREAD SAFE!
		textPane.setText("");
		if(tor.length == 1){
			String hash = tor[0].getHash();
			
			insertText(ResourcePool.getString("name", bundle, this)+":\n", BOLD);
			insertText("["+(d.is_open(hash) == 1 ? 
						ResourcePool.getString("open", bundle, this) : 
						ResourcePool.getString("closed", bundle, this))
				+"] - "+d.get_name(hash),null);
			insertText("\n\n"+ResourcePool.getString("hash", bundle, this)+":\n", BOLD);
			insertText(hash, null);
			insertText("\n\n"+ResourcePool.getString("directory", bundle, this)+":\n", BOLD);
			insertText(d.get_directory(hash), null);
			insertText("\n\n"+ResourcePool.getString("date-created", bundle, this)+":\n", BOLD);
			insertText(new Date(d.get_creation_date(hash)*1000).toString(), null);
			
			String tiedToFile = d.get_tied_to_file(hash);
			if(tiedToFile.length() > 0){
				insertText("\n\n"+ResourcePool.getString("tied", bundle, this)+":\n", BOLD);
				insertText(tiedToFile, null);
			}
			
			//add target free diskspace
			if (d.is_open(hash) > 0) {
				insertText("\n\n"+ResourcePool.getString("freediskspace", bundle, this)+":\n", BOLD);
				long diskspace = d.get_free_diskspace(hash);
				NumberFormat formatter = NumberFormat.getInstance(ResourcePool.getLocale());
				if (diskspace < 1024*1024) {
					insertText("" + formatter.format(diskspace / 1024) + "KB", null);
				} else {
					insertText("" + formatter.format(diskspace / 1024 / 1024)
							+ "MB", null);
				}
			}

		}
	}
	
	private void insertText(String text, AttributeSet set) {
		try {
		  textPane.getDocument().insertString(
		      textPane.getDocument().getLength(), text, set);
		} catch (BadLocationException e) {
		  e.printStackTrace();
		}
	}	

}
