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

public abstract class DataUnit implements Comparable<DataUnit> {
	protected Long data;
	
	public DataUnit(long b){
		data = b;
	}
	
	protected abstract String[] getUnitDesc();
	protected abstract int getUnitDivider();
	
	public long getValue(){ return data;}
	
	public String toString(){
		int x = 0;
		double bytes = this.data;
		while(bytes >= getUnitDivider()){
			bytes /= getUnitDivider();
			x++;
		}
		if(x < getUnitDesc().length){

			 DecimalFormat output = new DecimalFormat("#0.0 "+getUnitDesc()[x]);
			 return output.format(bytes);
		}
		return "...";		
	}

	public int compareTo(DataUnit o) {
		return data.compareTo(o.data);
	}

	public void setValue(long l) {
		data = l;
	}
	
	public void appendValue(DataUnit b){
		data += b.getValue();
	}
}
