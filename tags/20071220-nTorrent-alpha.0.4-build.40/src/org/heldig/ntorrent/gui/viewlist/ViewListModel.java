/**
 * 
 */
package org.heldig.ntorrent.gui.viewlist;

import javax.swing.AbstractListModel;

import org.heldig.ntorrent.language.Language;

/**
 * @author Kim Eik
 *
 */
public class ViewListModel extends AbstractListModel {

	
	final Language[] data = {
			Language.Viewlist_main,
			Language.Viewlist_started,
			Language.Viewlist_stopped,
			Language.Viewlist_complete,
			Language.Viewlist_incomplete,
			Language.Viewlist_hashing,
			Language.Viewlist_seeding,
			};
	
	private static final long serialVersionUID = 1L;
	
	public Object getElementAt(int index) {
		return data[index];
	}

	
	public int getSize() {
		return data.length;
	}
	
}
