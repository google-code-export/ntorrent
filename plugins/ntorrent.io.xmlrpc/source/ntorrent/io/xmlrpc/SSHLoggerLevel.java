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

import java.util.logging.Level;

/**
 * This class is creates a logging level to SSHLogger.
 * (is however maybe unecessary)
 * @author Kim Eik
 *
 */
public class SSHLoggerLevel extends Level {

	private static final long serialVersionUID = 1L;

	protected SSHLoggerLevel(String name, int value) {
		super(name, value);
	}

    public static Level SSH =
        new SSHLoggerLevel("SSH", Level.INFO.intValue()+55);
    
}
