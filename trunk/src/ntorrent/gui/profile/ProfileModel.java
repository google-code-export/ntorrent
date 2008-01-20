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
package ntorrent.gui.profile;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.AbstractListModel;

import ntorrent.io.tools.Serializer;

public class ProfileModel extends AbstractListModel implements Serializable,Iterable<ClientProfile>{
	private static final long serialVersionUID = 1L;
	Vector<ClientProfile> list = new Vector<ClientProfile>();

	
	public ClientProfile getElementAt(int index) {
		return list.elementAt(index);
	}

	public int getSize() {
		return list.size();
	}

	public void addListData(ClientProfile[] data) {
		for(ClientProfile p : data)
			addElement(p);
	}

	public void remove(int index) {
		list.removeElementAt(index);
		fireIntervalRemoved(this, index, index);
	}

	public void addElement(ClientProfile profile) {
		list.addElement(profile);
		int index = list.indexOf(profile);
		fireIntervalAdded(this,index,index);
	}
	
	public static ProfileModel deserialize() throws IOException, ClassNotFoundException{
		return (ProfileModel)Serializer.deserialize(ProfileModel.class);
	}

	public Iterator<ClientProfile> iterator() {
		return list.iterator();
	}
	

}
