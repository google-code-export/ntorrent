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
package ntorrent.data;

import java.io.File;


import org.java.plugin.PluginManager;
import org.java.plugin.boot.Application;
import org.java.plugin.boot.ApplicationPlugin;
import org.java.plugin.util.ExtendedProperties;

/**
 * @author Kim Eik
 *
 */
public class Environment extends ApplicationPlugin implements Application {
	
	/** App name **/
	public final static String appName = "nTorrent-0.5-alpha";
	
	/** the users ntorrent dir **/
	private static File ntorrent;
	
	/** Internal communication port **/
	private static int intSocketPort;

	private static String userLanguage;
	
	private static String userCountry;
	
	private static String args[];
	
	private static PluginManager pluginManager;


	@Override
	protected Application initApplication(ExtendedProperties config, String[] args) throws Exception {
		this.args = args;
		this.pluginManager = super.getManager();
		ntorrent = new File(config.getProperty("userNtorrentDir"));
		intSocketPort = Integer.parseInt(config.getProperty("internalCommPort"));
		userLanguage = config.getProperty("userLanguage");
		userCountry = config.getProperty("userCountry");
		return this;
	}
	
	public void startApplication() throws Exception {
		pluginManager.activatePlugin("ntorrent");
	}
	
	@Override
	protected void doStart() throws Exception {
		//do nothing
	}

	@Override
	protected void doStop() throws Exception {
		//do nothing
	}
	
	public static File getNtorrentDir() {
		return ntorrent;
	}

	public static int getIntSocketPort() {
		return intSocketPort;
	}
	
	public static String getUserLanguage() {
		return userLanguage;
	}
	
	public static String getUserCountry() {
		return userCountry;
	}
	
	public static String[] getArgs() {
		return args;
	}

	public static PluginManager getPluginManager() {
		return pluginManager;
	}
}
