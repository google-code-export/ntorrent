package org.heldig.ntorrent.io.xmlrpc;
/**
 *
 * $Id$
 *
 * Copyright Solomenchuk V. 2007.
 * Solomenchuk Vladimir <vovasty@gmail.com>
 *
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URL;
import java.util.StringTokenizer;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientException;
import org.apache.xmlrpc.client.XmlRpcHttpClientConfig;
import org.apache.xmlrpc.client.XmlRpcLiteHttpTransport;
import org.apache.xmlrpc.client.XmlRpcStreamTransport;
import org.apache.xmlrpc.common.XmlRpcStreamRequestConfig;
import org.apache.xmlrpc.util.HttpUtil;
import org.apache.xmlrpc.util.LimitedInputStream;
import org.xml.sax.SAXException;

public class XmlRpcSCGITransport extends XmlRpcStreamTransport {
	private OutputStream output;
	private InputStream input;
	private Socket socket;
	private String hostname;
	private int port;
	private XmlRpcHttpClientConfig config;
	
	protected XmlRpcSCGITransport(XmlRpcClient client) {
		super(client);
	}
	
	public Object sendRequest(XmlRpcRequest request) throws XmlRpcException {
		config = (XmlRpcHttpClientConfig) request.getConfig();
		URL url = config.getServerURL();
		hostname = url.getHost();
		port = url.getPort();
		return super.sendRequest(request);
	}

	
	
	protected void close() throws XmlRpcClientException {
		IOException e = null;
		if (input != null) {
			try {
				input.close();
			} catch (IOException ex) {
				e = ex;
			}
		}
		if (output != null) {
			try {
				output.close();
			} catch (IOException ex) {
				if (e != null) {
					e = ex;
				}
			}
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException ex) {
				if (e != null) {
					e = ex;
				}
			}
		}
		if (e != null) {
			throw new XmlRpcClientException("Failed to close connection: " + e.getMessage(), e);
		}
	}

	
	protected InputStream getInputStream() throws XmlRpcException {
		final byte[] buffer = new byte[2048];
		try {
            // If reply timeout specified, set the socket timeout accordingly
            if (config.getReplyTimeout() != 0)
                socket.setSoTimeout(config.getReplyTimeout());
            input = new BufferedInputStream(socket.getInputStream());
			// start reading  server response headers
			String line = HttpUtil.readLine(input, buffer);
			StringTokenizer tokens = new StringTokenizer(line);
			tokens.nextToken(); // Skip HTTP version
			String statusCode = tokens.nextToken();
			String statusMsg = tokens.nextToken("\n\r");
			if (!"200".equals(statusCode)) {
				throw new IOException("Unexpected Response from Server: "
									  + statusMsg);
			}
			int contentLength = -1;
			for (;;) {
				line = HttpUtil.readLine(input, buffer);
				if (line == null  ||  "".equals(line)) {
					break;
				}
				line = line.toLowerCase();
				if (line.startsWith("content-length:")) {
					contentLength = Integer.parseInt(line.substring("content-length:".length()).trim());
				}
			}
			InputStream result;
			if (contentLength == -1) {
				result = input;
			} else {
				result = new LimitedInputStream(input, contentLength);
			}
			return result;
		} catch (IOException e) {
			throw new XmlRpcClientException("Failed to read server response: " + e.getMessage(), e);
		}
	}

	
	protected boolean isResponseGzipCompressed(XmlRpcStreamRequestConfig config) {
		return false;
	}

	
	protected void writeRequest(ReqWriter writer) throws XmlRpcException,
			IOException, SAXException {
		ByteArrayOutputStream sOut=new ByteArrayOutputStream();
		writer.write(sOut);
		String message=Scgi.make(null, sOut.toString());
		sOut.close();
		OutputStream out=getOutputStream();
		out.write(message.getBytes());
		out.flush();
	}
	
	private OutputStream getOutputStream() throws XmlRpcException {
		try {
			final int retries = 3;
	        final int delayMillis = 100;
	
			for (int tries = 0;  ;  tries++) {
				try {
					socket = new Socket(hostname, port);
					output = new BufferedOutputStream(socket.getOutputStream()){
						/** Closing the output stream would close the whole socket, which we don't want,
						 * because the don't want until the request is processed completely.
						 * A close will later occur within
						 * {@link XmlRpcLiteHttpTransport#close()}.
						 */
						public void close() throws IOException {
							flush();
							socket.shutdownOutput();
						}
					};
					break;
				} catch (ConnectException e) {
					if (tries >= retries) {
						throw new XmlRpcException("Failed to connect to "
								+ hostname + ":" + port + ": " + e.getMessage(), e);
					} else {
	                    try {
	                        Thread.sleep(delayMillis);
	                    } catch (InterruptedException ignore) {
	                    }
					}
				}
			}
			return output;
		} catch (IOException e) {
			throw new XmlRpcException("Failed to open connection to "
					+ hostname + ":" + port + ": " + e.getMessage(), e);
		}
	}
	

}
