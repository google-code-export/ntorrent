package ntorrent.gui.file;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ntorrent.model.TorrentFile;

/**
 * @author   netbrain
 */
public class InfoPanel {
	JPanel container = new JPanel(new GridLayout(0,1,10,5));
	JScrollPane infoPanel = new JScrollPane(container);

	
	public InfoPanel(){
		container.setOpaque(false);
		container.setVisible(false);
	}
	
	public void setInfo(TorrentFile tf){
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
	
	/**
	 * @return
	 * @uml.property  name="infoPanel"
	 */
	public JScrollPane getInfoPanel() {
		return infoPanel;
	}
}
