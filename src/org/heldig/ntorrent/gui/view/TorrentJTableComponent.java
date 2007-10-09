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

package org.heldig.ntorrent.gui.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.heldig.ntorrent.Controller;
import org.heldig.ntorrent.NTorrent;
import org.heldig.ntorrent.gui.FileTabComponent;
import org.heldig.ntorrent.gui.listener.JTablePopupMenuListener;
import org.heldig.ntorrent.gui.render.PercentRenderer;
import org.heldig.ntorrent.gui.render.TorrentTitleRenderer;
import org.heldig.ntorrent.io.xmlrpc.ssh.SshConnection;
import org.heldig.ntorrent.model.TorrentInfo;
import org.heldig.ntorrent.model.TorrentJTableModel;
import org.heldig.ntorrent.model.units.Percent;
import org.heldig.ntorrent.settings.Constants.Commands;

import com.sshtools.j2ssh.SshClient;


/**
 * @author   Kim Eik
 */
public class TorrentJTableComponent extends JTablePopupMenuListener {
	JTable table = new JTable(new TorrentJTableModel());
	final static Object[] subpriority = {
		"Set priority",
		"High",
		"Normal",
		"Low",
		"Off"
		};
	
	final static Object[] sublocal = {
		"Local",
		"Open file",
		"(Remove data)",
	};
	
	final static Object[] subssh = {
		"Ssh",
		"Copy to local",
		"(Remove data)"
	};
	
	final static Object[] menuItems = {
		Commands.START.toString(),
		Commands.STOP.toString(),
		Commands.REMOVE_TORRENT.toString(),
		null,
		subpriority,
		null,
		Commands.OPEN.toString(),
		Commands.CHECK_HASH.toString(),
		Commands.CLOSE.toString(),
		null,
		sublocal,
		subssh
		
	};

	
	public TorrentJTableComponent(Controller c){
		super(c, menuItems);
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
			TorrentInfo tf = ((TorrentInfo)source.getValueAt(source.getSelectedRow(), 0));
			FileTabComponent panel = C.GC.getFileTab();
			panel.getInfoPanel().setInfo(tf);
			C.IO.getRpc().getFileList(tf.getHash(),panel.getFileList());
			C.IO.getRpc().getTrackerList(tf.getHash(), panel.getTrackerList(tf.getHash()));
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
    	FileTabComponent panel = C.GC.getFileTab();
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		String cmd = e.getActionCommand();
		if(cmd.equals("High")){
			C.MC.getTorrentPool().setPriority(selectedRows, 3);
		}else if(cmd.equals("Normal")){
			C.MC.getTorrentPool().setPriority(selectedRows, 2);
		}else if(cmd.equals("Low")){
			C.MC.getTorrentPool().setPriority(selectedRows, 1);
		}else if(cmd.equals("Off")){
			C.MC.getTorrentPool().setPriority(selectedRows, 0);
		}else if(cmd.equals(sublocal[1])){
			try {
				if(selectedRows.length == 1)
					NTorrent.settings.runProgram(C.MC.getTorrentPool().get(selectedRows[0]));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if(cmd.equals(subssh[1])){
			if(C.IO.getRpcLink() instanceof SshConnection){
				SshClient ssh = ((SshConnection)C.IO.getRpcLink()).getSsh();
				JFileChooser pf = new JFileChooser();
				pf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				pf.setDialogTitle("Save in directory");
				switch(pf.showSaveDialog(C.GC.getRootWin())){
				case JFileChooser.APPROVE_OPTION:
					if(selectedRows.length == 1){
						C.TC.startFileTransfer(ssh,C.MC.getTorrentPool().get(selectedRows[0]),pf.getSelectedFile());
					}
				}
				
			} else
				System.out.println("Only available on a ssh connection");
		}
	}
}
