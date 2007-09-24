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
package ntorrent.io.xmlrpc;

import java.util.LinkedList;
import java.util.Queue;

import ntorrent.io.xmlrpc.model.RpcRequest;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class RpcQueue extends XmlRpcClient implements Runnable {
	
	private Queue<RpcRequest> queue = new LinkedList<RpcRequest>();
	private Thread thisThread;
	private XmlRpcClientConfigImpl config;
	
	
	public RpcQueue(XmlRpcClientConfigImpl c) {
		thisThread = new Thread(this);
		thisThread.start();
		config = c;
	}
	
	public void run() {
		while(true){
			try {
				thisThread.join();
			} catch (InterruptedException x) {
				executeQueue();
			}
		}
	}
	
	public void executeAsync(String pMethodName, Object[] pParams, RpcCallback pCallback) {
		queue.add(new RpcRequest(pMethodName, pParams,config, pCallback));
		if(!thisThread.isInterrupted())
			thisThread.interrupt();
	}
	
	private void executeQueue(){
		while(!queue.isEmpty()){
			RpcRequest req = queue.poll();
			System.out.println(req+" "+queue.size());
			try {
				super.executeAsync(req, req.getCallBack());
			} catch (XmlRpcException e) {
				req.getCallBack().handleError(req, e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
