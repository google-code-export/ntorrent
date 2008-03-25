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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;

import ntorrent.locale.ResourcePool;
import ntorrent.torrenttrackers.TorrentTrackerActionListener;
import ntorrent.torrenttrackers.model.TorrentTracker;
import ntorrent.torrenttrackers.model.TorrentTrackersListModel;


/**
 * @author Kim Eik
 *
 */
public class TorrentTrackerList extends JList implements ActionListener,MouseListener {
	private static final long serialVersionUID = 1L;
	private final TorrentTrackerPopupMenu popup = new TorrentTrackerPopupMenu(this);
	private TorrentTrackerActionListener listener = null;
	
	public TorrentTrackerList(TorrentTrackersListModel model) {
		super(model);
		addMouseListener(this);
		setCellRenderer(new ListCellRenderer(){
			
			public final static String bundle = "locale";
			
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
				
				JPanel panel = new JPanel(new BorderLayout());
				
				ImageIcon icon = new ImageIcon(iconUrl);
				JLabel iconHolder = new JLabel(icon);
				
				JTextPane tracker = new JTextPane();
				tracker.setText(tt.toString()+"\n" +
						ResourcePool.getString("scrape-done", bundle, this)+": "+tt.getScrapeComplete()+" "+
						ResourcePool.getString("scrape-down", bundle, this)+": "+tt.getScrapeDownloaded()+" "+
						ResourcePool.getString("scrape-undone", bundle, this)+": "+tt.getScrapeIncomplete()+" "+
						ResourcePool.getString("scrape-time", bundle, this)+": "+tt.getScrapeTimeLast()+"\n"+
						ResourcePool.getString("min-int", bundle, this)+": "+tt.getMinIntervall()+" "+
						ResourcePool.getString("nom-int", bundle, this)+": "+tt.getNormalIntervall());
				
				tracker.setOpaque(false);
				
				panel.add(iconHolder,BorderLayout.WEST);
				panel.add(tracker,BorderLayout.CENTER);

				
				if(isSelected){
					panel.setBackground(list.getSelectionBackground());
					panel.setForeground(list.getSelectionForeground());
					tracker.setBackground(list.getSelectionBackground());
					tracker.setForeground(list.getSelectionForeground());
				}else{
					panel.setBackground(list.getBackground());
					panel.setForeground(list.getForeground());
					tracker.setBackground(list.getBackground());
					tracker.setForeground(list.getForeground());
				}
				
				return panel;
			}			
		});
		
		
		
	}
	
	public void setTorrentTrackerActionListener(TorrentTrackerActionListener listener){
		this.listener = listener;
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		String[] M = TorrentTrackerPopupMenu.MENU_ITEMS;
		if(cmd.equals(M[0])){
			listener.setEnabled(true, (TorrentTracker)getSelectedValue());
		}else if(cmd.equals(M[1])){
			listener.setEnabled(false, (TorrentTracker)getSelectedValue());
		}
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		if(e.isPopupTrigger()){
			showPopup(e);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if(e.isPopupTrigger()){
			showPopup(e);
		}
	}
	
	private void showPopup(MouseEvent e){
		popup.show(this, e.getX(), e.getY());
	}
}
