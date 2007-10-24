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

package org.heldig.ntorrent.gui.torrent;

import java.util.HashMap;

import org.heldig.ntorrent.NTorrent;
import org.heldig.ntorrent.model.Bit;
import org.heldig.ntorrent.model.Byte;
import org.heldig.ntorrent.model.Percent;
import org.heldig.ntorrent.model.Priority;
import org.heldig.ntorrent.model.Ratio;


/**
 * @author   netbrain
 */
public class TorrentInfo implements Comparable<TorrentInfo>{
	private String hash;
	private String filename;
	private Byte byteSize = new Byte(1);
	private Byte bytesDownloaded = new Byte(1);
	private Byte bytesUploaded = new Byte(1);
	private Bit rateDown = new Bit(1);
	private Bit rateUp = new Bit(1);
	private Percent percentFinished = new Percent(1);
	private boolean started;
	private int numFiles;
	private String filePath;
	private String tiedToFile;
	private String message;
	private Priority priority;
	private long lastUpdate;
	private Long trackerNum;
	private Long peersComplete;
	private Long peersNotConnected;
	private Long peersConnected;
	private String label = new String();
	
	TorrentInfo(String h){
		hash = h;
	}
	
	/**
	 * @param  f
	 */
	void setFilename(String f){ filename = f; }
	void setByteSize(Long b){ byteSize = new Byte(b); }
	void setBytesUploaded(Long b){ bytesUploaded = new Byte(b);}
	void setBytesDownloaded(Long b){ bytesDownloaded = new Byte(b); }
	void setRateDown(Long r){ rateDown = new Bit(r); }
	void setRateUp(Long r){ rateUp = new Bit(r); }
	/**
	 * @param  b
	 */
	void setStarted(boolean b){ started = b; }
	void setFiles(long num) { numFiles = (int)num; }
	/**
	 * @param  s
	 */
	void setFilePath(String s){ filePath = s; }
	/**
	 * @param  s
	 */
	void setTiedToFile(String s){ tiedToFile = s; }
	/**
	 * @param  s
	 */
	void setMessage(String s){message = s;}
	void setPriority(long pri){priority = new Priority(pri);}
	private void touch(){ lastUpdate = System.currentTimeMillis();}
	
	/**
	 * @return
	 */
	public String getHash(){return hash;}
	/**
	 * @return
	 */
	public String getFilename(){ return filename; }
	/**
	 * @return
	 */
	public Byte getByteSize(){ return byteSize; }
	/**
	 * @return
	 */
	public Byte getBytesUploaded(){ return bytesUploaded;}
	/**
	 * @return
	 */
	public Byte getBytesDownloaded(){ return bytesDownloaded;}
	/**
	 * @return
	 */
	public Bit getRateDown(){ return rateDown; }
	/**
	 * @return
	 */
	public Bit getRateUp(){ return rateUp; }
	/**
	 * @return
	 */
	public int getNumFiles() { return numFiles; }
	/**
	 * @return
	 */
	public String getFilePath() {return filePath;}
	/**
	 * @return
	 */
	public String getTiedToFile() {return tiedToFile;}
	/**
	 * @return
	 */
	public String getMessage() {return message;}
	/**
	 * @return
	 */
	public Priority getPriority() {return priority;}
	/**
	 * @return
	 */
	
	public String getLabel() {
		return label;
	}
	
	public boolean isStarted(){ return started; }
	public boolean isOutOfDate(){ 
		return System.currentTimeMillis()-lastUpdate > NTorrent.settings.vintervall;
	}
	
	/**
	 * @return
	 */
	Percent getPercentFinished(){
		long down = getBytesDownloaded().getValue();
		long total = getByteSize().getValue();
		percentFinished.setValue((int)(down*100/total));
		return percentFinished; 
	}
	Ratio getRatio(){
		double down = getBytesDownloaded().getValue();
		double up = getBytesUploaded().getValue();
		double ratio = 0.00;
		if(down > 0)
			ratio = up/down;
		return new Ratio(ratio);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof String)
			return ((String)obj).equals(getHash());
		if(!(obj instanceof TorrentInfo))
			return false;
		return ((TorrentInfo)obj).hashCode() == hashCode();
	}
	
	@Override
	public int hashCode() {
		return getHash().hashCode();
	}
	
	public String toString(){
		return getFilename();
	}

	public int compareTo(TorrentInfo o) {
		return getFilename().compareToIgnoreCase(o.getFilename());
	}
	
	public void setInfo(HashMap<String,Object> set){
		touch();
		for(String key : set.keySet()){
			if("d.get_name=".equals(key)){
				setFilename((String)set.get(key));
			}else if("d.get_size_bytes=".equals(key)){
				setByteSize((Long)set.get(key));
			}else if("d.get_size_files=".equals(key)){
				setFiles((Long)set.get(key));
			}else if("d.get_base_path=".equals(key)){
				setFilePath((String)set.get(key));
			}else if("d.get_up_total=".equals(key)){
				setBytesUploaded((Long)set.get(key));
			}else if("d.get_completed_bytes=".equals(key)){
				setBytesDownloaded((Long)set.get(key));
			}else if("d.get_down_rate=".equals(key)){
				setRateDown((Long)set.get(key));
			}else if("d.get_up_rate=".equals(key)){
				setRateUp((Long)set.get(key));
			}else if("d.get_state=".equals(key)){
				setStarted((Long)set.get(key) == 1);
			}else if("d.get_message=".equals(key)){
				setMessage((String)set.get(key));
			}else if("d.get_priority=".equals(key)){
				setPriority((Long)set.get(key));
			}else if("d.get_tied_to_file=".equals(key)){
				setTiedToFile((String)set.get(key));
			}else if("d.get_peers_connected=".equals(key)){
				setPeersConnected((Long)set.get(key));
			}else if("d.get_peers_not_connected=".equals(key)){
				setPeersNotConnected((Long)set.get(key));
			}else if("d.get_peers_complete=".equals(key)){
				setPeersComplete((Long)set.get(key));
			}else if("d.get_tracker_size=".equals(key)){
				setTrackerSize((Long)set.get(key));
			}else if("d.get_custom1=".equals(key)){
				setLabel((String)set.get(key));
			}
		}
	}

	private void setTrackerSize(Long long1) {
		// TODO Auto-generated method stub
		trackerNum = long1;
	}

	/**
	 * @param  long1
	 */
	private void setPeersComplete(Long long1) {
		// TODO Auto-generated method stub
		peersComplete = long1;
	}

	/**
	 * @param  long1
	 */
	private void setPeersNotConnected(Long long1) {
		// TODO Auto-generated method stub
		peersNotConnected = long1;
	}

	/**
	 * @param  long1
	 */
	private void setPeersConnected(Long long1) {
		// TODO Auto-generated method stub
		peersConnected = long1;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public Long getSeeders() {
		return peersComplete;
	}
	
	public Long getLeechers(){
		return getPeersConnected()-getSeeders();
	}
	
	/**
	 * @return
	 */
	public Long getPeersConnected() {
		return peersConnected;
	}
	
	/**
	 * @return
	 */
	public Long getPeersNotConnected() {
		return peersNotConnected;
	}
	
	public Long getPeersTotal(){
		return getPeersConnected()+getPeersNotConnected();
	}
	
	/**
	 * @return
	 */
	public Long getTrackerNum() {
		return trackerNum;
	}
}
