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
package redstone.xmlrpc;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract class XmlRpcClient extends XmlRpcParser implements XmlRpcInvocationHandler {
		
    /** The parsed value returned in a response. */
    protected Object returnValue;

    /** Writer to which XML-RPC messages are serialized. */
    protected StringWriter writer;
    
    /** Indicates whether or not the incoming response is a fault response. */
    protected boolean isFaultResponse;
    
    /** The serializer used to serialize arguments. */
    protected XmlRpcSerializer serializer = new XmlRpcSerializer();

    public synchronized Object invoke(String method, List arguments ) throws Exception {
        beginCall( method );

        if (arguments != null){
            Iterator argIter = arguments.iterator();

            while ( argIter.hasNext() ){
	            writer.write( "<param>" );
	            serializer.serialize( argIter.next(), writer );
	            writer.write( "</param>" );
            }
        }

        endCall();
        return returnValue;
    }

    public synchronized Object invoke(String method, Object[] arguments ) throws XmlRpcException, XmlRpcFault{
        beginCall( method );
       
        if ( arguments != null ){
            for ( int i = 0; i < arguments.length; ++i ){
	            writer.write( "<param>" );
	            try {
					serializer.serialize( arguments[ i ], writer );
				} catch (IOException e) {
					throw new XmlRpcException(e.getMessage(),e);
				}
	            writer.write( "</param>" );
            }
        }
        
        endCall();
        
        return returnValue;
    }

    protected void beginCall( String methodName ) throws XmlRpcException{
    		writer = new StringWriter( 2048 );
            writer.write( "<?xml version=\"1.0\" encoding=\"" );
            writer.write( XmlRpcMessages.getString( "XmlRpcClient.Encoding" ) );
            writer.write( "\"?>" );
            writer.write( "<methodCall><methodName>" );
            writer.write( methodName );
            writer.write( "</methodName><params>" );
    }

    protected void endCall() throws XmlRpcException, XmlRpcFault {
            writer.write( "</params>" );
            writer.write( "</methodCall>" );
    }

    protected void handleResponse(InputStream is) throws XmlRpcFault{
    	parse( new BufferedInputStream( is ) );
        
        if ( isFaultResponse ){
            XmlRpcStruct fault = ( XmlRpcStruct ) returnValue;
            isFaultResponse = false;
            
            throw new XmlRpcFault( fault.getInteger( "faultCode" ),
                                   fault.getString( "faultString" ) );
        }
    }    
     
    @Override
    public void startElement(String uri,String name,String qualifiedName,Attributes attributes )throws SAXException{
        if ( name.equals( "fault" ) ){
            isFaultResponse = true;
        }
        else{
            super.startElement( uri, name, qualifiedName, attributes );
        }
    }
    
    @Override
    protected void handleParsedValue(Object obj) {
    	returnValue = obj;
    }

}
