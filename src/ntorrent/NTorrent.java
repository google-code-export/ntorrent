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

package ntorrent;

import java.net.MalformedURLException;

import javax.swing.UIManager;

import ntorrent.controller.Controller;

public class NTorrent{
	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException {
		System.out.println(
				"nTorrent  Copyright (C) 2007  Kim Eik\n" +
				"This program comes with ABSOLUTELY NO WARRANTY\n" +
				"This is free software, and you are welcome to redistribute it\n" +
				"under certain conditions.\n"
				);
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Controller.drawMainGui();
	    } 
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
		
	}
}
