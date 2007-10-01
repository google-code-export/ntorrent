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

import ntorrent.io.Rpc;
import ntorrent.io.xmlrpc.XmlRpcCallback;
import ntorrent.settings.LocalSettings;

/**
 * @author Kim Eik
 *
 */
public class ThrottleThread extends Thread{
	Rpc IO;
	XmlRpcCallback callback;
	Integer drate;
	Integer urate;
	
	ThrottleThread(Rpc r, XmlRpcCallback c){
		IO = r;
		callback = c;
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
				sleep(LocalSettings.sintervall-5000);
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
