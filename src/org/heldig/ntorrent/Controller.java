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


package org.heldig.ntorrent;

import java.io.IOException;

import org.heldig.ntorrent.gui.GUIController;
import org.heldig.ntorrent.io.IOController;
import org.heldig.ntorrent.model.ClientProfile;
import org.heldig.ntorrent.model.ModelController;
import org.heldig.ntorrent.threads.ThreadController;



/**
 * @author   Kim Eik
 */
public class Controller{
	
	private String[] filesToLoad;
	public IOController IO;
	public GUIController GC;
	public ThreadController TC;
	public ModelController MC;
	public ClientProfile profile;
	
	public Controller(String[] args) throws IOException{
		this();
		filesToLoad = args;
	}
	
	public Controller() throws IOException {
		IO = new IOController();
		GC = new GUIController(this);
	}
	
	public boolean connect(ClientProfile p){
		profile = p;
		try {
			System.out.println("Connecting");
			IO.connect(p);
			IO.loadStartupFiles(filesToLoad);
			GC.drawMainWindow();
			
			MC = new ModelController(this);
			TC = new ThreadController(this);
			
			GC.getTorrentTableModel().fillData(MC.getTorrentPool());
			TC.startMainContentThread();
			TC.startThrottleThread();
		} catch (Exception e) {
			GC.showError(e);
			e.printStackTrace();
			return false;
		}
		return true;
	}

}