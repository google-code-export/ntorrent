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
package ntorrent.torrenttrackers.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import ntorrent.torrenttrackers.model.TorrentTracker;
import ntorrent.torrenttrackers.model.TorrentTrackersListModel;


/**
 * @author Kim Eik
 *
 */
public class TorrentTrackerList extends JList {
	private static final long serialVersionUID = 1L;
	
	public TorrentTrackerList(TorrentTrackersListModel model) {
		super(model);
		setFixedCellHeight(40);
		setCellRenderer(new ListCellRenderer(){

			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				TorrentTracker tt = (TorrentTracker) value;
				String iconUrl = null;
				if(tt.isEnabled()){
					if(tt.isOpen()){
						iconUrl = "plugins/ntorrent.torrenttable/icons/downandup.png";
					}else{
						iconUrl = "plugins/ntorrent.torrenttable/icons/started.png";
					}
				}else{
					iconUrl = "plugins/ntorrent.torrenttable/icons/stopped.png";
				}
					
				ImageIcon icon = new ImageIcon(iconUrl);
				JLabel label = new JLabel(tt.toString(),icon,SwingConstants.LEFT);
				JLabel data = new JLabel(
						" Min-int: "+tt.getMinIntervall()+
						" Normal-int: "+tt.getNormalIntervall()+
						" Scrape-done: "+tt.getScrapeComplete()+
						" Scrape-down: "+tt.getScrapeDownloaded()+
						" Scrape-undone: "+tt.getScrapeIncomplete()+
						" Scrape-lasttime: "+tt.getScrapeTimeLast()
						
						);
				
				JPanel panel = new JPanel(new BorderLayout());
				
				panel.add(label,BorderLayout.NORTH);
				panel.add(data, BorderLayout.CENTER);
				
				if(isSelected){
					panel.setBackground(list.getSelectionBackground());
					panel.setForeground(list.getSelectionForeground());
				}else{
					panel.setBackground(list.getBackground());
					panel.setForeground(list.getForeground());
				}
				
				return panel;
			}			
		});
	}
	

}
