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

import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import ntorrent.io.settings.Serializer;

public class ProfileList extends JList {
	
	ClientProfile[] data = {};
	
	public ProfileList(ListSelectionListener l) {
		setLayout(new GridLayout(0,1));
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		try {
			data = (ClientProfile[])Serializer.deserialize(ClientProfile[].class);
		} catch(FileNotFoundException e){
			Logger.global.info(e.getMessage());
			try {
				Serializer.serialize(data);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setListData(data);
		addListSelectionListener(l);
	}

	public void deleteSelected() {
		remove(getSelectedIndex());
		try {
			Serializer.serialize(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
