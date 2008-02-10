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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import ntorrent.env.Environment;
import ntorrent.tools.Serializer;
import ntorrent.torrenttable.model.TorrentColumnModel;

public class TorrentTablePopupMenu extends JPopupMenu implements ItemListener {
	private static final long serialVersionUID = 1L;
	final DefaultTableColumnModel model;
	
	public TorrentTablePopupMenu(DefaultTableColumnModel columnModel) {
		model = columnModel;
		
		for(String c : TorrentColumnModel.cols){
			JCheckBox check = new JCheckBox(c);
			check.setName(c);
			try{
				columnModel.getColumnIndex(c);
				check.setSelected(true);
			}catch(IllegalArgumentException x){
				check.setSelected(false);
			}
			check.addItemListener(this);
			add(check);
		}
		add(new JSeparator());
		JMenuItem save = add("torrenttable.popup.savestate");
		save.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				try {
					Serializer.serialize(model, Environment.getNtorrentDir());
				} catch (IOException x) {
					Logger.global.log(Level.WARNING,x.getMessage(),x);
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
		if(e.getStateChange() == ItemEvent.SELECTED){
			try{
				model.getColumnIndex(src.getName());
			}catch(IllegalArgumentException x){
				TableColumn col = new TableColumn();
				col.setHeaderValue(src.getText());
				col.setIdentifier(src.getName());
				model.addColumn(col);
			}
		}else{
			if(model.getColumnCount() > 1){
				int index = model.getColumnIndex(src.getName());
				model.removeColumn(model.getColumn(index));
			}else
				src.setSelected(true);
				
		}
	}
}
