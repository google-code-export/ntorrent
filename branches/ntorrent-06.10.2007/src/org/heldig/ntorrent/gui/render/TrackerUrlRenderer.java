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
package org.heldig.ntorrent.gui.render;


import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.heldig.ntorrent.model.TrackerInfo;


/**
 * @author Kim Eik
 *
 */
public class TrackerUrlRenderer implements TableCellRenderer {
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if(!(value instanceof TrackerInfo))
			return null;
		TrackerInfo t = (TrackerInfo)value;
		ImageIcon icon;
		if(t.isEnabled())
			icon = new ImageIcon("icons/started.png");
		else
			icon = new ImageIcon("icons/stopped.png");
		
		JLabel component = new JLabel(t.toString(),icon,JLabel.LEADING);
				
		if(isSelected){
			component.setOpaque(true);
			component.setBackground(table.getSelectionBackground());
			component.setForeground(table.getSelectionForeground());
		}
		
		return component;
	}

}
