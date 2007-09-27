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

/**
 * @deprecated
 */
public class FileCommandThread extends Controller implements Runnable {
	
	private String command;
	private int[] indexes;
	private String hash;

	public FileCommandThread(String c,String h,int[] i) {
		command = c;
		indexes = i;
		hash = h;
	}
	
	public void run() {
			if(command.equals("High")){
				setPriority(2);
			}else if(command.equals("Low")){
				setPriority(1);				
			}else if(command.equals("Off")){
				setPriority(0);				
			}
			Controller.writeToLog("Setting priority");
	}

	private void setPriority(int pri){
		rpc.setFilePriority(hash,pri,indexes);
	}
}
