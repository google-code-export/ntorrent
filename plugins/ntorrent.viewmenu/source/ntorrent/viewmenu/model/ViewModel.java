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
package ntorrent.viewmenu.model;

import ntorrent.locale.ResourcePool;

public class ViewModel implements View {

	final private String id;
	final private String view;
	
	public ViewModel(String id,String view) {
		this.id = id;
		this.view = view;
	}
	
	public String getId() {
		return id;
	}

	public String getView() {
		return view;
	}
	
	@Override
	public String toString() {
		return ResourcePool.getString(id,"locale",this);
	}

}
