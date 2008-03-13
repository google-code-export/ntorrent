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
package ntorrent.settings.model;

import java.awt.Component;
import java.io.Serializable;

/**
 * @author Kim Eik
 *
 */
public class SettingsElement implements SettingsExtension {
	
	private final SettingsExtension ext;
	private final String name;
	
	public SettingsElement(String name, SettingsExtension ext) {
		this.name = name;
		this.ext = ext;
	}

	public Component getDisplay() {
		return ext.getDisplay();
	}

	public void saveActionPerformed() {
		ext.saveActionPerformed();
	}
	
	@Override
	public String toString() {
		return name;
	}
}
