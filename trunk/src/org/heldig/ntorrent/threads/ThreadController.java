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
package org.heldig.ntorrent.threads;

import org.heldig.ntorrent.Controller;

/**
 * @author  Kim Eik
 */
public class ThreadController{
	private Thread mainContentThread;
	Controller parent;
	private ThrottleThread throttleThread;
	public ThreadController(Controller c) {
		parent = c;
		mainContentThread = new ContentThread(
				c.GC, 
				c.IO.getRpc(), 
				c.GC.getStatusBar(),
				c.MC.getTorrentPool());
		throttleThread = new ThrottleThread(c.IO.getRpc(),c.GC.getStatusBar());
	}
	
	public void startMainContentThread(){
		System.out.println("Starting content thread.");
		mainContentThread.start();
	}
	
	public void startThrottleThread(){
		System.out.println("Starting throttle thread.");
		throttleThread.start();
	}
	
	/**
	 * @return
	 */
	public Thread getMainContentThread() {
		return mainContentThread;
	}
	
	public ThrottleThread getThrottleThread() {
		return throttleThread;
	}
	
}