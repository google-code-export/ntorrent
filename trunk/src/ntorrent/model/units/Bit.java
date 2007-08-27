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

public class Bit implements Comparable<Bit> {
	Long bits;
	
	public Bit(Long r){
		bits = r;
	}
	
	public Bit(int i) {
		bits = (long)i;
	}

	public Long getValue(){ return bits;}
	
	public String toString(){
		int x = 0;
		double bits = this.bits;
		while(bits >= 1024){
			bits /= 1024;
			x++;
		}
		if(x < 4){
			String[] out = {"b/s","Kb/s","Mb/s","Gb/s"};

			 DecimalFormat output = new DecimalFormat("#0.0 "+out[x]);
			 return output.format(bits);
		}
		return "...";		
	}

	public int compareTo(Bit o) {
		return (int) (this.bits - o.bits);
	}		
}
