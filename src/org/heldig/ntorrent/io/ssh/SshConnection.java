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
package org.heldig.ntorrent.io.ssh;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import org.heldig.ntorrent.io.RpcConnection;

import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.configuration.SshConnectionProperties;
import com.sshtools.j2ssh.forwarding.ForwardingClient;
import com.sshtools.j2ssh.transport.ConsoleKnownHostsKeyVerification;

/**
 * @author Kim Eik
 *
 */
public class SshConnection implements RpcConnection {
	  // A buffered reader so we can request information from the user
	  private static BufferedReader reader =
	        new BufferedReader(new InputStreamReader(System.in));
	  
	  public static void main(String args[]) {
		    try {
		    	SshClient ssh 
		    	  = new SshClient();


		    	System.out.print("Host to connect: ");

		    	String hostname = reader.readLine();
		    	
		    	System.out.print("port: ");

		    	//String port= reader.readLine();
		    	SshConnectionProperties prop = new SshConnectionProperties();
		    	prop.setHost(hostname);
		    	//prop.setPort(Integer.parseInt(port));

		    	ssh.connect(hostname, new ConsoleKnownHostsKeyVerification());

		    	
		    	  PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
		    	  System.out.print("Username: ");
		    	  String username = reader.readLine();
		    	  pwd.setUsername(username);
		    	  
		    	  System.out.print("Password: ");
		    	  String password = reader.readLine();
		    	  pwd.setPassword(password);
		    	  
		    	  int result = ssh.authenticate(pwd);
		    	  
		    	  if(result==AuthenticationProtocolState.FAILED)
		    		     System.out.println("The authentication failed");

		    		  if(result==AuthenticationProtocolState.PARTIAL)
		    		     System.out.println("The authentication succeeded but another"
		    			                   + "authentication is required");

		    		  if(result==AuthenticationProtocolState.COMPLETE)
		    		     System.out.println("The authentication is complete");
		    		  
		    		  ForwardingClient forwarding = ssh.getForwardingClient();
		    		  
		    		  forwarding.addLocalForwarding("test","0.0.0.0",6000,"127.0.0.1",5000);
		    		  forwarding.startLocalForwarding("test");
		    		  Socket socket = new Socket("localhost",6000);
		    		  //should now be connected to the remote scgi socket.
		    		  

			} catch(Exception e) {
		      e.printStackTrace();
		    }
		  }
}
