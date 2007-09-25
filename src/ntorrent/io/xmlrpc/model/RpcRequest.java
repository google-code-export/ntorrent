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
package ntorrent.io.xmlrpc.model;

import ntorrent.io.xmlrpc.RpcCallback;

import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.XmlRpcRequestConfig;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class RpcRequest implements XmlRpcRequest {
	private String method;
	private Object[] params;
	private RpcCallback callBack;
	private XmlRpcClientConfigImpl config;
	
	public RpcRequest(String m, Object[] p,XmlRpcClientConfigImpl c) {
		this(m, p,c,null);
	}
	
	public RpcRequest(String m, Object[] p, XmlRpcClientConfigImpl c ,RpcCallback a){
		method = m;
		params = p;
		config = c;
		if(a == null){
			callBack = new RpcCallback(){

				@Override
				public void handleResult(XmlRpcRequest pRequest, Object pResult) {}
				
			};
		} else
			callBack = a;
	}
	
	public RpcCallback getCallBack() {
		return callBack;
	}
	
	public XmlRpcRequestConfig getConfig() {
		return config;
	}

	public String getMethodName() {
		return method;
	}

	public Object getParameter(int pIndex) {
		return params[pIndex];
	}

	public int getParameterCount() {
		return params.length;
	}
	
	@Override
	public String toString() {
		return method;
	}


}
