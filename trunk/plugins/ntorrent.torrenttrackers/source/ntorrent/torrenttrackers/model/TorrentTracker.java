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
package ntorrent.torrenttrackers.model;

/**
 * @author Kim Eik
 *
 */
public class TorrentTracker {
		
	private final String url;
	
	private int group;
	private String id;
	private int minIntervall;
	private int normalIntervall;
	private int scrapeComplete;
	private int scrapeDownloaded;
	private int scrapeIncomplete;
	private int scrapeTimeLast;
	
	private boolean enabled;
	private boolean open;
	
	public TorrentTracker(String url) {
		this.url = url;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMinIntervall() {
		return minIntervall;
	}

	public void setMinIntervall(int minIntervall) {
		this.minIntervall = minIntervall;
	}

	public int getNormalIntervall() {
		return normalIntervall;
	}

	public void setNormalIntervall(int normalIntervall) {
		this.normalIntervall = normalIntervall;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public int getScrapeComplete() {
		return scrapeComplete;
	}

	public void setScrapeComplete(int scrapeComplete) {
		this.scrapeComplete = scrapeComplete;
	}

	public int getScrapeDownloaded() {
		return scrapeDownloaded;
	}

	public void setScrapeDownloaded(int scrapeDownloaded) {
		this.scrapeDownloaded = scrapeDownloaded;
	}

	public int getScrapeIncomplete() {
		return scrapeIncomplete;
	}

	public void setScrapeIncomplete(int scrapeIncomplete) {
		this.scrapeIncomplete = scrapeIncomplete;
	}

	public int getScrapeTimeLast() {
		return scrapeTimeLast;
	}

	public void setScrapeTimeLast(int scrapeTimeLast) {
		this.scrapeTimeLast = scrapeTimeLast;
	}

	public String getUrl() {
		return url;
	}
	
	@Override
	public String toString() {
		return getUrl();
	}
	
}
