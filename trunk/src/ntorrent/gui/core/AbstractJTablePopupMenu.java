package ntorrent.gui.core;


public abstract class AbstractJTablePopupMenu extends AbstractPopupMenu {
	protected int[] selectedRows;
	
	public AbstractJTablePopupMenu(String[] menuItems) {
		super(menuItems);
	}
	
}
