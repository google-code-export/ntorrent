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

package ntorrent.gui.main.view;

import java.awt.Color;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import ntorrent.model.TorrentFile;
import ntorrent.model.TorrentTableModel;
import ntorrent.model.render.PercentRenderer;
import ntorrent.model.render.TorrentTitleRenderer;
import ntorrent.model.units.Percent;

public class MainTableComponent {
	JTable table = new JTable(new TorrentTableModel());
	
	public MainTableComponent(){
		//Not stable... probably make own sorter.
		//table.setAutoCreateRowSorter(true);
		table.setShowHorizontalLines(true);
		table.setShowVerticalLines(false);
		table.setRowMargin(5);
		//table.setCellSelectionEnabled(false);
		//table.setRowSelectionAllowed(true);
		table.setRowHeight(25);
		table.setBackground(Color.white);
		//table.setSelectionBackground(Color.LIGHT_GRAY);
		//table.setSelectionBackground(new Color(210,225,225));
		//table.setSelectionForeground(Color.black);
		table.setFillsViewportHeight(true);
		//table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		table.setDefaultRenderer(TorrentFile.class, new TorrentTitleRenderer());
		table.setDefaultRenderer(Percent.class, new PercentRenderer());
		//TorrentPopUpListener tp = new TorrentPopUpListener();
		table.addMouseListener(new TorrentTableListener());
		
		TableColumn column = null;
		for (int i = 0; i < table.getColumnCount(); i++) {
		    column = table.getColumnModel().getColumn(i);
		    if (i == 0) {
		        column.setPreferredWidth(300); //third column is bigger
		    }else if(i == 7){
		    	column.setPreferredWidth(32);
		    } else {
		        column.setPreferredWidth(75);
		    }
		}
	}
	
	public JTable getTable() {
		return table;
	}
}
