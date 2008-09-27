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
package ntorrent.io.xmlrpc;

import com.jcraft.jsch.Logger;

/**
 * This class governs the logging of ssh connections.
 * (however, might be unecessary)
 * @author Kim Eik
 *
 */
public class SSHLogger implements Logger {

	public boolean isEnabled(int level) {
		return true;
	}

	public void log(int level, String message) {
		String type = "";
		if(level == INFO){
			type = "INFO";
		}else if(level == DEBUG){
			type = "DEBUG";
		}else if(level == WARN){
			type = "WARN";
		}else if(level == FATAL){
			type = "FATAL";
		}else if(level == ERROR){
			type = "ERROR";
		}
			
		java.util.logging.Logger.global.log(SSHLoggerLevel.SSH,type+": "+message);
		
	}

}
