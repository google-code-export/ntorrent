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
package ntorrent.io.settings;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

public interface Constants {
	
	/** App name **/
	public static final String appName = "nTorrent-0.5-alpha";
	
	/** the users home dir **/
	public static final File home = new File(System.getProperty("user.home"));
	
	/** the users ntorrent dir **/
	public static final File ntorrent = new File(home,".ntorrent");
	
	/** the users language **/
	public static final String language = System.getProperty("user.language");
	
	/** the users country **/
	public static final String country = System.getProperty("user.country");
	
	/** the java vm version **/
	public static final String javaSpec = System.getProperty("java.specification.version");
	
	/** ntorrent comm port. **/
	public static final int intSocketPort = 4050;
	
	/** Locale specification **/
	public static Locale locale = new Locale(language,country);
	
	/** Translations **/
	public static ResourceBundle messages = ResourceBundle.getBundle("ntorrent", locale);
}
