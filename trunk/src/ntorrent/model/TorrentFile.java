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

import ntorrent.model.units.Bit;
import ntorrent.model.units.Byte;
import ntorrent.model.units.Percent;
import ntorrent.model.units.Ratio;


public class TorrentFile implements Comparable<TorrentFile>{
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
	private long priority;
	
	TorrentFile(String h){
		//Controller.writeToLog("New torrent: "+h);
		hash = h;
	}
	
	void setFilename(String f){ filename = f; }
	void setByteSize(Long b){ byteSize = new Byte(b); }
	void setBytesUploaded(Long b){ bytesUploaded = new Byte(b);}
	void setBytesDownloaded(Long b){ bytesDownloaded = new Byte(b); }
	void setRateDown(Long r){ rateDown = new Bit(r); }
	void setRateUp(Long r){ rateUp = new Bit(r); }
	void setStarted(boolean b){ started = b; }
	void setFiles(long num) { numFiles = (int)num; }
	void setFilePath(String s){ filePath = s; }
	void setTiedToFile(String s){ tiedToFile = s; }
	void setMessage(String s){message = s;}
	void setPriority(long pri){priority = pri;}
	
	public String getHash(){return hash;}
	public String getFilename(){ return filename; }
	public Byte getByteSize(){ return byteSize; }
	public Byte getBytesUploaded(){ return bytesUploaded;}
	public Byte getBytesDownloaded(){ return bytesDownloaded;}
	public Bit getRateDown(){ return rateDown; }
	public Bit getRateUp(){ return rateUp; }
	public int getNumFiles() { return numFiles; }
	public String getFilePath() {return filePath;}
	public String getTiedToFile() {return tiedToFile;}
	public String getMessage() {return message;}
	public long getPriority() {return priority;}
	public boolean isStarted(){ return started; }
	
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
		if(!(obj instanceof TorrentFile))
			return false;
		return ((TorrentFile)obj).hashCode() == hashCode();
	}
	
	@Override
	public int hashCode() {
		return getHash().hashCode();
	}
	
	public String toString(){
		return getFilename();
	}

	public int compareTo(TorrentFile o) {
		return getFilename().compareToIgnoreCase(o.getFilename());
	}
	
	
}
