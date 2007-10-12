/**
 * 
 */
package org.heldig.ntorrent.gui.label;


import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author Kim Eik
 *
 */
public class LabelListComponent implements ListSelectionListener {
	final LabelListModel model = new LabelListModel();
	final JList labelList = new JList(model);
	
	public LabelListComponent() {
		labelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		labelList.setLayoutOrientation(JList.VERTICAL);
		labelList.addListSelectionListener(this);
	}
	
	public JList getLabelList() {
		return labelList;
	}
	
	public LabelListModel getModel() {
		return model;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		System.out.println(model.getElementAt(e.getFirstIndex()));
	}
}
