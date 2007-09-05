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
package ntorrent.gui.elements;

import java.awt.Image;
import java.awt.PopupMenu;

public class TrayIcon extends java.awt.TrayIcon {
	
	public TrayIcon(Image image) {
		super(image);
	}
	
	public TrayIcon(Image image, String string) {
		super(image,string);
	}
	
	
	public TrayIcon(Image image, String string, PopupMenu popup) {
		super(image, string, popup);
	}

	@Override
	public void displayMessage(String caption, String text, MessageType messageType) {
		// TODO format this messagebox to a more ntorrent'ish style.
		super.displayMessage(caption, text, messageType);
	}


}
