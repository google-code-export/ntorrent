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
import java.lang.annotation.*; 
import java.awt.Component;

/**
 * @author Kim Eik
 *
 */
public interface SettingsExtension {
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface UserSetting{
		String label() default "";
	}
	
	/**
	 * returns a string that gives a human understandable name of this plugin.
	 * This string will be shown in the settings menu.
	 * @return String
	 */
	public String toString();
	
	/**
	 * Fetch the user interface for user customizable values.
	 * if this returns null, then SettingsElement will autogenerate a display
	 * based on reflection data
	 * @return Component
	 */
	public Component getDisplay();
	
	/**
	 * this method is called when the save button has been pressed.
	 * Signals that the plugin should serialize the model connected to
	 * the display.
	 */
	public void saveActionPerformed() throws Exception;
}
