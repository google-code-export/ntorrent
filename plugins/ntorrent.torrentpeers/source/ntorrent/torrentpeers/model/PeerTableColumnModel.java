package ntorrent.torrentpeers.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import ntorrent.data.Environment;
import ntorrent.locale.ResourcePool;
import ntorrent.tools.Serializer;

public class PeerTableColumnModel implements Serializable{

	private static final long serialVersionUID = -3062295634275635868L;
	
	public static transient final String[] cols = {
		"address",
		"clientVersion",
		"downRate",
		"upRate",
		"downTotal",
		"upTotal",
		"peerRate",
		"peerTotal",
	};
	
	public static transient final int[] widths = {
		100,
		100,
		50,
		50,
		50,
		50,
		50,
		50
	};
	
	private transient DefaultTableColumnModel model = new DefaultTableColumnModel();
	private List<ColumnModel> columnModel = new ArrayList<ColumnModel>();
	
	public PeerTableColumnModel() {
		for(int x = 0; x < cols.length; x++){
			columnModel.add(new ColumnModel(cols[x],widths[x]));
		}
		createTableColumnModel();
		
		try{
			PeerTableColumnModel obj = (PeerTableColumnModel)Serializer.deserialize(PeerTableColumnModel.class, Environment.getNtorrentDir());
			List<ColumnModel> columnModel = obj.columnModel;
			List<TableColumn> tableColumns = new ArrayList<TableColumn>();
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
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void createTableColumnModel() {
		for (int x = 0; x < columnModel.size(); x++) {
			TableColumn t = new TableColumn(x);
			ColumnModel c = columnModel.get(x);
			t.setHeaderValue(ResourcePool.getString(c.name, "locale", this));
			t.setIdentifier(c.name);
			t.setPreferredWidth(c.width);
			model.addColumn(t);
		}
	}
	
	public DefaultTableColumnModel getModel() {
		return model;
	}
	
	private void clearTableColumnModel(){
		int num = model.getColumnCount();
		while(num > 0){
			model.removeColumn(model.getColumn(--num));
		}
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
