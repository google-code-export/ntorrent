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

import java.text.DecimalFormat;
import java.util.Comparator;

public class Ratio implements Comparable<Ratio>, Comparator<Ratio> {
	double ratio;
	
	public Ratio(double r){
		ratio = r;
	}
	
	public Double getValue(){ return ratio;}
	
	public String toString(){
		DecimalFormat output = new DecimalFormat("#0.00");
		return output.format(ratio);
	
	}

	public int compareTo(Ratio o) {
		if(this.ratio > o.ratio)
			return 1;
		if(this.ratio < o.ratio)
			return -1;
		return 0;
	}

	public int compare(Ratio o1, Ratio o2) {
		return o1.compareTo(o2);
	}		
}
