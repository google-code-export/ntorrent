/**
 * 
 */
package org.heldig.ntorrent.io.xmlrpc;

import java.util.Vector;

import org.apache.xmlrpc.XmlRpcRequest;

/**
 * @author Kim Eik
 *
 */
public class XmlRpcCallbackList extends Vector<XmlRpcCallback> implements XmlRpcCallback{
	private static final long serialVersionUID = 1L;

	
	public void handleResult(XmlRpcRequest request, Object result) {
		for(XmlRpcCallback c : this)
			c.handleResult(request, result);
	}

	
}
