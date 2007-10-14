/**
 * 
 */
package org.heldig.ntorrent.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * @author Kim Eik
 *
 */
public class NativeAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e);
	}
}
