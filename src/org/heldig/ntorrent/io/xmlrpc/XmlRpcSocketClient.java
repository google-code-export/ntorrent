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
package org.heldig.ntorrent.io.xmlrpc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcFault;
import redstone.xmlrpc.XmlRpcInvocationHandler;
import redstone.xmlrpc.XmlRpcMessages;
import redstone.xmlrpc.XmlRpcParser;
import redstone.xmlrpc.XmlRpcSerializer;
import redstone.xmlrpc.XmlRpcStruct;

/**
 * @author Kim Eik
 *
 */
public class XmlRpcSocketClient extends XmlRpcParser implements XmlRpcInvocationHandler{
	
	private final Socket connection;
	private Object returnValue;
	private StringWriter writer = new StringWriter();
	private final XmlRpcSerializer serializer = new XmlRpcSerializer();
	private boolean isFaultResponse;
	
	public XmlRpcSocketClient(String host, int port) throws UnknownHostException, IOException {
		connection = new Socket(host,port);
	}
	
	
	protected void handleParsedValue(Object value) {
		returnValue = value;
	}

	@SuppressWarnings("unchecked")
	
	public Object invoke(String method, List arguments) throws Throwable {
        beginCall(method);
        if ( arguments != null ){
            Iterator argIter = arguments.iterator();
            while (argIter.hasNext()){
                try{
                	writer.write( "<param>" );
                    serializer .serialize( argIter.next(), writer );
                    writer.write( "</param>" );
                }
                catch ( IOException ioe ){
                    throw new XmlRpcException(
                        XmlRpcMessages.getString( "XmlRpcClient.NetworkError" ), ioe );
                }
            }
        }

        return endCall();
	}

    private void beginCall( String methodName ) throws XmlRpcException{
        writer = new StringWriter();
		writer.write( "<?xml version=\"1.0\" encoding=\"" );
		writer.write( XmlRpcMessages.getString( "XmlRpcClient.Encoding" ) );
		writer.write( "\"?>\n" );
		writer.write( "<methodCall>\n<methodName>" );
		writer.write( methodName );
		writer.write( "</methodName>\n<params>" );
    }
	
    private Object endCall() throws XmlRpcException, XmlRpcFault, IOException{
        try
        {
            writer.write( "</params>\n" );
            writer.write( "</methodCall>\n" );
            writer.flush();
            
           setSCGIHeader();
           BufferedWriter so = new BufferedWriter(
        		   new OutputStreamWriter(
        				   connection.getOutputStream()));
           	so.write(writer.toString());
           	so.flush();
           	System.out.println(writer.toString());
            handleResponse();
        }
        catch (IOException ioe){
            throw new XmlRpcException(XmlRpcMessages.getString( "XmlRpcClient.NetworkError" ),ioe);
        }
        finally{
            writer.close();
            connection.close();
        }
        return returnValue;
    }
    
   private void setSCGIHeader(){
	   final String[] headers = {
			   "CONTENT_LENGTH",
			   "SCGI",
			   };
	   
	   final String[] values = {
			   String.valueOf(writer.toString().length()),
			   "1",
	   };
	   
	   StringBuffer header = new StringBuffer();
	   int clength = 0;
	   for(int x = 0; x < headers.length; x++){
		   //plussing 2 cause <00> counts as 1 char.
		   clength += headers[x].length()+values[x].length()+2;
		   header.append(headers[x]+'\0'+values[x]+'\0');
	   }
	   
	   header.insert(0, clength+":");
	   header.append(",");
	   writer.getBuffer().insert(0, header);
   }
   
   private void handleResponse() throws XmlRpcFault, IOException
    {
	   
	   InputStream stream = connection.getInputStream();
        try
        {
            parse(stream);
        }
        catch ( Exception e )
        {
            throw new XmlRpcException(
                XmlRpcMessages.getString( "XmlRpcClient.ParseError" ), e );
        }
        
        if ( isFaultResponse )
        {
            XmlRpcStruct fault = ( XmlRpcStruct ) returnValue;
            isFaultResponse = false;
            
            throw new XmlRpcFault( fault.getInteger( "faultCode" ),
                                   fault.getString( "faultString" ) );
        }
    }
 
	   
	public void parse(InputStream is) throws XmlRpcException {
		   String headers = new String();
		   while(!headers.endsWith("\r\n\r\n")){
			   try {
				   headers += (char)is.read();
				} catch (IOException e) {
					e.printStackTrace();
				}
		   }
		   //System.out.println(headers);
		   super.parse(is);
	}
   
}
