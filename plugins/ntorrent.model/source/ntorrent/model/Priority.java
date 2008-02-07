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
package ntorrent.model;


public class Priority {
	Integer pri;
	
	public Priority(long p){
		this(p,true);
	}
	
	public Priority(long p, boolean torrent){
		pri = (int)p;
		
		if(!torrent && pri == 2)
			pri++;
	}
	
	public String toString() {
		switch(pri){
			case 0: return "off";
			case 1: return "low";
			case 2: return "norm";
			case 3: return "high";
			default: return "wuff";
		}
	}
}
