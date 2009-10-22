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
package ntorrent.torrentfiles.model;

import javax.swing.tree.DefaultMutableTreeNode;

import ntorrent.torrenttable.model.Byte;
import ntorrent.torrenttable.model.Percent;
import ntorrent.torrenttable.model.Priority;

/**
 * @author Kim Eik
 *
 */
public class TorrentFile extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 1L;

	final private String name;
	final private String parent;
	
	private int offset;
	private Priority priority;
	private Percent percent;
	private Byte size;
	private Boolean created;
	private Boolean open;
	private String lastTouched;
	
	public TorrentFile(String filename){
		this(filename,null);
	}
	
	public TorrentFile(String filename, String parent) {
		this.parent = parent; //info_hash of the parent torrent.
		this.name = filename;
	}

	public Percent getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = new Percent(percent);
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(long priority) {
		this.priority = new Priority(priority);
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public Boolean isCreated() {
		return created;
	}

	public void setCreated(Boolean created) {
		this.created = created;
	}

	public String getLastTouched() {
		return lastTouched;
	}

	public void setLastTouched(String lastTouched) {
		this.lastTouched = lastTouched;
	}

	public Boolean isOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public Byte getSize() {
		return size;
	}
	
	public String getParentHash() {
		return parent;
	}
	
	public int getOffset(){
		return offset;
	}

	public void setSize(long size) {
		this.size = new Byte(size);
	}

	public void setOffset(int i) {
		offset = i;
	}

	public TorrentFile contains(String name){
		TorrentFile child = null;
		for(int x = 0; x < getChildCount(); x++){
			TorrentFile c = (TorrentFile)getChildAt(x);
			if(c.name.equals(name)){
				child = c;
				break;
			}
		}
		return child;
	}
	
}
