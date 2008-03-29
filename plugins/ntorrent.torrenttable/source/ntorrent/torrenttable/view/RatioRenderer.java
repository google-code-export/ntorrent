/**
 *   nTorrent - A GUI client to administer a rtorrent process 
 *   over a network connection.
 *   
 *   Copyright (C) 2007  Hans Hasert
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

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import ntorrent.torrenttable.model.Ratio;

public class RatioRenderer extends JPanel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	private final JLabel rat = new JLabel();
	private final GridLayout layout = new GridLayout(0,1);
	
	boolean firstrun = true;
	
	public RatioRenderer() {
		setLayout(layout);
		add(rat);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		if(firstrun){
			firstrun = false;
			rat.setFont(table.getFont());
			rat.setHorizontalAlignment(JLabel.RIGHT);
		}
		
		if (value == null)
			return null;
		
		Ratio r = (Ratio) value;
		rat.setText(r.toString());
						
		if(isSelected){
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
			rat.setForeground(table.getSelectionForeground());
		}else{
			setBackground(table.getBackground());
			setForeground(table.getForeground());
			rat.setForeground(table.getForeground());
		}
		return this;
	}
}