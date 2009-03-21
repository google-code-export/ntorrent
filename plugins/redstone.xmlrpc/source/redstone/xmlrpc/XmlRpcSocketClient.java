package redstone.xmlrpc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.Socket;

import redstone.xmlrpc.util.Scgi;


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

public class XmlRpcSocketClient extends XmlRpcClient {
	
	private final String host;
	private final int port;
	private Socket connection;

	public XmlRpcSocketClient(String host, int port) throws XmlRpcException{
		this.host = host;
		this.port = port;
		//test the connection
		Socket s;
		try {
			s = new Socket(host,port);
			s.close();
		} catch (Exception e) {
			throw new XmlRpcException(e.getMessage(),e);
		}
	}
	
	public Socket getConnection() {
		return connection;
	}
	
	@Override
	protected void beginCall(String methodName) throws XmlRpcException {
		try {
			//if(connection == null) each request needs a new socket?
				connection = new Socket(host,port);
		} catch (Exception e) {
			throw new XmlRpcException(e.getMessage(),e);
		}
		super.beginCall(methodName);
	}
	
	@Override
	protected void endCall()throws XmlRpcException,XmlRpcFault{
		super.endCall();
		try {
	        StringBuffer buffer = new StringBuffer(Scgi.make(null,writer.toString()));
	        OutputStream output = new BufferedOutputStream(connection.getOutputStream());
			
	        //System.out.println(buffer.toString().replace('\0', ' '));
	        
	        output.write( buffer.toString().getBytes() );
	        output.flush();
	        
	        InputStream input = new BufferedInputStream(connection.getInputStream());
	        
            
	        /*
	         * ignores response message of scgi server.
	         */
	        int c = 0;
	        while(c != -1){
	        	c = input.read();
	        	if(c == '\r' && input.read() == '\n')
	        		if(input.read() == '\r' && input.read() == '\n')
	        			break;
	        }
	        
	        handleResponse(input);
			
		} catch (IOException e) {
			throw new XmlRpcException(e.getMessage(),e);
		}
	}

}
