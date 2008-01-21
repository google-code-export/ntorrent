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
package ntorrent.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;

import ntorrent.io.settings.Constants;

public class GuiAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	ActionListener listener;
	
	public GuiAction(String key, ActionListener listener) {
		this.listener = listener;
		putValue(ACTION_COMMAND_KEY, key);
		putValue(NAME,Constants.messages.getString(key));
	}

	public void actionPerformed(ActionEvent e) {
		//TODO add own gui action logger?
		Logger.global.log(Level.INFO, e.getActionCommand());
		listener.actionPerformed(e);
	}

}
