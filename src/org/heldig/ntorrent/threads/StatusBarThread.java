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
package org.heldig.ntorrent.threads;

import org.heldig.ntorrent.NTorrent;
import org.heldig.ntorrent.gui.statusbar.StatusBarComponent;
import org.heldig.ntorrent.io.Rpc;
import org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback;


/**
 * @author Kim Eik
 *
 */
public class StatusBarThread extends Thread{
	Rpc IO;
	XmlRpcCallback callback;
	Integer drate;
	Integer urate;
	
	public StatusBarThread(Rpc r, StatusBarComponent s){
		IO = r;
		callback = s;
		s.setThread(this);
	}
	
	public void setDownloadRate(int i){
		drate = i;
		interrupt();
	}
	
	public void setUploadRate(int i){
		urate = i;
		interrupt();
	}
	
	public void run() {
		getServerState();
		while(true){
			try {
				sleep(5000);
				if(drate != null || urate != null)
					setServerRate();
				getServerRate();
				sleep(NTorrent.settings.sintervall-5000);
			} catch (InterruptedException e) {
				
			}
		}
	}
	
	private void getServerState(){
		getServerRate();
		IO.getPortRange(callback);
	}
	
	private void getServerRate() {
		IO.getDownloadRate(callback);
		IO.getUploadRate(callback);
	}

	private void setServerRate(){
		System.out.println("Setting server rate");
		if(drate != null)
			IO.setDownloadRate(drate,null);
		if(urate != null)
			IO.setUploadRate(urate, null);
		drate = urate = null;
	}
}
