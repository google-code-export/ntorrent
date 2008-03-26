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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import ntorrent.gui.window.Window;
import ntorrent.locale.ResourcePool;

/**
 * @author Kim Eik
 *
 */
public class AboutWindow extends Window  {

	private static final long serialVersionUID = 1L;
	private static final String data = 
			"Copyright (C) 2007  Kim Eik\n" +
			"This program comes with ABSOLUTELY NO WARRANTY\n" +
			"This is free software, and you are welcome to\n" +
			"redistribute it under certain conditions.";
	private static final String links = 
			"Project webpage:\nhttp://ntorrent.googlecode.com\n\n" +
			"How can i contribute?\nhttp://code.google.com/p/ntorrent/wiki/Contribute\n\n" +
			"Author: Kim Eik <kei060@gmail.com>";
	
	public AboutWindow(){
		super();
		setTitle(ResourcePool.getString("about", "locale", this));
		setResizable(false);
		//init image
		ImageIcon aboutImg = new ImageIcon("plugins/ntorrent/about.png");
		
		//init label containing image
		JLabel imgHolder = new JLabel();
		imgHolder.setIcon(aboutImg);
		imgHolder.setSize(350, 100);
		
		//init layout and panel
		BorderLayout layout = new BorderLayout();
		JPanel panel = new JPanel(layout);
		panel.setOpaque(false);
		
		//add image to panel
		panel.add(imgHolder,BorderLayout.NORTH);
		
		//set about content
		JTextPane text = new JTextPane();
		text.setOpaque(false);
		text.setText(data+"\n\n"+links);
		text.setEditable(false);
		
		
		JScrollPane scrollable = new JScrollPane(text);
		scrollable.getViewport().setOpaque(false);
		scrollable.setOpaque(false);
		scrollable.setBorder(BorderFactory.createEmptyBorder());
		scrollable.setViewportBorder(BorderFactory.createEmptyBorder());
		scrollable.getVerticalScrollBar().setBorder(BorderFactory.createEmptyBorder());
		
		//add about content
		panel.add(scrollable);
		
		//set close button
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		JButton close = new JButton(ResourcePool.getString("close", "locale", this));
		close.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				closeWindow();
			}
			
		});
		buttonPanel.add(close);
		
		//add close button
		panel.add(buttonPanel,BorderLayout.SOUTH);
		
		//set the content pane
		setContentPane(panel);
		setUndecorated(true);
		setBackground(Color.WHITE);
		setSize(350, 250);
	}
}
