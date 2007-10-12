package org.heldig.ntorrent.gui.core;



public abstract class JTablePopupMenuImplementation extends JPopupMenuImplementation {
	protected int[] selectedRows;
	
	public JTablePopupMenuImplementation(Object[] menuItems) {
		super(menuItems);
	}	
}
