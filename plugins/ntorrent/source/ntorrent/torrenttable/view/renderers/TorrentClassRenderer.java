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
package ntorrent.torrenttable.view.renderers;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import ntorrent.locale.ResourcePool;
import ntorrent.torrenttable.model.Torrent;

public class TorrentClassRenderer extends JPanel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	private static final ImageIcon stoppedIcon = new ImageIcon("plugins/ntorrent/icons/stopped.png");
	private static final ImageIcon startedIcon = new ImageIcon("plugins/ntorrent/icons/started.png");
	private static final ImageIcon messageIcon = new ImageIcon("plugins/ntorrent/icons/emblem-important.png");
	private static final ImageIcon upIcon = new ImageIcon("plugins/ntorrent/icons/uploading.png");
	private static final ImageIcon downIcon = new ImageIcon("plugins/ntorrent/icons/downloading.png");
	private static final ImageIcon dandupIcon = new ImageIcon("plugins/ntorrent/icons/downandup.png");
	private static final ImageIcon hashing = new ImageIcon("plugins/ntorrent/icons/hashing.png");
	
	private static final int selectedRowHeight = 40;
	private static final int standardRowHeight = 20;
	
	private final JLabel name = new JLabel();
	private final JLabel message = new JLabel();
	private final GridLayout layout = new GridLayout(0,1);
	
	boolean firstrun = true;
	
	public TorrentClassRenderer() {
		setLayout(layout);
		
		add(name);
		add(message);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		if(firstrun){
			firstrun = false;
			name.setFont(table.getFont());
			message.setFont(table.getFont());
		}
		
		if (value == null)
			return null;
		
		Torrent tor = (Torrent) value;
		name.setText(tor.toString());
		
		if(tor.isHashChecking())
			name.setIcon(hashing);
		else if(tor.isStarted()){
			if(tor.hasMessage()){
				name.setIcon(messageIcon);
			}else if(tor.isDownloading() && tor.isUploading()){
				name.setIcon(dandupIcon);
			}else if(tor.isDownloading()){
				name.setIcon(downIcon);
			}else if(tor.isUploading()){
				name.setIcon(upIcon);
			}else
				name.setIcon(startedIcon);
		}else{
			name.setIcon(stoppedIcon);
		}
		
		if(isSelected){
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
			name.setForeground(table.getSelectionForeground());
			message.setForeground(table.getSelectionForeground());
			
			if(tor.hasMessage() && tor.isStarted() && table.getSelectedRowCount() == 1){
				if(table.getRowHeight(row) == standardRowHeight)
					table.setRowHeight(row, selectedRowHeight); //eats cpu as its revalidates and repaints
				add(message);
				message.setText(ResourcePool.getString("message",this)+": "+tor.getMessage());
			}else{
				remove(message);
				if(table.getRowHeight(row) == selectedRowHeight)
					table.setRowHeight(row, standardRowHeight); //eats cpu as its revalidates and repaints
			}
			
		}else{
			setBackground(table.getBackground());
			setForeground(table.getForeground());
			name.setForeground(table.getForeground());
			message.setForeground(table.getForeground());
			if(table.getRowHeight(row) == selectedRowHeight)
				table.setRowHeight(row,standardRowHeight); //eats cpu as its revalidates and repaints
			remove(message);
		}
		
		return this;
	}

}
