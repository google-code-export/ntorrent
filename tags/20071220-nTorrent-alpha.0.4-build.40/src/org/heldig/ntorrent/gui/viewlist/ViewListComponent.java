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
public class ViewListComponent extends JList implements MouseListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;
	final static ListModel model = new ViewListModel();
	final ControllerEventListener event;
	boolean changed = false;
	
	public ViewListComponent(ControllerEventListener e){
		super(model);
		event = e;
		setSelectedIndex(0);
		setLayoutOrientation(JList.VERTICAL);
		addMouseListener(this);
		addListSelectionListener(this);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public ListModel getModel() {
		return model;
	}

	
	public void mouseClicked(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {}
	
	public void mouseExited(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) {}
	
	public void mouseReleased(MouseEvent e) {
		if(changed){
		Language l = (Language)model.getElementAt(((JList)e.getSource()).getSelectedIndex());
		event.viewListEvent(Language.toRawString(l.name()));
		changed = false;
		}
	}

	
	public void valueChanged(ListSelectionEvent e) {
		changed = true;
	}
}
