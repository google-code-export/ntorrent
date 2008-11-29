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
package ntorrent.mvc;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import ntorrent.locale.ResourcePool;

public abstract class AbstractView implements View {
	public void errorOccured(String error) {
		JOptionPane.showMessageDialog(null, error,ResourcePool.getString("error", this),JOptionPane.ERROR_MESSAGE);
	}
	
	public void errorOccured(Throwable t) {
		errorOccured(t.getMessage());
	}
	
	public void display() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setContentPane(getDisplay());
		frame.pack();
		frame.validate();
		frame.setVisible(true);
	}
}
