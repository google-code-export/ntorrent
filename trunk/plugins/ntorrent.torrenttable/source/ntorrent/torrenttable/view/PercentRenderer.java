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
package ntorrent.torrenttable.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import ntorrent.torrenttable.model.Percent;
import ntorrent.torrenttable.model.Torrent;

public class PercentRenderer extends JPanel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	private final JProgressBar pbar = new JProgressBar(0,100);
	
	public PercentRenderer() {
		super(new BorderLayout());
		add(pbar);
		setBorder(new EmptyBorder(2,1,1,2));
		pbar.setStringPainted(true);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
		Percent p = (Percent) value;
		pbar.setValue(p.getValue());
		pbar.setString(p.toString());
		
		if(!isSelected){
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}else{
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		}
		return this;
	}


}
