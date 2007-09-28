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

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import ntorrent.Controller;
import ntorrent.gui.core.AbstractJTablePopupMenu;
import ntorrent.gui.main.file.FileTabComponent;
import ntorrent.model.TorrentFile;
import ntorrent.settings.Constants.Commands;
import ntorrent.threads.TorrentCommandThread;

public class TorrentTableListener extends AbstractJTablePopupMenu{
	final static String[] menuItems = {
			Commands.START.toString(),
			Commands.STOP.toString(),
			null,
			Commands.OPEN.toString(),
			Commands.CHECK_HASH.toString(),
			Commands.CLOSE.toString(),
			null,
			Commands.REMOVE.toString()};
	
	public TorrentTableListener(){
		super(menuItems);
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
			FileTabComponent panel = Controller.getGui().getFileTab();
			panel.getInfoPanel().setInfo(tf);
			Controller.getRpc().getFileList(tf.getHash(),panel.getFileList());
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
    	FileTabComponent panel = Controller.getGui().getFileTab();
    	panel.getFileList().hideInfo();
    	panel.getInfoPanel().hideInfo();
	}
    
	public void actionPerformed(ActionEvent e) {
		Runnable torrentCommand = new TorrentCommandThread(e.getActionCommand(),selectedRows);
		Thread actionThread = new Thread(torrentCommand);
		actionThread.start();
	}   
}