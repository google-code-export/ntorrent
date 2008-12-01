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
package ntorrent.mvc;

import java.beans.PropertyChangeEvent;
import java.util.Vector;

public abstract class AbstractModel implements Model {	
	Vector<ModelChangeListener> listeners = new Vector<ModelChangeListener>();

	public void addPropertyChangeListener(ModelChangeListener l) {
		listeners.add(l);
	}

	public synchronized void firePropertyChangeEvent(Object source, String name, Object oldVal, Object newVal) {
		for(ModelChangeListener p : listeners)
			p.propertyChange(new PropertyChangeEvent(source,name,oldVal,newVal));
	}
	
}
