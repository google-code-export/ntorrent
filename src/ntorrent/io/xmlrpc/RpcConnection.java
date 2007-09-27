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

import java.net.MalformedURLException;
import java.net.URL;

import ntorrent.io.xmlrpc.type.CustomTypeFactory;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class RpcConnection {
	XmlRpcClientConfigImpl config;
	RpcQueue client;
	
	public RpcConnection(String url) throws MalformedURLException{
		config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL(url));
		config.setEnabledForExtensions(true);
		config.setEnabledForExceptions(true);
		client = new RpcQueue(config);
		//client.setTransportFactory(new XmlRpcTransportFactory(client));
		client.setTypeFactory(new CustomTypeFactory(null));
	}
	
	public void setUsername(String u){
		config.setBasicUserName(u);
	}
	
	public void setPassword(String p){
		config.setBasicPassword(p);
	}
	
	public RpcQueue connect() throws XmlRpcException{
       client.setConfig(config);
       //makes sure that server and client speak the same dialect.
       Object[] params = {"apache"};
       client.execute("xmlrpc_dialect", params);
       return client;
	}
}
