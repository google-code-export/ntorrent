/**
 * 
 */
package org.heldig.ntorrent.gui.label;

import javax.swing.AbstractListModel;

import org.heldig.ntorrent.language.Language;

/**
 * @author Kim Eik
 *
 */
public class LabelListModel extends AbstractListModel {
	private static final long serialVersionUID = 1L;
	Language[] data = {
			Language.Label_none
			};
	
	@Override
	public Object getElementAt(int index) {
		return data[index];
	}

	@Override
	public int getSize() {
		return data.length;
	}


}
