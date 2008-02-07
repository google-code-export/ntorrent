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


import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


import ntorrent.env.Environment;
import ntorrent.tools.Serializer;

public class ClientProfileListModel extends Vector<ClientProfileInterface> implements ListModel, Serializable  {
	private static final long serialVersionUID = 1L;
	
	private transient Vector<ListDataListener> listener = new Vector<ListDataListener>();
	
	@SuppressWarnings("unchecked")
	public ClientProfileListModel(){
		try {
			ClientProfileListModel list = (ClientProfileListModel)Serializer.deserialize(this.getClass(),Environment.getNtorrentDir());
			addAll(list);
		} catch (Exception e) {
			Logger.global.log(Level.WARNING,e.getMessage(),e);
		}
	}
		
	public boolean add(ClientProfileInterface o){
		boolean ret = super.add(o);
		Serialize();
		for(ListDataListener l : listener)
			l.intervalAdded(new ListDataEvent(this,ListDataEvent.INTERVAL_ADDED,getSize(),getSize()));
		return ret; 
	}

	public boolean remove(ClientProfileInterface o){
		boolean ret = super.remove(o);
		Serialize();
		for(ListDataListener l : listener)
			l.intervalRemoved(new ListDataEvent(this,ListDataEvent.INTERVAL_REMOVED,getSize(),getSize()));
		return ret; 
	}
	
	public void Serialize(){
		try {
			Serializer.serialize(this,Environment.getNtorrentDir());
		} catch (IOException e) {
			Logger.global.log(Level.WARNING,e.getMessage(),e);
		}
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
	
	public static ClientProfileListModel Deserialize() throws IOException, ClassNotFoundException{
		return (ClientProfileListModel) Serializer.deserialize(ClientProfileListModel.class, Environment.getNtorrentDir());
	}
}
