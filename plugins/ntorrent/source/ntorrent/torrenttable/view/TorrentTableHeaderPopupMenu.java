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
package ntorrent.torrenttable.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import ntorrent.locale.ResourcePool;
import ntorrent.tools.Serializer;
import ntorrent.torrenttable.model.TorrentTableColumnModel;

import org.apache.log4j.Logger;

public class TorrentTableHeaderPopupMenu extends JPopupMenu implements ItemListener{
	private static final long serialVersionUID = 1L;
	private final TorrentTableColumnModel model;
	
	/**
	 * Log4j logger
	 */
	private final static Logger log = Logger.getLogger(TorrentTableHeaderPopupMenu.class);
	
	public TorrentTableHeaderPopupMenu(final TorrentTableColumnModel model) {
		this.model = model;
		
		for(String c : TorrentTableColumnModel.cols){
			String name = ResourcePool.getString(c,this);
			JCheckBox check = new JCheckBox(name);
			check.setName(c);
			try{
				model.getModel().getColumnIndex(c);
				check.setSelected(true);
			}catch(IllegalArgumentException x){
				check.setSelected(false);
			}
			check.addItemListener(this);
			add(check);
		}
		add(new JSeparator());
		JMenuItem save = add(ResourcePool.getString("savestate",this));
		save.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				//doesnt work.
				try {
					if(model instanceof Serializable){
						Serializer.serialize(model);
					}
				} catch (IOException x) {
					log.warn(x.getMessage(),x);
				}
			}
			
		});
	}

	@Override
	public void show(Component invoker, int x, int y) {
		super.show(invoker, x, y);
	}

	public void itemStateChanged(ItemEvent e) {
		JCheckBox src = ((JCheckBox) e.getItem());
		TableColumnModel model = this.model.getModel();
		if(e.getStateChange() == ItemEvent.SELECTED){
			try{
				model.getColumnIndex(src.getName());
			}catch(IllegalArgumentException x){
				TableColumn col = new TableColumn();
				col.setHeaderValue(src.getText());
				col.setIdentifier(src.getName());
				for(int i = 0; i < TorrentTableColumnModel.cols.length; i++){
					if(TorrentTableColumnModel.cols[i].equals(src.getName()))
						col.setModelIndex(i);
				}
				model.addColumn(col);
				//System.out.println("adding col"+src.getName());
			}
		}else{
			if(model.getColumnCount() > 1){
				int index = model.getColumnIndex(src.getName());
				//System.out.println("removing column "+src.getName());
				model.removeColumn(model.getColumn(index));
			}else
				src.setSelected(true);
				
		}
	}
}
