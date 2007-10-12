/**
 * 
 */
package org.heldig.ntorrent.gui.viewlist;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.heldig.ntorrent.event.ControllerEventListener;
import org.heldig.ntorrent.language.Language;

/**
 * @author Kim Eik
 *
 */
public class ViewListComponent implements MouseListener, ListSelectionListener {
	final ListModel model = new ViewListModel();
	final JList viewList = new JList(model);
	final ControllerEventListener event;
	boolean changed = false;
	
	public ViewListComponent(ControllerEventListener e){
		event = e;
		viewList.setSelectedIndex(0);
		viewList.setLayoutOrientation(JList.VERTICAL);
		viewList.addMouseListener(this);
		viewList.addListSelectionListener(this);
		viewList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	public JList getViewList() {
		return viewList;
	}
	
	public ListModel getModel() {
		return model;
	}

	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(changed){
		Language l = (Language)model.getElementAt(((JList)e.getSource()).getSelectedIndex());
		event.viewListEvent(Language.toRawString(l.name()));
		changed = false;
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		changed = true;
	}
}
