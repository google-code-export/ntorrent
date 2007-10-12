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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author  Kim Eik
 */
public final class Constants {
	static Properties systemProperties = new Properties();
	
	private static final String NAME = "nTorrent";
	private static final String VERSION = "0.3.alpha";
	public static final File profile = new File("profile.dat");
	public static final File settings = new File("settings.dat");
	private static final int commPort = 4050;
	
	public static String getReleaseName(){
		final String BUILD;
		
		FileInputStream stream;
		try {
			stream = new FileInputStream("build.number");
			systemProperties.load(stream);
		} catch (FileNotFoundException x) {
			//do not change
			x.printStackTrace();
		} catch (IOException x) {
			//do not change
			x.printStackTrace();
		}
		
		BUILD = systemProperties.getProperty("build.number");
		return  NAME+"-"+VERSION+" (build "+BUILD+")";
	}
	
	public static String getLicense(){
		return getReleaseName()+"\n\nCopyright (C) 2007  Kim Eik\n"+
	     "This program comes with ABSOLUTELY NO WARRANTY\n"+
	     "This is free software, and you are welcome to \n" +
	     "redistribute it under certain conditions.\n\n";
	}
	
	/**
	 * @return
	 */
	public static int getCommPort() {
		return commPort;
	}
}
