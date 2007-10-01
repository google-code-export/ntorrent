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

/**
 * @author Kim Eik
 *
 */
public class TrackerInfo {
	long group;
	long minInterval;
	long normalInterval;
	long scrapeComplete;
	long scrapeDownloaded;
	long scrapeIncomplete;
	long scrapeTimeLast;
	long type;
	String hash;
	String url;
	String id;
	boolean enabled;
	boolean open;
	
	public TrackerInfo(String url, String hash) { 
		this.url = url;
		this.hash = hash;
	}
	
	public void setId(String id) { this.id = id; }
	public void setOpen(boolean open) {	this.open = open;}
	public void setEnabled(boolean enabled) {this.enabled = enabled;}
	public void setScrapeTimeLast(long scrapeTimeLast) {this.scrapeTimeLast = scrapeTimeLast;}
	public void setScrapeComplete(long scrapeComplete) {this.scrapeComplete = scrapeComplete;}
	public void setType(long type) {this.type = type;}
	public void setScrapeIncomplete(long scrapeIncomplete) {this.scrapeIncomplete = scrapeIncomplete;}
	public void setGroup(long group) {this.group = group;}
	public void setMinInterval(long minInterval) {this.minInterval = minInterval;}
	public void setNormalInterval(long normalInterval) {this.normalInterval = normalInterval;}
	public void setScrapeDownloaded(long scrapeDownloaded) {this.scrapeDownloaded = scrapeDownloaded;}
	
	public String getId() {
		return id;
	}
	
	public long getScrapeTimeLast() {
		return scrapeTimeLast;
	}
	
	public long getScrapeComplete() {
		return scrapeComplete;
	}
	
	public long getGroup() {
		return group;
	}
	
	public long getMinInterval() {
		return minInterval;
	}
	
	public long getNormalInterval() {
		return normalInterval;
	}
	
	public long getScrapeDownloaded() {
		return scrapeDownloaded;
	}
	
	public long getScrapeIncomplete() {
		return scrapeIncomplete;
	}

	public long getType() {
		return type;
	}
	
	public String getUrl() {
		return url;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public String getHash() {
		return hash;
	}
	
	@Override
	public String toString() {
		return getUrl();
	}
}
