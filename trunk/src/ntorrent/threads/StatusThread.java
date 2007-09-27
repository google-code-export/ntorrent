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
 *  
 */

package ntorrent.threads;

import ntorrent.Controller;
import ntorrent.gui.status.StatusBarComponent;
import ntorrent.settings.LocalSettings;

public class StatusThread extends Controller implements Runnable {
	StatusBarComponent bar = getGui().getStatusBar();
	public void run(){
		boolean firstRun = true;		
		while(true){
			
			//try {
				if(firstRun){
					//bar.setPort(rpc.getPortRange());
					//bar.setMaxUploadRate((int)rpc.getUploadRate()/1024);
					//bar.setMaxDownloadRate((int)rpc.getDownloadRate()/1024);
					firstRun = false;
				}
				//Seeders
				//Leechers and so on.
				bar.repaint();
			//} catch (XmlRpcException e) {
				//Controller.getGui().showError(e.getLocalizedMessage());
				//Controller.writeToLog(e);
			//}	
			
			try {
				//doesn't need to update so often. rate is updated when interrupted.
				Thread.sleep(LocalSettings.sintervall);
			} catch (InterruptedException e1) {
				updateRate();
			}
		}
	}
	
	public void updateRate(){
		bar.setDownloadRate(torrents.getRateDown());
		bar.setUploadRate(torrents.getRateUp());
	}
}
