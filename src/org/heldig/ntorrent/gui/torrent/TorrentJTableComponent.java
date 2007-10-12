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

package org.heldig.ntorrent.gui.torrent;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.heldig.ntorrent.event.ControllerEventListener;
import org.heldig.ntorrent.gui.core.JTablePopupMenuImplementation;
import org.heldig.ntorrent.gui.render.PercentRenderer;
import org.heldig.ntorrent.gui.render.TorrentTitleRenderer;
import org.heldig.ntorrent.language.Language;
import org.heldig.ntorrent.model.Percent;


/**
 * @author   Kim Eik
 */
public class TorrentJTableComponent extends JTablePopupMenuImplementation{
	final TorrentJTableModel model = new TorrentJTableModel();
	final JTable table = new JTable(model);
	private ControllerEventListener event;
	Language[] subpriority = {
		Language.Torrent_Menu_Priority_set_priority,
		Language.Priority_Menu_high,
		Language.Priority_Menu_normal,
		Language.Priority_Menu_low,
		Language.Priority_Menu_off
		};
	
	Language[] sublocal = {
		Language.Local_Menu_local,
		Language.Local_Menu_open_file,
		Language.Local_Menu_remove_data,
	};
	
	Language[] subssh = {
		Language.Ssh_Menu_ssh,
		Language.Ssh_Menu_copy,
		Language.Ssh_Menu_remove_data
	};
	
	Language[] sublabel = {
		Language.Torrent_Menu_Priority_set_label,
		Language.Label_none,
		null,
		null,
		Language.Label_new_label
	};
	
	Object[] menuItems = {
		Language.Torrent_Menu_start,
		Language.Torrent_Menu_stop,
		Language.Torrent_Menu_remove_torrent,
		null,
		subpriority,
		sublabel,
		null,
		Language.Torrent_Menu_check_hash,
		null,
		sublocal,
		subssh
		
	};

	
	public TorrentJTableComponent(ControllerEventListener e){
		subssh[0].setEnabled(false);
		sublocal[0].setEnabled(false);
		event = e;
		table.setShowHorizontalLines(true);
		table.setShowVerticalLines(false);
		table.setRowMargin(5);
		table.setRowHeight(25);
		table.setBackground(Color.white);
		table.setFillsViewportHeight(true);
		table.setDefaultRenderer(TorrentInfo.class, new TorrentTitleRenderer());
		table.setDefaultRenderer(Percent.class, new PercentRenderer());
		table.addMouseListener(this);
		
		TableColumn column = null;
		for (int i = 0; i < table.getColumnCount(); i++) {
		    column = table.getColumnModel().getColumn(i);
		    if (i == 0) {
		        column.setPreferredWidth(300); //third column is bigger
		    }else if(i == 4 || i == 5){
		    	column.setPreferredWidth(48);
		    }else if(i == 9 || i == 10){
		    	column.setPreferredWidth(32);
		    } else {
		        column.setPreferredWidth(75);
		    }
		}
	}
	
	

	public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        maybeShowFileTab(e);
    }
	
	private void maybeShowFileTab(MouseEvent e) {
		JTable source = (JTable)e.getSource();
		if(source.getSelectedRowCount() == 1){
			TorrentInfo tf = ((TorrentInfo)source.getValueAt(source.getSelectedRow(), 0));
			event.torrentSelectionEvent(tf);
		}		
	}
	protected void maybeShowPopup(MouseEvent e) {
		if(popup.getComponentCount() == 0){
        	switch(event.getProtocol()){
    			case SSH:
    				subssh[0].setEnabled(true);
    				break;
    			case LOCAL: 
    				sublocal[0].setEnabled(true);
        	}
			createMenuItems(popup,menuItems, this);
		}
    	JTable source = (JTable)e.getComponent();
    	if(source.getSelectedRowCount() > 0)
	        if (e.isPopupTrigger()) {
	        	selectedRows = source.getSelectedRows();
	            popup.show(source,  e.getX(), e.getY());
	        }
    }
    
	
	/**
	 * @return
	 */
	public JTable getTable() {
		return table;
	}
	
	public TorrentJTableModel getModel() {
		return model;
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		TorrentInfo[] tf = new TorrentInfo[selectedRows.length];
		String[] hash = new String[selectedRows.length];
		for(int i = 0; i < selectedRows.length; i++){
			tf[i] = (TorrentInfo)model.getValueAt(selectedRows[i], 0);
			hash[i] = tf[i].getHash();
		}
		switch(Language.getFromString(e.getActionCommand())){
			case Local_Menu_remove_data:
				if(JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(null, 
						"Are you sure?", 
						null, 
						JOptionPane.YES_NO_OPTION)) break;
			case Local_Menu_open_file:
					event.localEvent(tf, e.getActionCommand());
				break;
			case Ssh_Menu_copy:
				if(selectedRows.length == 1){
					final JFileChooser pf = new JFileChooser();
					pf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					if(pf.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
						event.sshCopyEvent(tf[0], pf.getSelectedFile());
				}
				break;
			case Ssh_Menu_remove_data:
				event.sshRemoveEvent(tf);
				break;
			case Torrent_Menu_start:
				event.torrentCommand(hash, "d.start");
				break;
			case Torrent_Menu_check_hash:
				event.torrentCommand(hash, "d.check_hash");
				break;
			case Torrent_Menu_stop:
				event.torrentCommand(hash, "d.stop");
				break;
			case Torrent_Menu_remove_torrent:
				event.torrentCommand(hash, "d.erase");
				break;
			case Priority_Menu_high:
				event.setTorrentPriority(hash, 3);
				break;
			case Priority_Menu_low:
				event.setTorrentPriority(hash, 1);
				break;
			case Priority_Menu_normal:
				event.setTorrentPriority(hash, 2);
				break;
			case Priority_Menu_off:
				event.setTorrentPriority(hash, 0);
				break;
			case Label_new_label:
				event.setLabel(hash,(String)JOptionPane.showInputDialog(null));
				break;
			case Label_none:
				event.setLabel(hash, "");
				break;
				
		}	
	}
}
