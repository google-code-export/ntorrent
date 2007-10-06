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
package org.heldig.ntorrent.model;


import org.apache.xmlrpc.XmlRpcException;
import org.heldig.ntorrent.Controller;

/**
 * @author  Kim Eik
 */
public class ModelController {

	TorrentPool torrents;
	Controller parent;
	
	
	public ModelController(Controller c) throws XmlRpcException {
		parent = c;
		torrents = new TorrentPool(parent);
	}

	public TorrentPool getTorrentPool() {
		return torrents;
	}

}
