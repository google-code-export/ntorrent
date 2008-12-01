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
package ntorrent.torrenttable.view.renderers;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * 
 * @author Hans Hasert
 *
 */
public class IntegerRenderer extends JPanel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	private final JLabel intg = new JLabel();
	private final GridLayout layout = new GridLayout(0,1);
	
	boolean firstrun = true;
		
	public IntegerRenderer() {
		setLayout(layout);
		add(intg);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		if(firstrun){
			firstrun = false;
			intg.setFont(table.getFont());
			intg.setHorizontalAlignment(JLabel.RIGHT);
		}
		
		if (value == null)
			return null;
		
		Integer i = (Integer) value;
		intg.setText(i.toString()+" ");
				
		if(isSelected){
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
			intg.setForeground(table.getSelectionForeground());
		}else{
			setBackground(table.getBackground());
			setForeground(table.getForeground());
			intg.setForeground(table.getForeground());
		}
		return this;
	}
}