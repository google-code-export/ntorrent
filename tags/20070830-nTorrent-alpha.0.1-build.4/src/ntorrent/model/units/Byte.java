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

package ntorrent.model.units;

import java.text.DecimalFormat;

public class Byte implements Comparable<Byte>{
	long bytes;
	public Byte(long b){
		bytes = b;
	}
	
	public long getValue(){ return bytes;}
	
	public String toString(){
		int x = 0;
		double bytes = this.bytes;
		while(bytes >= 1024){
			bytes /= 1024;
			x++;
		}
		if(x < 4){
			String[] out = {"B","KB","MB","GB"};

			 DecimalFormat output = new DecimalFormat("#0.0 "+out[x]);
			 return output.format(bytes);
		}
		return "...";		
	}

	public int compareTo(Byte o) {
		return (int) (this.bytes-o.bytes);
	}

	public void setValue(long l) {
		bytes = l;
	}
}
