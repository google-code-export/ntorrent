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
package ntorrent.env;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import org.java.plugin.ObjectFactory;
import org.java.plugin.PluginManager;

import ntorrent.Session;
import ntorrent.gui.MainWindow;
import ntorrent.io.logging.SystemLog;
import ntorrent.io.settings.LocalSettings;

public class Environment {
	
	/** App name **/
	private static final String appName = "nTorrent-0.5-alpha";
	
	/** Plugin manager **/
	private static PluginManager pluginManager;
	
	/** the users home dir **/
	private static File home;
	
	/** the users ntorrent dir **/
	private static File ntorrent;
	
	/** Logging system **/
	private static SystemLog log;
		
	/** Locale specification **/
	private static Locale locale;
	
	/** Translations **/
	private static ResourceBundle messages;
	
	/** Internal communication port **/
	private static int intSocketPort;
	
	public static String getString(String key) {
		try{
			return messages.getString(key);
		}catch(NullPointerException x){
			return key;
		}
	}
	
	public static String getAppName() {
		return appName;
	}

	/**
	 * @return the home
	 */
	public static final File getHome() {
		return home;
	}

	/**
	 * @return the ntorrent
	 */
	public static final File getNtorrentDir() {
		return ntorrent;
	}

	/**
	 * @return the log
	 */
	public static final SystemLog getLog() {
		return log;
	}

	/**
	 * @return the locale
	 */
	public static final Locale getLocale() {
		return locale;
	}

	/**
	 * @return the messages
	 */
	public static final ResourceBundle getMessages() {
		return messages;
	}

	/**
	 * @return the intSocketPort
	 */
	public static final int getIntSocketPort() {
		return intSocketPort;
	}

	/**
	 * @param home the home to set
	 */
	public static final void setHome(File home) {
		Environment.home = home;
	}

	/**
	 * @param ntorrent the ntorrent to set
	 */
	public static final void setNtorrentDir(File ntorrent) {
		Environment.ntorrent = ntorrent;
	}

	/**
	 * @param log the log to set
	 */
	public static final void setLog(SystemLog log) {
		Environment.log = log;
	}

	/**
	 * @param locale the locale to set
	 */
	public static final void setLocale(Locale locale) {
		Environment.locale = locale;
	}

	/**
	 * @param messages the messages to set
	 */
	public static final void setMessages(ResourceBundle messages) {
		Environment.messages = messages;
	}

	/**
	 * @param intSocketPort the intSocketPort to set
	 */
	public static final void setIntSocketPort(int intSocketPort) {
		Environment.intSocketPort = intSocketPort;
	}
	
	public static PluginManager getPluginManager() {
		return pluginManager;
	}

	public static void setPluginManager(PluginManager manager) {
		pluginManager = manager;
	}
	

}
