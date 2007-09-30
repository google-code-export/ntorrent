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

package ntorrent.gui.view;

import java.awt.Color;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import ntorrent.Controller;
import ntorrent.gui.FileTabComponent;
import ntorrent.gui.listener.JTablePopupMenuListener;
import ntorrent.model.TorrentFile;
import ntorrent.model.TorrentTableModel;
import ntorrent.model.render.PercentRenderer;
import ntorrent.model.render.TorrentTitleRenderer;
import ntorrent.model.units.Percent;
import ntorrent.settings.Constants.Commands;

/**
 * @author   netbrain
 */
public class MainTableComponent extends JTablePopupMenuListener {
	JTable table = new JTable(new TorrentTableModel());
	
	final static String[] menuItems = {
		Commands.START.toString(),
		Commands.STOP.toString(),
		null,
		Commands.OPEN.toString(),
		Commands.CHECK_HASH.toString(),
		Commands.CLOSE.toString(),
		null,
		Commands.REMOVE.toString()};
	
	public MainTableComponent(Controller c){
		super(c, menuItems);
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
	
    public void mousePressed(MouseEvent e) {
    	super.mousePressed(e);
        hideFileTab();
    }

	public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        maybeShowFileTab(e);
    }
	
	private void maybeShowFileTab(MouseEvent e) {
		JTable source = (JTable)e.getSource();
		if(source.getSelectedRowCount() == 1){
			TorrentFile tf = ((TorrentFile)source.getValueAt(source.getSelectedRow(), 0));
			FileTabComponent panel = C.getGC().getFileTab();
			panel.getInfoPanel().setInfo(tf);
			C.getIO().getRpc().getFileList(tf.getHash(),panel.getFileList());
		}else
			hideFileTab();
		
	}
	protected void maybeShowPopup(MouseEvent e) {
    	JTable source = (JTable)e.getComponent();
    	if(source.getSelectedRowCount() > 0)
	        if (e.isPopupTrigger()) {
	        	selectedRows = source.getSelectedRows();
	            popup.show(source,  e.getX(), e.getY());
	        }
    }
    
	private void hideFileTab() {
    	FileTabComponent panel = C.getGC().getFileTab();
    	panel.getFileList().hideInfo();
    	panel.getInfoPanel().hideInfo();
	}
	
	/**
	 * @return
	 */
	public JTable getTable() {
		return table;
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
}
