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
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import ntorrent.torrenttable.model.Torrent;

public class TorrentClassRenderer extends JPanel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	private static final ImageIcon stoppedIcon = new ImageIcon("plugins/ntorrent.torrenttable/icons/stopped.png");
	private static final ImageIcon startedIcon = new ImageIcon("plugins/ntorrent.torrenttable/icons/started.png");
	private static final ImageIcon messageIcon = new ImageIcon("plugins/ntorrent.torrenttable/icons/emblem-important.png");
	private static final ImageIcon upIcon = new ImageIcon("plugins/ntorrent.torrenttable/icons/uploading.png");
	private static final ImageIcon downIcon = new ImageIcon("plugins/ntorrent.torrenttable/icons/downloading.png");
	private static final ImageIcon dandupIcon = new ImageIcon("plugins/ntorrent.torrenttable/icons/downandup.png");
	
	JLabel name = new JLabel();
	JLabel message = new JLabel();
	
	boolean firstrun = true;
	
	public TorrentClassRenderer() {
		setLayout(new GridLayout(0,1));
		
		add(name);
		add(message);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		if(firstrun){
			name.setFont(table.getFont());
			message.setFont(table.getFont());
		}
		
		Torrent tor = (Torrent) value;
		try{
			name.setText(tor.toString());
			
			if(tor.hasMessage()){
				name.setIcon(messageIcon);
			}else if(tor.isStarted()){
				if(tor.isDownloading() && tor.isUploading()){
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
				//if(System.getProperty("java.specification.version") == "1.6"){
					name.setForeground(table.getSelectionForeground());
					//name.setBackground(table.getSelectionBackground());
					message.setForeground(table.getSelectionForeground());
					//message.setBackground(table.getSelectionBackground());
				//}
				if(tor.hasMessage()){
					add(message);
					table.setRowHeight(row, 40);
					message.setText("Message: "+tor.getMessage());
				}else{
					remove(message);
				}
				
			}else{
				setBackground(table.getBackground());
				setForeground(table.getForeground());
				name.setForeground(table.getForeground());
				message.setForeground(table.getForeground());
				table.setRowHeight(row,20);
				remove(message);
			}
		}catch(NullPointerException x){
			//something wrong
		}
		
		
		return this;
	}

}
