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
package ntorrent.gui.window;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import ntorrent.data.Environment;

public class Window extends JFrame {
	private static final long serialVersionUID = 1L;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private Dimension screenSize = toolkit.getScreenSize();
	private Image icon = toolkit.getImage("plugins/ntorrent/icons/ntorrent48.png");
	
	public Window(){
		this(Environment.appName);
	}
	
	public Window(String title){
		super(title);
		setIconImage(icon);
	}
	
	public void drawWindow(){
		validate();
		pack();
		
		int x = (screenSize.width - getWidth()) / 2;
		int y = (screenSize.height - getHeight()) / 2;
		
		setLocation(x, y);
		
		setVisible(true);
	}
	
	public void closeWindow(){
		setVisible(false);
		dispose();
	}
	
}