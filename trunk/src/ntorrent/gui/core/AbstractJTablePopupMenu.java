package ntorrent.gui.core;

import java.awt.event.ActionEvent;
import java.util.Vector;

public class AbstractJTablePopupMenu extends AbstractPopupMenu {
	protected Vector<Integer> selectedRows = new Vector<Integer>();
	
	public AbstractJTablePopupMenu(String[] menuItems) {
		super(menuItems);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
