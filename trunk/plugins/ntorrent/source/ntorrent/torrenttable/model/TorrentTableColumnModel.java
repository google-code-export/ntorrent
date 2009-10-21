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

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import ntorrent.NtorrentApplication;
import ntorrent.locale.ResourcePool;
import ntorrent.tools.Serializer;


public class TorrentTableColumnModel implements Serializable{
	private static final long serialVersionUID = 1L;

	public static transient final String[] cols = {
		"name",
		"size",
		"down",
		"up",
		"seeders",
		"leechers",
		"downspeed",
		"upspeed",
		"eta",
		"percent",
		"ratio",
		"priority",
		"label"
	};
	
	public static transient final int[] widths = {
		300,
		50,
		50,
		50,
		20,
		20,
		50,
		50,
		50,
		50,
		50,
		50,
		50
	};
	
	private transient DefaultTableColumnModel model = new DefaultTableColumnModel();
	private Vector<ColumnModel> columnModel = new Vector<ColumnModel>();
	
	public TorrentTableColumnModel() {
		for(int x = 0; x < cols.length; x++){
			columnModel.add(new ColumnModel(cols[x],widths[x]));
		}
		createTableColumnModel();
		
		try{
			TorrentTableColumnModel obj = Serializer.deserialize(TorrentTableColumnModel.class, NtorrentApplication.SETTINGS.getNtorrent());
			//TODO dont deserialize here, deserialize in the object referencing this.
			if(obj != null){
				Vector<ColumnModel> columnModel = obj.columnModel;
				Vector<TableColumn> tableColumns = new Vector<TableColumn>();
				for(ColumnModel c : columnModel){
					int index = model.getColumnIndex(c.name);
					TableColumn tc = model.getColumn(index);
					tableColumns.add(tc);
					model.removeColumn(tc);
					tc.setPreferredWidth(c.width);
				}
				
				clearTableColumnModel();
				
				for(TableColumn t : tableColumns)
					model.addColumn(t);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
	private void createTableColumnModel(){
		for(int x = 0; x < columnModel.size(); x++){
			TableColumn t = new TableColumn(x);
			ColumnModel c = columnModel.get(x);
			t.setHeaderValue(ResourcePool.getString(c.name,this));
			t.setIdentifier(c.name);
			t.setPreferredWidth(c.width);
			model.addColumn(t);
		}
	}
	
	private void clearTableColumnModel(){
		int num = model.getColumnCount();
		while(num > 0){
			model.removeColumn(model.getColumn(--num));
		}
	}
	
	public DefaultTableColumnModel getModel() {
		return model;
	}
	
	private Object writeReplace() throws ObjectStreamException{
		columnModel.clear();
		for(int x = 0; x < model.getColumnCount(); x++){
			TableColumn c = model.getColumn(x);
			columnModel.add(x, new ColumnModel((String)c.getIdentifier(),c.getWidth()));
		}
		return this;
	}
	
	private class ColumnModel implements Serializable{
		private static final long serialVersionUID = 1L;
		private final String name;
		private final int width;

		public ColumnModel(String name,int width) {
			this.name = name;
			this.width = width;
		}
		
		@Override
		public String toString() {
			return "["+name+" width="+width+"]";
		}
	}
	
}
