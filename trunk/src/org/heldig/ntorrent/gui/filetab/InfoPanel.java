package org.heldig.ntorrent.gui.filetab;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.heldig.ntorrent.gui.torrent.TorrentInfo;


/**
 * @author   netbrain
 */
public class InfoPanel extends JScrollPane {
	private static final long serialVersionUID = 1L;
	private final static JPanel container = new JPanel(new GridLayout(0,1,10,5));

	
	public InfoPanel(){
		super(container);
		container.setOpaque(false);
		container.setVisible(false);
	}
	
	public void setInfo(TorrentInfo tf){
		container.removeAll();
		String[] titles = {
				"Name", 			
				"Directory",		
				"Tied to file",		
				"Size",				
				"Files",			
				"Message",			
				"Priority"			
				
		};
		String[] values = {
				tf.toString(),
				tf.getFilePath(),
				tf.getTiedToFile(),
				""+tf.getByteSize(),
				""+tf.getNumFiles(),
				tf.getMessage(),
				""+tf.getPriority()
				
		};
		for(int x = 0; x < titles.length; x++)
			container.add(new JLabel(titles[x]+": "+values[x]));
		container.repaint();
		container.revalidate();
		container.setVisible(true);
	}
	
	public void hideInfo(){
		container.setVisible(false);
	}
}
