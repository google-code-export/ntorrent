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
package org.heldig.ntorrent.io.xmlrpc.ssh;


import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.heldig.ntorrent.gui.profile.ClientProfile;
import org.heldig.ntorrent.io.RpcConnection;
import org.heldig.ntorrent.io.xmlrpc.CustomTypeFactory;
import org.heldig.ntorrent.io.xmlrpc.XmlRpcQueue;
import org.heldig.ntorrent.io.xmlrpc.XmlRpcSCGITransportFactory;

import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.configuration.SshConnectionProperties;
import com.sshtools.j2ssh.forwarding.ForwardingClient;
import com.sshtools.j2ssh.transport.ConsoleKnownHostsKeyVerification;
import com.sshtools.j2ssh.transport.InvalidHostFileException;

/**
 * @author Kim Eik
 *
 */
public class SshConnection implements RpcConnection {
	
		XmlRpcClientConfigImpl config;
		XmlRpcQueue client;
    	SshClient ssh = new SshClient();
    	SshConnectionProperties prop = new SshConnectionProperties();
    	PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
    	ClientProfile profile;
	  
	public SshConnection(ClientProfile p) throws InvalidHostFileException, UnknownHostException, IOException {
		profile = p;
    	prop.setHost(p.getHost());
    	prop.setPort(p.getConnectionPort());
    	ssh.connect(prop, new ConsoleKnownHostsKeyVerification());
    			
    	
		config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL("http://127.0.0.1:"+p.getSocketPort()));
		config.setEnabledForExtensions(true);
		config.setEnabledForExceptions(true);
		client = new XmlRpcQueue(config);
		client.setConfig(config);
		client.setTransportFactory(new XmlRpcSCGITransportFactory(client));
		client.setTypeFactory(new CustomTypeFactory(null));
	}
	  
	public XmlRpcQueue connect() throws XmlRpcException, IOException {
		  switch(ssh.authenticate(pwd)){
		  case AuthenticationProtocolState.FAILED:
			  throw new IOException("The authentication failed");
		  case AuthenticationProtocolState.PARTIAL:
			  throw new IOException("The authentication succeeded but another"
              + "authentication is required");
		  case AuthenticationProtocolState.COMPLETE:
			  System.out.println("The authentication is complete");
		  }
		  
		  ForwardingClient forwarding = ssh.getForwardingClient();
		  System.out.println("adding forwarding 0.0.0.0:"+profile.getSocketPort()+" - "+prop.getHost()+":"+profile.getSocketPort());
		  forwarding.addLocalForwarding("ntorrent","0.0.0.0",profile.getSocketPort(),prop.getHost(),profile.getSocketPort());
		  forwarding.startLocalForwarding("ntorrent");

	    Object[] params = {"apache"};
	    client.execute("xmlrpc_dialect", params);
		return client;
	}

	public void setPassword(String p) {
		pwd.setPassword(p);	
	}

	public void setUsername(String u) {
		pwd.setUsername(u);
	}
	
	public SshClient getSsh() {
		return ssh;
	}
}
