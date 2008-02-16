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
package ntorrent.torrenttable.model;

import java.util.HashMap;
import java.util.Map;

public class Torrent implements Comparable<Torrent> {

	private String hash,name,message;
	private Boolean started;
	private Byte completedBytes,upTotal,sizeBytes;
	private Long peersComplete,peersAccounted;
	private Bit downRate,upRate;
	private Priority priority;
	
	Map<String,Object> properties = new HashMap<String,Object>();
	
	public Torrent(String hash) {
		this.hash = hash;
	}
	
	public String getHash() {
		return hash;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Boolean getStarted() {
		return started;
	}
	
	public void setStarted(Boolean started) {
		this.started = started;
	}
	
	public Byte getCompletedBytes() {
		return completedBytes;
	}
	
	public void setCompletedBytes(long completedBytes) {
		setCompletedBytes(new Byte(completedBytes));
	}
	
	public void setCompletedBytes(Byte completedBytes) {
		this.completedBytes = completedBytes;
	}
	
	public Byte getUpTotal() {
		return upTotal;
	}
	
	public void setUpTotal(long upTotal) {
		setUpTotal(new Byte(upTotal));
	}
	
	public void setUpTotal(Byte upTotal) {
		this.upTotal = upTotal;
	}
	
	public Long getPeersComplete() {
		return peersComplete;
	}
	
	public void setPeersComplete(Long peersComplete) {
		this.peersComplete = peersComplete;
	}
	
	public Long getPeersAccounted() {
		return peersAccounted;
	}
	
	public void setPeersAccounted(Long peersConnected) {
		this.peersAccounted = peersConnected;
	}
	
	public Bit getDownRate() {
		return downRate;
	}
	
	public void setDownRate(long downRate) {
		setDownRate(new Bit(downRate));
	}
	
	public void setDownRate(Bit downRate) {
		this.downRate = downRate;
	}
	
	public Bit getUpRate() {
		return upRate;
	}
	
	public void setUpRate(long upRate) {
		setUpRate(new Bit(upRate));
	}
	
	public void setUpRate(Bit upRate) {
		this.upRate = upRate;
	}
	
	public Priority getPriority() {
		return priority;
	}
	
	public void setPriority(long priority) {
		setPriority(new Priority(priority));
	}
	
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
		
	public Byte getSizeBytes() {
		return sizeBytes;
	}
	
	public void setSizeBytes(long sizeBytes) {
		setSizeBytes(new Byte(sizeBytes));
	}
	
	public void setSizeBytes(Byte sizeBytes) {
		this.sizeBytes = sizeBytes;
	}
	
	public int getSeeders(){
		return getPeersComplete().intValue();
	}
	
	public int getLeechers(){
		return getPeersAccounted().intValue()-getSeeders();
	}
	
	public Eta getEta(){
		long done = getCompletedBytes().getValue();
		long complete = getSizeBytes().getValue();
		long speed = getDownRate().getValue();
		
		try {
			return new Eta((int)(((complete-done)/(speed))));
		} catch (ArithmeticException x){
			return new Eta(0);
		}
	}
	

	public Percent getPercentDone() {
        long down = getCompletedBytes().getValue();
        long total = getSizeBytes().getValue();
        return new Percent(((int)(down*100/total)));
	}
	
	public Ratio getRatio(){
        double down = getCompletedBytes().getValue();
        double up = getUpTotal().getValue();
        double ratio = 0.00;
        if(down > 0)
                ratio = up/down;
        return new Ratio(ratio);
	}
	
	/**
	 * Set an additional property of this torrent
	 * @param key
	 * @param property
	 */
	
	public void setProperty(String key, Object property) {
		properties.put(key, property);
	}
	
	/**
	 * Gets an additional property of this torrent.
	 * @param key
	 * @return Object
	 */
	
	public Object getProperty(String key) {
		return properties.get(key);
	}
	
	/**
	 * gets additional properties of this torrent.
	 * @return Map<String, Object>
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

	public int compareTo(Torrent o) {
		return getName().toLowerCase().compareTo(o.getName().toLowerCase());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Torrent){
			return (( Torrent )obj).getHash().equals(getHash());
		}
		return false;
	}

}
