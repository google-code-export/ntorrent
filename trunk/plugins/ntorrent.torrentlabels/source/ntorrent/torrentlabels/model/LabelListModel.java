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
package ntorrent.torrentlabels.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import ntorrent.env.Environment;
import ntorrent.torrentlabels.LabelController;

/**
 * @author Kim Eik
 *
 */
public class LabelListModel implements ListModel,Map<String,JMenuItem> {
	private static final long serialVersionUID = 1L;
	Vector<ListDataListener> listDataListeners = new Vector<ListDataListener>();
	Vector<String> keys = new Vector<String>();
	Vector<JMenuItem> data = new Vector<JMenuItem>();
	
	public LabelListModel() {
		String[] entries = new String[] {
				"torrentlabel.all",
				"torrentlabel.none",
				};
		
		for(String s : entries){
			put(Environment.getString(s), null);
		}
	}
	
	//list methods
	public void addListDataListener(ListDataListener l) {
		listDataListeners.add(l);
	}
	public String getElementAt(int index) {
		return keys.get(index);
	}
	public int getSize() {
		return keys.size();
	}
	public void removeListDataListener(ListDataListener l) {
		listDataListeners.remove(l);
	}
	//*listmethods
	
	//map methods
	public void clear() {
		data.clear();
		keys.clear();
	}
	public boolean containsKey(Object key) {
		return keys.contains(key);
	}
	public boolean containsValue(Object value) {
		return data.contains(value);
	}
	public Set entrySet() {
		return null;
	}
	public boolean isEmpty() {
		return keys.isEmpty();
	}
	public Set<String> keySet() {
		return new HashSet<String>(keys);
	}

	public JMenuItem remove(Object key) {
		int index = keys.indexOf(key);
		keys.remove(key);
		return data.remove(index);
	}
	public int size() {
		return keys.size();
	}
	public Collection<JMenuItem> values() {
		return data;
	}
	public JMenuItem put(String key, JMenuItem value) {
		JMenuItem item = null;
		if(containsKey(key))
			item = remove(key);
		keys.add(key);
		data.add(value);
		return item;
	}
	public void putAll(Map<? extends String, ? extends JMenuItem> m) {
		for(Entry entry : m.entrySet()){
			Entry<String,JMenuItem> e = entry; 
			put(e.getKey(), e.getValue());
		}
		
	}
	public JMenuItem get(Object key) {
		return data.get(keys.indexOf(key));
	}
	
	//*map methods

	public void fireIntervallAdded(Object src, int startRow, int endRow){
		for(ListDataListener l : listDataListeners){
			l.intervalAdded(new ListDataEvent(src,ListDataEvent.INTERVAL_ADDED,startRow,endRow));
		}
	}
	
	public void fireIntervallRemoved(Object src, int startRow, int endRow){
		for(ListDataListener l : listDataListeners){
			l.intervalAdded(new ListDataEvent(src,ListDataEvent.INTERVAL_REMOVED,startRow,endRow));
		}
	}

	public void fireIntervallChanged(Object src, int startRow, int endRow) {
		for(ListDataListener l : listDataListeners){
			l.intervalAdded(new ListDataEvent(src,ListDataEvent.CONTENTS_CHANGED,startRow,endRow));
		}
	}
}
