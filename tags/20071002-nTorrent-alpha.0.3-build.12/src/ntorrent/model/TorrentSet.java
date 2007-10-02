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

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

public class TorrentSet extends Vector<TorrentInfo>{
	private static final long serialVersionUID = 1L;
	HashMap<String,TorrentInfo> map = new HashMap<String,TorrentInfo>();
	
	public boolean add(TorrentInfo e) {
		if(!contains(e)){
			map.put(e.getHash(),e);
			return super.add(e);
		}
		return false;
	}

	public boolean addAll(Collection<? extends TorrentInfo> c) {
		for(TorrentInfo e : c)
			if(!add(e))
				return false;
		return true;
	}
	
	public boolean contains(String hash) {
		return map.containsKey(hash);
	}

	public TorrentInfo get(String hash) {
		return map.get(hash);
	}
	
	public void remove(String hash){
		remove(map.get(hash));
		map.remove(hash);
	}
	
	public int Indexof(String hash){
		return indexOf(map.get(hash));
	}
	
	public Set<String> getHashSet(){
		return map.keySet();
	}
	
	
	@Override
	public void clear() {
		map.clear();
	}

}
