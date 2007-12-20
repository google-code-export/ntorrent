/**
 * 
 */
package org.heldig.ntorrent.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.heldig.ntorrent.NTorrent;

/**
 * @author Kim Eik
 *
 */
public class NativeAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	public void actionPerformed(ActionEvent e) {
		if(NTorrent.settings.debug)
			System.out.println(e.paramString());
	}
}
