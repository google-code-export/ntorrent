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
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.TableColumn;

import org.heldig.ntorrent.event.ControllerEventListener;
import org.heldig.ntorrent.event.NativeAction;
import org.heldig.ntorrent.gui.render.PercentRenderer;
import org.heldig.ntorrent.gui.render.TorrentTitleRenderer;
import org.heldig.ntorrent.language.Language;
import org.heldig.ntorrent.model.Percent;


/**
 * @author   Kim Eik
 */
public class TorrentJTableComponent extends JTable implements ListDataListener, MouseListener, ActionListener{
	private static final long serialVersionUID = 1L;
	private final ControllerEventListener event;
	private static final JPopupMenu popup = new JPopupMenu();
	private static final JMenu subpriority = new JMenu(Language.Torrent_Menu_Priority_set_priority);
	private final JMenu sublabel = new JMenu(Language.Torrent_Menu_Priority_set_label);
	private final JMenu sublocal = new JMenu(Language.Local_Menu_local);
	private final JMenu subssh = new JMenu(Language.Ssh_Menu_ssh);
	private static int[] selectedRows;
	
	public TorrentJTableComponent(ControllerEventListener e){
		super(new TorrentJTableModel());
		event = e;
		popup.add(Language.Torrent_Menu_start).addActionListener(this);
		popup.add(Language.Torrent_Menu_stop).addActionListener(this);
		popup.add(Language.Torrent_Menu_remove_torrent).addActionListener(this);
		popup.add(new JSeparator());
		popup.add(subpriority);
		popup.add(sublabel);
		popup.add(new JSeparator());
		popup.add(Language.Torrent_Menu_check_hash).addActionListener(this);
		popup.add(new JSeparator());
		popup.add(sublocal);
		popup.add(subssh);
		
		subpriority.add(Language.Priority_Menu_high).addActionListener(this);
		subpriority.add(Language.Priority_Menu_normal).addActionListener(this);
		subpriority.add(Language.Priority_Menu_low).addActionListener(this);
		subpriority.add(Language.Priority_Menu_off).addActionListener(this);

		sublabel.add(Language.Label_none).addActionListener(this);
		sublabel.add(Language.Label_new_label).addActionListener(this);
		sublabel.add(new JSeparator());
		
		sublocal.add(Language.Local_Menu_open_file).addActionListener(this);
		sublocal.add(Language.Local_Menu_remove_data).addActionListener(this);
		
		subssh.add(Language.Ssh_Menu_copy).addActionListener(this);
		subssh.add(Language.Ssh_Menu_remove_data).addActionListener(this);
		
		subssh.setEnabled(false);
		sublocal.setEnabled(false);
		
		setShowHorizontalLines(true);
		setShowVerticalLines(false);
		setRowMargin(5);
		setRowHeight(25);
		setBackground(Color.white);
		setFillsViewportHeight(true);
		setDefaultRenderer(TorrentInfo.class, new TorrentTitleRenderer());
		setDefaultRenderer(Percent.class, new PercentRenderer());
		addMouseListener(this);
		
		TableColumn column = null;
		for (int i = 0; i < getColumnCount(); i++) {
		    column = getColumnModel().getColumn(i);
		    if (i == 0) {
		        column.setPreferredWidth(300);
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
		if(e.isPopupTrigger())
			maybeShowPopup(e);
        maybeShowFileTab(e);
    }
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.isPopupTrigger())
			maybeShowPopup(e);
	}

	private void maybeShowFileTab(MouseEvent e) {
		JTable source = (JTable)e.getSource();
		if(source.getSelectedRowCount() == 1){
			TorrentInfo tf = ((TorrentInfo)source.getValueAt(source.getSelectedRow(), 0));
			event.torrentSelectionEvent(tf);
		}		
	}
	protected void maybeShowPopup(MouseEvent e) {
        	switch(event.getProtocol()){
    			case SSH:
    				subssh.setEnabled(true);
    				break;
    			case LOCAL: 
    				sublocal.setEnabled(true);
    				break;
  
        	}
    	JTable source = (JTable)e.getComponent();
    	if(source.getSelectedRowCount() > 0)
	        if (e.isPopupTrigger()) {
	        	selectedRows = source.getSelectedRows();
	            popup.show(source,  e.getX(), e.getY());
	        }
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
		TorrentInfo[] tf = new TorrentInfo[selectedRows.length];
		String[] hash = new String[selectedRows.length];
		for(int i = 0; i < selectedRows.length; i++){
			tf[i] = (TorrentInfo)getModel().getValueAt(selectedRows[i], 0);
			hash[i] = tf[i].getHash();
		}
		Language l = Language.getFromString(e.getActionCommand());
		if(l != null)
			switch(l){
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
				case Label_custom:
					event.setLabel(hash, ((JMenuItem)e.getSource()).getText());
					break;
					
			}
		else
			System.out.println(e.getActionCommand());
	}



	@Override
	public void contentsChanged(ListDataEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void intervalAdded(ListDataEvent e) {
		ListModel model = (ListModel)e.getSource();
		Action a = new NativeAction();
		a.putValue(Action.NAME, model.getElementAt(e.getIndex0()));
		a.putValue(Action.ACTION_COMMAND_KEY, Language.Label_custom.name());
		sublabel.add(a).addActionListener(this);
	}



	@Override
	public void intervalRemoved(ListDataEvent e) {
		// TODO Auto-generated method stub
		
	}

}
