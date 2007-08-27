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

package ntorrent.io;

import java.net.MalformedURLException;
import java.net.URL;

import ntorrent.io.type.CustomTypeFactory;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcLiteHttpTransportFactory;

public class RpcConnection {
	XmlRpcClientConfigImpl config;
	XmlRpcClient client;
	
	public RpcConnection(String url) throws MalformedURLException{
		config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL(url));
		config.setEnabledForExtensions(true);
		config.setEnabledForExceptions(true);
		client = new XmlRpcClient();
		//client.setTransportFactory(new XmlRpcLiteHttpTransportFactory(client));
		client.setConfig(config);
		client.setTypeFactory(new CustomTypeFactory(null));
	}
	
	public void setUsername(String u){
		config.setBasicUserName(u);
	}
	
	public void setPassword(String p){
		config.setBasicPassword(p);
	}
	
	public XmlRpcClient connect(){
       client.setConfig(config);
        return client;
	}
}
