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

package ntorrent.threads;

import ntorrent.Controller;
import ntorrent.gui.status.StatusBarComponent;
import ntorrent.settings.LocalSettings;

public class ContentThread extends Controller implements Runnable {
	StatusBarComponent bar = getGui().getStatusBar();
	
	public void run(){
		try {
			rpc.getTorrentSet(torrents.getView(), torrents);
			rpc.getPortRange(bar);
			rpc.getUploadRate(bar);
			rpc.getDownloadRate(bar);
			bar.setDownloadRate(torrents.getRateDown());
			bar.setUploadRate(torrents.getRateUp());
			while(true){
				try {
					rpc.getTorrentVariables(torrents.getView(),torrents);
					bar.repaint();
					Thread.sleep(LocalSettings.vintervall);
				} catch (InterruptedException e) {
					torrents.getTable().fireTableDataChanged();
					rpc.getTorrentSet(torrents.getView(), torrents);
				}
			}
		} catch (Exception e) {
			Controller.getGui().showError(e.getLocalizedMessage());
			Controller.writeToLog(e);
		}
	}
	
}
