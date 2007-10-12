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
package org.heldig.ntorrent.gui.filetab;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.xmlrpc.XmlRpcRequest;
import org.heldig.ntorrent.event.ControllerEventListener;
import org.heldig.ntorrent.gui.core.JTablePopupMenuImplementation;
import org.heldig.ntorrent.gui.render.TrackerUrlRenderer;
import org.heldig.ntorrent.io.xmlrpc.XmlRpcCallback;
import org.heldig.ntorrent.language.Language;


/**
 * @author Kim Eik
 *
 */
public class TrackerList extends JTablePopupMenuImplementation implements XmlRpcCallback {
	TrackerJTableModel model = new TrackerJTableModel();
	JTable trackerlist = new JTable(model);
	JScrollPane pane = new JScrollPane(trackerlist);
	private String hash;
	private ControllerEventListener event;
	
	final static Object[] menuItems = {
		Language.Tracker_List_Menu_disable,
		Language.Tracker_List_Menu_enable,
	};
	
	public TrackerList(ControllerEventListener e) {
		createMenuItems(popup,menuItems, this);
		event = e;
		trackerlist.setBackground(Color.white);
		trackerlist.setDefaultRenderer(TrackerInfo.class, new TrackerUrlRenderer());
		trackerlist.addMouseListener(this);
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
	
	@Override
	protected void maybeShowPopup(MouseEvent e) {
		selectedRows = ((JTable)e.getSource()).getSelectedRows();
		popup.show(e.getComponent(), e.getX(), e.getY());
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		switch(Language.getFromString(e.getActionCommand())){
			case Tracker_List_Menu_disable:
				event.setTrackerEnabled(hash, selectedRows, false, null);
				break;
			case Tracker_List_Menu_enable:
				event.setTrackerEnabled(hash, selectedRows, true, null);
				break;
		}
		
		//event.handleEvent(new Event(getHash(),selectedRows,e.getActionCommand()));
	}		
	
}
