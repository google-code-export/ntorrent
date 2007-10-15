/**
 * 
 */
package org.heldig.ntorrent.gui.label;


import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.heldig.ntorrent.event.ControllerEventListener;

/**
 * @author Kim Eik
 *
 */
public class LabelListComponent extends JList implements ListSelectionListener {
	private static final long serialVersionUID = 1L;
	final static LabelListModel model = new LabelListModel();
	final ControllerEventListener event;
	
	public LabelListComponent(ControllerEventListener c) {
		super(model);
		event = c;
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setLayoutOrientation(JList.VERTICAL);
		addListSelectionListener(this);
	}
	
	public LabelListModel getModel() {
		return model;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting())
			event.labelSelectionEvent((String)getSelectedValue());
	}
	
}
