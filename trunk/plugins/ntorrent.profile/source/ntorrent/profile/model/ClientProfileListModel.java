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
package ntorrent.profile.model;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.apache.log4j.Logger;

import ntorrent.data.Environment;
import ntorrent.tools.Serializer;

public class ClientProfileListModel extends Vector<ClientProfileInterface> implements ListModel, Serializable  {
	private static final long serialVersionUID = 1L;
	private static final File dataDir = Environment.getNtorrentDir();
	private transient Vector<ListDataListener> listener = new Vector<ListDataListener>();
	
	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(ClientProfileListModel.class);
	@SuppressWarnings("unchecked")
	public ClientProfileListModel(){
		try {
			ClientProfileListModel list = Serializer.deserialize(ClientProfileListModel.class,dataDir);
			addAll(list);
		} catch (Exception e) {
			log.warn(e.getMessage(),e);
		}
	}
		
	public boolean add(ClientProfileInterface o){
		boolean ret = super.add(o);
		try {
			Serializer.serialize(this, dataDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(ListDataListener l : listener)
			l.intervalAdded(new ListDataEvent(this,ListDataEvent.INTERVAL_ADDED,getSize(),getSize()));
		return ret; 
	}

	public boolean remove(ClientProfileInterface o){
		boolean ret = super.remove(o);
		try {
			Serializer.serialize(this, dataDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(ListDataListener l : listener)
			l.intervalRemoved(new ListDataEvent(this,ListDataEvent.INTERVAL_REMOVED,getSize(),getSize()));
		return ret; 
	}

	public void addListDataListener(ListDataListener l) {
		listener.add(l);
	}

	public Object getElementAt(int index) {
		return this.get(index);
	}

	public int getSize() {
		return size();
	}

	public void removeListDataListener(ListDataListener l) {
		listener.remove(l);
	}
}
