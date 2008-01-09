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
package io.redstone;

import redstone.xmlrpc.XmlRpcFault;
import redstone.xmlrpc.XmlRpcSocketClient;

public class XmlRpcRedstoneImpl {

	static interface Jira
	{
	    public String login( String username, String password ) throws XmlRpcFault;
	    public void logout( String loginToken ) throws XmlRpcFault;
	}

	public static void main( String[] args ) throws Exception
	{
		XmlRpcSocketClient client = new XmlRpcSocketClient("127.0.0.1",5000);
		Object token = client.invoke("system.listMethods", new Object[]{});
		System.out.println(token);
	}

}
