package ntorrent.connection.view;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import ntorrent.connection.model.ConnectionProfile;
import ntorrent.connection.model.ConnectionProfileExtension;

public class ConnectionProfileRenderer implements ListCellRenderer {
	private final DefaultListCellRenderer renderer = new DefaultListCellRenderer();
	
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Component c = renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if(value != null){
			String label = null;
			if(value instanceof ConnectionProfileExtension){
				label = ((ConnectionProfileExtension)value).getName();
			}
			renderer.setText(label != null ? label : value.toString());
			renderer.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
		}
		return c;
	}

}
