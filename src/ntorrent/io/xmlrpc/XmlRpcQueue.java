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

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import ntorrent.io.xmlrpc.model.RpcRequest;
import ntorrent.settings.LocalSettings;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class XmlRpcQueue extends XmlRpcClient implements Runnable {
	
	private Queue<RpcRequest> queue = new ArrayBlockingQueue<RpcRequest>(32);
	private Thread thisThread;
	private XmlRpcClientConfigImpl config;
	private static long time = 0;
	
	
	public XmlRpcQueue(XmlRpcClientConfigImpl c) {
		thisThread = new Thread(this);
		thisThread.setPriority(Thread.MAX_PRIORITY);
		thisThread.setDaemon(true);
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
	
	public static long lag(){
		return 	(System.currentTimeMillis()-time)/1000;
	}
	
	private RpcRequest makeRequest(String pMethodName, Object[] pParams, XmlRpcCallback pCallback) {
		if(pParams == null)
			pParams = new Object[0];
		if(pCallback == null)
			pCallback = new XmlRpcCallback(){

				@Override
				public void handleResult(XmlRpcRequest pRequest, Object pResult) {
					// TODO Auto-generated method stub
					
				}};
		return new RpcRequest(pMethodName, pParams,config, pCallback);
	}
	
	public void addToExecutionQueue(String pMethodName, Object[] pParams, XmlRpcCallback pCallback) {		
		addToExecutionQueue(makeRequest(pMethodName, pParams, pCallback));
	}
	
	public void addToExecutionQueue(RpcRequest req) {
		if(!queue.contains(req)){
				queue.add(req);
			if(!thisThread.isInterrupted())
				thisThread.interrupt();
		}else{
			if(LocalSettings.debug)
				System.out.println("already in queue: "+req);
		}
	}

		
	private void executeQueue(){
		while(!queue.isEmpty()){
			final RpcRequest req = queue.poll();
			
			if(req != null)
				try {
					final Object result = super.execute(req);
					if(LocalSettings.debug)
						System.out.println(req);
					Thread handle = new Thread(){
						public void run() {
							// TODO Auto-generated method stub
							req.getCallBack().handleResult(req,result);
						}
						
					};
					handle.start();
					time = System.currentTimeMillis();
				} catch (XmlRpcException e) {
					req.getCallBack().handleError(req, e);
				}
		}
	}
	
}
