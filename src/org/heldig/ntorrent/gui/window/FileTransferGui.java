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
package org.heldig.ntorrent.gui.window;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.heldig.ntorrent.gui.Window;
import org.heldig.ntorrent.model.Byte;

import com.sshtools.j2ssh.FileTransferProgress;

/**
 * @author Kim Eik
 *
 */
public class FileTransferGui implements FileTransferProgress, ActionListener {
	Window fileTransfer;
	JProgressBar bar;
	JLabel file = new JLabel();
	boolean cancelled = false;
	
	public FileTransferGui(String title) {
		fileTransfer = new Window(title);
		fileTransfer.setLayout(new BorderLayout());
		fileTransfer.add(file,BorderLayout.NORTH);
		JPanel buttons = new JPanel();
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		buttons.add(cancel);
		fileTransfer.add(cancel,BorderLayout.SOUTH);
		
	}

	public void completed() {
		fileTransfer.requestFocus();
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void progressed(long arg0) {
		bar.setValue(((Long)arg0).intValue());
		bar.setString(new Byte(arg0).toString());
	}

	public void started(long arg0, String arg1) {
		file.setText(arg1);
		int endpoint = ((Long)arg0).intValue();
		bar = new JProgressBar(0,endpoint);
		bar.setStringPainted(true);
		fileTransfer.add(bar,BorderLayout.CENTER);
		fileTransfer.drawWindow();
		
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("Cancel")){
			cancelled = true;
			fileTransfer.dispose();
		}
		
	}
}
