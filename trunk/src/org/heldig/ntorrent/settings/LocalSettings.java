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
package org.heldig.ntorrent.settings;

import java.io.File;
import java.io.IOException;

import org.heldig.ntorrent.gui.torrent.TorrentInfo;

public class LocalSettings extends Settings {
	private static final long serialVersionUID = 1L;
	@Description("Update intervall in ms / view")
	public int vintervall = 3000;
	
	/**
	 * @TODO set a minimum value? @Minval?
	 */
	@Description("Refresh intervall in ms / throttle")
	public int sintervall = 60000;
	
	@Description("Default command for opening local data")
	public String filecommand = "gnome-open";
	
	@Description("Default command for erasing local data")
	public String deletecommand = "rm";
	
	@Description("Language file")
	public File language = new File("language/default.lang");
	
	@Description("Debug mode / log")
	public boolean debug = false;
	
	@SuppressWarnings("static-access")
	@Override
	protected void restoreData(Object obj) {
		LocalSettings data = (LocalSettings)obj;
		vintervall = data.vintervall;
		sintervall = data.sintervall;
		debug = data.debug;
	}

	public void deserialize() {
		try {
			deserialize(Constants.settings, this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void serialize() {
		try {
			serialize(Constants.settings, this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void runProgram(TorrentInfo info) throws IOException{
		File file = new File(info.getFilePath());
		File cwd = new File(file.getParent());
		String arg = file.getName();
		String cmd[] = {filecommand,arg};
		System.out.println("Executing "+cmd[0]+" "+arg+" in "+cwd);
		Runtime.getRuntime().exec(cmd,null,cwd);
	}
		
}
