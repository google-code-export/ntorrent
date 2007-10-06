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
package org.heldig.ntorrent.gui.file;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.xmlrpc.XmlRpcRequest;
import org.heldig.ntorrent.Controller;
import org.heldig.ntorrent.gui.render.TrackerUrlRenderer;
import org.heldig.ntorrent.model.TrackerInfo;
import org.heldig.ntorrent.model.TrackerJTableModel;
import org.heldig.ntorrent.xmlrpc.XmlRpcCallback;


/**
 * @author Kim Eik
 *
 */
public class TrackerList extends XmlRpcCallback {
	TrackerJTableModel model = new TrackerJTableModel();
	JTable trackerlist = new JTable(model);
	JScrollPane pane = new JScrollPane(trackerlist);
	private String hash;
	
	public TrackerList(Controller c) {
		trackerlist.setBackground(Color.white);
		trackerlist.setDefaultRenderer(TrackerInfo.class, new TrackerUrlRenderer());
		trackerlist.addMouseListener(new TrackerListener(c,this));
	}
	
	public JScrollPane getTrackerlist() {
		return pane;
	}

	@Override
	public void handleResult(XmlRpcRequest pRequest, Object pResult) {
		Object[] result = (Object[])pResult;
		TrackerInfo[] t = new TrackerInfo[result.length];
		for(int x = 0; x < result.length; x++){
			Object[] tracker = (Object[])result[x];
			t[x] = new TrackerInfo((String)tracker[0],"");
			t[x].setId((String)tracker[1]);
			t[x].setGroup((Long)tracker[2]);
			t[x].setMinInterval((Long)tracker[3]);
			t[x].setNormalInterval((Long)tracker[4]);
			t[x].setScrapeComplete((Long)tracker[5]);
			t[x].setScrapeDownloaded((Long)tracker[6]);
			t[x].setScrapeIncomplete((Long)tracker[7]);
			t[x].setScrapeTimeLast((Long)tracker[8]);
			t[x].setType((Long)tracker[9]);
			t[x].setEnabled(((Long)tracker[10]) == 1);
			t[x].setOpen(((Long)tracker[11]) == 1);
		}
	model.setData(t);
	model.fireTableDataChanged();
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
	
	public String getHash() {
		return hash;
	}
	
}
