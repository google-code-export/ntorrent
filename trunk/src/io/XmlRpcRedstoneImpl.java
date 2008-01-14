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
package io;

import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcFault;
import redstone.xmlrpc.XmlRpcProxy;
import redstone.xmlrpc.XmlRpcSocketClient;

public class XmlRpcRedstoneImpl {

	static interface system
	{
	    public XmlRpcArray listMethods() throws XmlRpcFault;
	}

	public static void main( String[] args ) throws Exception
	{
		XmlRpcSocketClient client = new XmlRpcSocketClient("127.0.0.1",5000);
		system proxy = (system)XmlRpcProxy.createProxy(new Class[] { system.class }, client);
		for (Object o : proxy.listMethods()) {
			System.out.println(o+"\t "+client.invoke("system.methodSignature", new Object[] {o}));
		}
		System.out.println();
		

	}

}
