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

package org.heldig.ntorrent.model;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("unchecked")
public class FileJTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	String[] columns = {
			"Priority",
			"Filename",
			"Size"
			};
	
	Vector<Object>[] data = new Vector[columns.length];
	
	public FileJTableModel() {
		clear();
	}
	
	public int getColumnCount() {
		return columns.length;
	}
	
    public String getColumnName(int col) {
        return columns[col];
    }

	public int getRowCount() {
		return data[0].size();
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[columnIndex].get(rowIndex);
	}
	
	public void clear(){
		for(int x = 0; x < data.length; x++)
			data[x] = new Vector();
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if(data[columnIndex].size() > rowIndex)
			data[columnIndex].set(rowIndex,aValue);
		else
			data[columnIndex].add(rowIndex, aValue);
		fireTableRowsInserted(rowIndex,rowIndex);
	}
	
	@Override
	public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

}
