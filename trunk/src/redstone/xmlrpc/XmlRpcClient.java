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
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract class XmlRpcClient extends XmlRpcParser implements XmlRpcInvocationHandler {
	
	/** Connection to the server. */
	//protected ConnectionInterface connection;
	
    /** The parsed value returned in a response. */
    protected Object returnValue;

    /** Writer to which XML-RPC messages are serialized. */
    protected Writer writer;
    
    /** Indicates whether or not the incoming response is a fault response. */
    protected boolean isFaultResponse;
    
    /** The serializer used to serialize arguments. */
    protected XmlRpcSerializer serializer = new XmlRpcSerializer();
    
    /**
     *  Stores away the one and only value contained in XML-RPC responses.
     *
     *  @param value The contained return value.
     */
    protected void handleParsedValue( Object value ){
        returnValue = value;
    }
    
    /**
     *  Invokes a method on the terminating XML-RPC end point. The supplied method name and
     *  argument collection is used to encode the call into an XML-RPC compatible message.
     *
     *  @param method The name of the method to call.
     *
     *  @param arguments The arguments to encode in the call.
     *
     *  @return The object returned from the terminating XML-RPC end point.
     *
     *  @throws XmlRpcException One or more of the supplied arguments are unserializable. That is,
     *                          the built-in serializer connot parse it or find a custom serializer
     *                          that can. There may also be problems with the socket communication.
     * @throws  
     */

    public synchronized Object invoke(
        String method,
        List arguments )
        throws XmlRpcException, XmlRpcFault
    {
        beginCall( method );

        if ( arguments != null )
        {
            Iterator argIter = arguments.iterator();

            while ( argIter.hasNext() )
            {
                try
                {
                    writer.write( "<param>" );
                    serializer.serialize( argIter.next(), writer );
                    writer.write( "</param>" );
                }
                catch ( IOException ioe )
                {
                    throw new XmlRpcException(
                        XmlRpcMessages.getString( "XmlRpcClient.NetworkError" ), ioe );
                }
            }
        }

        endCall();
        return returnValue;
    }
    
    /**
     *  Invokes a method on the terminating XML-RPC end point. The supplied method name and
     *  argument vector is used to encode the call into XML-RPC.
     *
     *  @param method The name of the method to call.
     *
     *  @param arguments The arguments to encode in the call.
     *
     *  @return The object returned from the terminating XML-RPC end point.
     *
     *  @throws XmlRpcException One or more of the supplied arguments are unserializable. That is,
     *                          the built-in serializer connot parse it or find a custom serializer
     *                          that can. There may also be problems with the socket communication.
     */

    public synchronized Object invoke(
        String method,
        Object[] arguments )
        throws XmlRpcException, XmlRpcFault
    {
        beginCall( method );

        if ( arguments != null )
        {
            for ( int i = 0; i < arguments.length; ++i )
            {
                try
                {
                    writer.write( "<param>" );
                    serializer.serialize( arguments[ i ], writer );
                    writer.write( "</param>" );
                }
                catch ( IOException ioe )
                {
                    throw new XmlRpcException(
                        XmlRpcMessages.getString( "XmlRpcClient.NetworkError" ), ioe );
                }
            }
        }
        
        endCall();
        
        return returnValue;
    }
    
    /**
     *  A asynchronous version of invoke performing the call in a separate thread and
     *  reporting responses, faults, and exceptions through the supplied XmlRpcCallback.
     *  TODO Determine on proper strategy for instantiating Threads.
     *
     *  @param method The name of the method at the server.
     *
     *  @param arguments The arguments for the call. This may be either a java.util.List
     *                   descendant, or a java.lang.Object[] array.
     *
     *  @param callback An object implementing the XmlRpcCallback interface. If callback is
     *                  null, the call will be performed but any results, faults, or exceptions
     *                  will be ignored (fire and forget).
     */

    public void invokeAsynchronously(
        final String method,
        final Object arguments,
        final XmlRpcCallback callback )
    {
        if ( callback == null )
        {
            new Thread()
            {
                public void run()
                {
                    try // Just fire and forget.
                    {
                        if ( arguments instanceof Object[] )
                            invoke( method, ( Object[] ) arguments );
                        else
                            invoke( method, ( List ) arguments );
                    }
                    catch ( XmlRpcFault e ) { /* Ignore, no callback. */ }
                    catch ( XmlRpcException e ) { /* Ignore, no callback. */ }
                }
            }.start();
        }
        else
        {
            new Thread()
            {
                public void run()
                {
                    Object result = null;

                    try
                    {
                        if ( arguments instanceof Object[] )
                            result = invoke( method, ( Object[] ) arguments );
                        else
                            result = invoke( method, ( List ) arguments );

                        callback.onResult( result );
                    }
                    catch ( XmlRpcException e )
                    {
                        callback.onException( e );
                    }
                    catch ( XmlRpcFault e )
                    {
                        XmlRpcStruct fault = ( XmlRpcStruct ) result;
    
                        callback.onFault( fault.getInteger( "faultCode" ),
                                          fault.getString( "faultString" ) );
                    }                    
                }
            }.start();
        }
    }
    
    /**
     *  Initializes the XML buffer to be sent to the server with the XML-RPC
     *  content common to all method calls, or serializes it directly over the writer
     *  if streaming is used. The parameters to the call are added in the execute()
     *  method, and the closing tags are appended when the call is finalized in endCall().
     *
     *  @param methodName The name of the method to call.
     */

    protected void beginCall( String methodName ) throws XmlRpcException
    {
        try
        {
            writer.write( "<?xml version=\"1.0\" encoding=\"" );
            writer.write( XmlRpcMessages.getString( "XmlRpcClient.Encoding" ) );
            writer.write( "\"?>" );
            writer.write( "<methodCall><methodName>" );
            writer.write( methodName );
            writer.write( "</methodName><params>" );
        }
        catch( IOException ioe )
        {
            throw new XmlRpcException(
                XmlRpcMessages.getString( "XmlRpcClient.NetworkError" ), ioe );
        }
    }
    
    /**
     *  Finalizaes the XML buffer to be sent to the server, and creates a HTTP buffer for
     *  the call. Both buffers are combined into an XML-RPC message that is sent over
     *  a socket to the server.
     *
     *  @return The parsed return value of the call.
     *
     *  @throws XmlRpcException when some IO problem occur. 
     *  
     */

    protected void endCall() throws XmlRpcException, XmlRpcFault
    {
        try
        {
            writer.write( "</params>" );
            writer.write( "</methodCall>" );
            
        }
        catch ( IOException ioe )
        {
            throw new XmlRpcException(
                XmlRpcMessages.getString( "XmlRpcClient.NetworkError" ),
                    ioe );
        }
        finally
        {
            try
            {
                writer.close();
            }
            catch( IOException ignore ) { /* Closed or not, we don't care at this point. */ }
        }
    }
    
    /**
     *  Handles the response returned by the XML-RPC server. If the server responds with a
     *  "non-200"-HTTP response or if the XML payload is unparseable, this is interpreted
     *  as an error in communication and will result in an XmlRpcException.<p>
     *
     *  If the user does not want the socket to be kept alive or if the server does not
     *  support keep-alive, the socket is closed.
     *
     *  @param inout The stream containing the server response to interpret.
     *
     *  @throws IOException If a socket error occurrs, or if the XML returned is unparseable.
     *                      This exception is currently also thrown if a HTTP response other
     *                      than "200 OK" is received.
     * @throws XmlRpcFault 
     */

    protected void handleResponse(InputStream is) throws XmlRpcFault
    {
        try
        {
            parse( new BufferedInputStream( is ) );
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
    
    /**
     *  Override the startElement() method inherited from XmlRpcParser. This way, we may set
     *  the error flag if we run into a fault-tag.
     *
     *  @param See SAX documentation
     */

    public void startElement(
        String uri,
        String name,
        String qualifiedName,
        Attributes attributes )
        throws SAXException
    {
        if ( name.equals( "fault" ) )
        {
            isFaultResponse = true;
        }
        else
        {
            super.startElement( uri, name, qualifiedName, attributes );
        }
    }

}
