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
package ntorrent.torrenttable.view.renderers;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import ntorrent.torrenttable.model.DataUnit;

public class DataUnitRenderer extends JPanel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	private final JLabel dta = new JLabel();
	private final GridLayout layout = new GridLayout(0,1);
	
	boolean firstrun = true;
	String[] s1 = new String[20];
	String s2 = new String();
		
	public DataUnitRenderer() {
		setLayout(layout);
		add(dta);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		if(firstrun){
			firstrun = false;
			dta.setFont(table.getFont());
			dta.setHorizontalAlignment(JLabel.RIGHT);
		}
		
		if (value == null)
			return null;
		
		DataUnit d = (DataUnit) value;
		s2 = d.toString();
		s1 = s2.split(" ",2);
		
		if (s1[1].length() >= 3 )
		  dta.setText(String.format("%5s %4s ", s1[0],s1[1]));
		else 
			dta.setText(String.format("%5s %2s ", s1[0],s1[1]));
		
		if(isSelected){
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
			dta.setForeground(table.getSelectionForeground());
		}else{
			setBackground(table.getBackground());
			setForeground(table.getForeground());
			dta.setForeground(table.getForeground());
		}
		return this;
	}
}