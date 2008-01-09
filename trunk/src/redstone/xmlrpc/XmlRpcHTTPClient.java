/*
    Copyright (c) 2007 Redstone Handelsbolag

    This library is free software; you can redistribute it and/or modify it under the terms
    of the GNU Lesser General Public License as published by the Free Software Foundation;
    either version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
    without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License along with this
    library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
    Boston, MA  02111-1307  USA
*/

package redstone.xmlrpc;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *  An XmlRpcClient represents a connection to an XML-RPC enabled server. It
 *  implements the XmlRpcInvocationHandler so that it may be used as a relay
 *  to other XML-RPC servers when installed in an XmlRpcServer. 
 *
 *  @author Greger Olsson
 */

public class XmlRpcHTTPClient extends XmlRpcClient 
{
	
    /** The server URL. */
    private URL url;
	
	/** Connection to the server. */
	protected HttpURLConnection connection;
	
    /** Indicates wheter or not we shall stream the message directly or build them locally? */
    protected boolean streamMessages;
    
    /** HTTP request properties, or null if none have been set by the application. */
    protected Map requestProperties;    
	
    /**
     *  Creates a new client with the ability to send XML-RPC messages
     *  to the the server at the given URL.
     *
     *  @param url the URL at which the XML-RPC service is locaed
     * 
     *  @param streamMessages Indicates whether or not to stream messages directly
     *                        or if the messages should be completed locally
     *                        before being sent all at once. Streaming is not
     *                        directly supported by XML-RPC, since the
     *                        Content-Length header is not included in the HTTP post. 
     *                        If the other end is not relying on Content-Length,
     *                        streaming the message directly is much more efficient.
     * @throws MalformedURLException 
     */

    public XmlRpcHTTPClient( String url, boolean streamMessages ) throws MalformedURLException
    {
        this( new URL( url ), streamMessages );
    }
    
    
    /**
     *  @see XmlRpcClient(String,boolean)
     */

    public XmlRpcHTTPClient( URL url, boolean streamMessages )
    {
        this.url = url;
        this.streamMessages = streamMessages;
        
        if ( !streamMessages )
        {
            writer = new StringWriter( 2048 );
        }
    }


    /**
     *  Sets the HTTP request properties that the client will use for the next invocation,
     *  and any invocations that follow until setRequestProperties() is invoked again. Null
     *  is accepted and means that no special HTTP request properties will be used in any
     *  future XML-RPC invocations using this XmlRpcClient instance.
     *
     *  @param requestProperties The HTTP request properties to use for future invocations
     *                           made using this XmlRpcClient instance. These will replace
     *                           any previous properties set using this method or the
     *                           setRequestProperty() method.
     */

    public void setRequestProperties( Map requestProperties )
    {
        this.requestProperties = requestProperties;
    }
    

    /**
     *  Sets a single HTTP request property to be used in future invocations.
     *  @see setRequestProperties()
     *
     *  @param name Name of the property to set
     *  @param value The value of the property
     */

    public void setRequestProperty( String name, String value )
    {
        if ( requestProperties == null )
        {
            requestProperties = new HashMap();
        }
        
        requestProperties.put( name, value );
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
            if ( streamMessages )
            {
                openConnection();
                writer = new BufferedWriter(
                    new OutputStreamWriter(
                        connection.getOutputStream(),
                        XmlRpcMessages.getString( "XmlRpcClient.Encoding" ) ) );
            }
            else
            {
                ( ( StringWriter ) writer ).getBuffer().setLength( 0 );
            }
            
            super.beginCall(methodName);
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
     */

    protected void endCall() throws XmlRpcException, XmlRpcFault
    {
        try
        {
            writer.write( "</params>" );
            writer.write( "</methodCall>" );

            if ( streamMessages )
            {
                writer.flush();
            }
            else
            {
                StringBuffer buffer = ( ( StringWriter ) writer ).getBuffer();

                openConnection();
                connection.setRequestProperty( "Content-Length", String.valueOf( buffer.length() ) );

                OutputStream output = new BufferedOutputStream( connection.getOutputStream() );
                output.write( buffer.toString().getBytes() );
                output.flush();
                output.close();
            }
            
            handleResponse(connection.getInputStream());
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
            
            connection.disconnect();
            connection = null;
        }
        
    }


    /**
     *  Opens a connection to the URL associated with the client instance. Any
     *  HTTP request properties set using setRequestProperties() are recorded
     *  with the internal HttpURLConnection and are used in the HTTP request.
     * 
     *  @throws IOException If a connection could not be opened. The exception
     *                      is propagated out of any unsuccessful calls made into
     *                      the internal java.net.HttpURLConnection.
     */

    private void openConnection() throws IOException
    {
        connection = ( HttpURLConnection ) url.openConnection();
        connection.setDoInput( true );
        connection.setDoOutput( true );
        connection.setRequestMethod( "POST" );
        connection.setRequestProperty(
            "Content-Type", "text/xml; charset=" +
            XmlRpcMessages.getString( "XmlRpcClient.Encoding" ) );
        
        if ( requestProperties != null )
        {
            for ( Iterator propertyNames = requestProperties.keySet().iterator();
                  propertyNames.hasNext(); )
            {
                String propertyName = ( String ) propertyNames.next();
                
                connection.setRequestProperty(
                    propertyName,
                    ( String ) requestProperties.get( propertyName ) );
            }
        }
    }
}