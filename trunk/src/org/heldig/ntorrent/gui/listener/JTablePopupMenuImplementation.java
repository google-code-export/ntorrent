package org.heldig.ntorrent.gui.listener;



public abstract class JTablePopupMenuImplementation extends JPopupMenuImplementation {
	protected int[] selectedRows;
	
	public JTablePopupMenuImplementation(Object[] menuItems) {
		super(menuItems);
	}	
}
